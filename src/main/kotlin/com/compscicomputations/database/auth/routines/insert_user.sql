drop function if exists auth.insert_user;
create or replace function auth.insert_user(
    _email text,
    _names text,
    _lastname text,
    _image bytea default null,
    _is_email_verified boolean default false,
    _password text default null,
    _otp text default null,
    _phone text default null,
    _university text default null,
    _school text default null,
    _course text default null
) returns auth.user_row
    language plpgsql
as
$code$
declare
    _rec record;
    _user auth.user_row;
begin
    if _password notnull then
        call auth.validate_otp(_email, _otp);
    end if;

    insert into auth.users (email, password, names, lastname, image, is_email_verified, phone)
    values (lower(_email), ext.crypt(_password, ext.gen_salt('md5')), _names,
            _lastname, _image, _is_email_verified, _phone)
    returning id into strict _rec;

    if _university notnull and _school notnull and _course notnull then
        insert into auth.students (id, university, school, course)
        values (_rec.id, _university, _school, _course);
    end if;

    if not _is_email_verified then
        perform auth.create_otp(_email);
    end if;

    select * into strict _user
    from auth.select_user(_email := _email);
    return _user;

exception
    when unique_violation then
        raise exception unique_violation
        using message = 'User with email: ' || _email || ' already exists',
            hint = 'Login to your account or reset your forgotten password.';

end
$code$;