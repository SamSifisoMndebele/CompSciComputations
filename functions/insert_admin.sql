drop function if exists auth.insert_admin;
create or replace function auth.insert_admin(
    _email text,
    _password text,
    _pin text,
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
    _pin_id int;
    _user auth.users;
begin
    select auth.validate_admin_pin(_email, _pin)
    into _pin_id;

    insert into auth.users(email, names, lastname, password_hash, photo_url, phone, is_admin, is_student)
    values (_email, _names, _lastname, ext.crypt(_password, ext.gen_salt('md5')), _photo_url, _phone, true, _is_student)
    on conflict (email)
    do update
    set names = excluded.names,
        lastname = excluded.lastname,
        password_hash = excluded.password_hash,
        photo_url = excluded.photo_url,
        phone = excluded.phone,
        is_admin = excluded.is_admin,
        updated_at = (now() at time zone 'SAST')
    returning * into _user;

    insert into auth.admins (id, pin_id)
    values (_user.id, _pin_id);

    if _is_student then
        insert into auth.students
        values (_user.id, _course, _school);
    end if;

    return _user;
end
$code$;