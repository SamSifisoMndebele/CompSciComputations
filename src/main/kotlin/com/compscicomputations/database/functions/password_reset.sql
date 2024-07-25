
drop function if exists auth.create_otp;
create or replace function auth.create_otp(
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


drop function if exists auth.validate_otp;
create or replace function auth.validate_otp(
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


drop procedure if exists auth.reset_password_otp;
create or replace procedure auth.reset_password_otp(
    _email text,
    _password text,
    _otp text
) language plpgsql
as
$code$
begin
    select auth.validate_otp(_email, _otp);

    update auth.users
    set password_hash = ext.crypt(_password, ext.gen_salt('md5'))
    where email like _email;
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
declare _password_hash text;
begin
    select password_hash into strict _password_hash
    from auth.users
    where email like _email;

    if _password_hash is null then
        raise exception 'Your email: %, do not have a password!', _email
            using hint = 'Request OTP to set your password.';
    elsif _password_hash <> ext.crypt(_old_password, _password_hash) then
        raise exception 'Your password: % is not valid!', _old_password;
    end if;

    update auth.users
    set password_hash = ext.crypt(_password, ext.gen_salt('md5'))
    where email like _email;
end
$code$;