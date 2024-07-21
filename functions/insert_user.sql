drop function if exists auth.insert_user;
create or replace function auth.insert_user(
    _email text,
    _password text,
    _names text,
    _lastname text,
    _photo_url text default null,
    _phone text default null,
    _is_student boolean default false,
    _course text default null,
    _school text default null
) returns auth.users
    language plpgsql
as
$code$
declare
    _user record;
begin
    insert into auth.users(email, names, lastname, password_hash, photo_url, phone, is_student)
    values (_email, _names, _lastname, ext.crypt(_password, ext.gen_salt('md5')), _photo_url, _phone, _is_student)
    returning * into strict _user;

    if _user.is_student then
        insert into auth.students
        values (_user.id, _course, _school);
    end if;

    return _user;

exception
    when unique_violation then
        raise exception 'User with email: % already exists', '_email'
        using hint = 'Login to your account or reset your forgotten password.';

end
$code$;