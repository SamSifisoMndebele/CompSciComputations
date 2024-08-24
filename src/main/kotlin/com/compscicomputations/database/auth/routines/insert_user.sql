drop function if exists auth.insert_user;
create or replace function auth.insert_user(
    _email text,
    _names text,
    _lastname text,
    _password text default null,
    _image bytea default null,
    _is_email_verified boolean default false
) returns auth.users
    language plpgsql
as
$code$
declare
    _user auth.users;
begin
    insert into auth.users (email, password, names, lastname, image, is_email_verified)
    values (lower(_email), ext.crypt(_password, ext.gen_salt('md5')), _names, _lastname, _image, _is_email_verified)
    returning * into strict _user;

    if not _is_email_verified then
        perform auth.create_otp(_email);
    end if;

    return _user;

exception
    when unique_violation then
        raise exception 'User with email: % already exists', _email
        using hint = 'Login to your account or reset your forgotten password.';
    when foreign_key_violation then
        raise exception 'User image does not exist on the database';

end
$code$;