drop function if exists auth.create_otp;
create or replace function auth.create_otp(
    _email text,
    _is_user boolean default null
) returns auth.otps
    security definer
    language plpgsql
as
$code$
declare
    _otp text := ext.gen_random_otp();
    _rec record;
begin
    if ext.isNullOrBlank(_email) then
        raise exception 'Invalid Parameters, email should not be null or empty';
    end if;
    if _is_user notnull then
        if _is_user and not exists(select * from auth.users where email like _email) then
            raise exception no_data_found
                using message = 'User with email: ' || _email || ' does not exists',
                    hint = 'Register a new account.';
        end if;
        if not _is_user and exists(select * from auth.users where email like _email) then
            raise exception unique_violation
                using message = 'User with email: ' || _email || ' already exists',
                    hint = 'Login in with your email.';
        end if;
    end if;

--     Delete old OTPs
    delete from auth.otps
    where valid_until < (ext.nowsast() - '10 min'::interval);

--     Add new OTP
    insert into auth.otps (email, otp, valid_until)
    values (lower(_email), ext.crypt(_otp, ext.gen_salt('md5')), (ext.nowsast() + '5 min'::interval))
    on conflict (email)
        do update
        set otp = excluded.otp,
            valid_until = excluded.valid_until
    returning id, email, _otp, valid_until into strict _rec;

    return _rec;
end
$code$;


drop function if exists auth.validate_otp;
create or replace procedure auth.validate_otp(
    _email text,
    _otp text
)
    security definer
    language plpgsql
as
$code$
declare
    _rec record;
begin
    --     Get latest OTP
    select * into strict _rec
    from auth.otps
    where email like lower(_email);

--     Validate OTP
    if _rec.otp <> ext.crypt(_otp, _rec.otp) then
        raise exception 'The OTP: % is not valid!', _otp
            using hint = 'Request a new OTP.',
                errcode = 'invalid_password';
    elseif _rec.valid_until < ext.nowsast() then
        raise exception 'The OTP: % has expired!', _otp
            using hint = 'Request a new OTP.',
                errcode = 'invalid_password';
    end if;

exception
    when no_data_found then
        raise exception 'OTP for email: % does not exists or it has expired.', _email
            using hint = 'Request a new OTP.',
                errcode = 'invalid_password';
end
$code$;