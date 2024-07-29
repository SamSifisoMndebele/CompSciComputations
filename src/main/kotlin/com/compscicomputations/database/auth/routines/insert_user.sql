drop function if exists auth.insert_user;
create or replace function auth.insert_user(
    _email text,
    _display_name text,
    _password text default null,
    _image_bytes bytea default null,
    _is_email_verified boolean default false
) returns auth.users
    language plpgsql
as
$code$
declare
    _user auth.users;
begin
    insert into auth.users (email, password_hash, display_name, image_bytes, is_email_verified)
    values (_email, ext.crypt(_password, ext.gen_salt('md5')), _display_name, _image_bytes, _is_email_verified)
    returning * into strict _user;

    return _user;

exception
    when unique_violation then
        raise exception 'User with email: % already exists', _email
        using hint = 'Login to your account or reset your forgotten password.';
    when foreign_key_violation then
        raise exception 'User image does not exist on the database';

end
$code$;