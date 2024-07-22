drop function if exists auth.insert_user;
create or replace function auth.insert_user(
    _email text,
    _password text,
    _display_name text,
    _photo_id int default null
) returns auth.users
    language plpgsql
as
$code$
declare
    _user auth.users;
begin
    insert into auth.users (email, password_hash, display_name, photo_id)
    values (_email, ext.crypt(_password, ext.gen_salt('md5')), _display_name, _photo_id)
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