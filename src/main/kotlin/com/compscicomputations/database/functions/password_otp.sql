drop function if exists auth.create_password_otp;
drop function if exists auth.validate_password_otp;

create or replace function auth.create_password_otp(
    _email text
) returns auth.password_otp
    security definer
    language plpgsql
as
$code$
declare
    _otp text := ext.gen_random_otp();
    _password_otp auth.password_otp;
begin
    if ext.isNullOrBlank(_email) then
        raise exception 'Invalid Parameters, email should not be null or empty';
    end if;
--     Delete old OTPs
    delete from auth.password_otp
    where valid_until < (ext.nowsast() - '5 min'::interval);

--     Add new PasswordOTP
    insert into auth.password_otp (email, otp_hash, valid_until)
    values (_email, ext.crypt(_otp, ext.gen_salt('md5')), (ext.nowsast() + '5 min'::interval))
    on conflict (email)
    do update
    set otp_hash = excluded.otp_hash,
        valid_until = excluded.valid_until
    returning id, email, _otp, valid_until into strict _password_otp;

    return _password_otp;

exception
    when foreign_key_violation then
        raise exception 'User with email: % does not exist.', _email
            using hint = 'Register a new account.';
end
$code$;


create or replace function auth.validate_password_otp(
    _email text,
    _otp text
) returns boolean
    security definer
    language plpgsql
as
$code$
declare
    _pass_otp auth.password_otp := null;
begin
    if ext.isNullOrBlank(_email) or ext.isNullOrBlank(_otp) then
        raise exception 'Invalid Parameters, email and otp should not be null or empty';
    end if;

--     Get latest PasswordOTP
    select * into strict _pass_otp
    from auth.password_otp
    where email like _email;

--     Validate PasswordOTP
    if _pass_otp.otp_hash <> ext.crypt(_otp, _pass_otp.otp_hash) then
        raise exception 'The PasswordOTP: % is not valid!', _otp
            using hint = 'Request a new PasswordOTP.';
    elseif _pass_otp.valid_until < ext.nowsast() then
        raise exception 'The PasswordOTP: % has expired!', _otp
            using hint = 'Request a new PasswordOTP.';
    end if;

    return true;

exception
    when no_data_found then
        raise exception 'PasswordOTP for email: % does not exists or it has expired.', _email
            using hint = 'Request a new PasswordOTP.';

end
$code$;