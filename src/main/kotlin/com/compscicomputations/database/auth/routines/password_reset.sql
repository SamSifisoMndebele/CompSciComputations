drop procedure if exists auth.reset_password_otp;
create or replace procedure auth.reset_password_otp(
    _email text,
    _new_password text,
    _otp text
) language plpgsql
as
$code$
begin
    call auth.validate_otp(_email, _otp);

    update auth.users
    set password = ext.crypt(_new_password, ext.gen_salt('md5'))
    where email like lower(_email);

    delete from auth.otps
    where email like lower(_email);
end
$code$;


drop procedure if exists auth.reset_password;
create or replace procedure auth.reset_password(
    _email text,
    _new_password text,
    _password text
) language plpgsql
as
$code$
declare _pass text;
begin
    select password into strict _pass
    from auth.users
    where email like lower(_email);

    if _pass is null then
        raise exception 'Your email: %, do not have a password!', _email
            using hint = 'Request a new OTP to set your password.';
    elsif _pass <> ext.crypt(_password, _pass) then
        raise exception 'Your password: % is not valid!', _password;
    end if;

    update auth.users
    set password = ext.crypt(_new_password, ext.gen_salt('md5'))
    where email like lower(_email);

exception
    when no_data_found then
        raise exception 'User with email: % does not exists.', _email
            using hint = 'Register a new account';
end
$code$;