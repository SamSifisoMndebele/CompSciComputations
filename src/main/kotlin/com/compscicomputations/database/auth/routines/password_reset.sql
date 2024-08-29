
drop function if exists auth.create_otp;
create or replace function auth.create_otp(
    _email text
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

exception
    when foreign_key_violation then
        raise exception 'User with email: % does not exist.', _email
            using hint = 'Register a new account.';
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
            using hint = 'Request a new OTP.';
    elseif _rec.valid_until < ext.nowsast() then
        raise exception 'The OTP: % has expired!', _otp
            using hint = 'Request a new OTP.';
    end if;

exception
    when no_data_found then
        raise exception 'OTP for email: % does not exists or it has expired.', _email
            using hint = 'Request a new OTP.';
end
$code$;


drop procedure if exists auth.reset_password_otp;
create or replace procedure auth.reset_password_otp(
    _email text,
    _password text,
    _otp text
) language plpgsql
as
$code$
begin
    call auth.validate_otp(_email, _otp);

    update auth.users
    set password = ext.crypt(_password, ext.gen_salt('md5'))
    where email like lower(_email);

    delete from auth.otps
    where email like lower(_email);
end
$code$;







drop procedure if exists auth.reset_password;
create or replace procedure auth.reset_password(
    _email text,
    _password text,
    _old_password text
) language plpgsql
as
$code$
declare _password text;
begin
    select password into strict _password
    from auth.users
    where email like lower(_email);

    if _password is null then
        raise exception 'Your email: %, do not have a password!', _email
            using hint = 'Request a new OTP to set your password.';
    elsif _password <> ext.crypt(_old_password, _password) then
        raise exception 'Your password: % is not valid!', _old_password;
    end if;

    update auth.users
    set password = ext.crypt(_password, ext.gen_salt('md5'))
    where email like lower(_email);

exception
    when no_data_found then
        raise exception 'User with email: % does not exists.', _email
            using hint = 'Register a new account';
end
$code$;