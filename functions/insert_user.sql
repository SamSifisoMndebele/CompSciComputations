drop function if exists auth.insert_user;
create or replace function auth.insert_user(
    _email text,
    _names text,
    _lastname text,
    _password text default null,
    _photo_url text default null,
    _phone text default null,
    _is_admin boolean default false,
    _is_student boolean default false
) returns uuid
    language plpgsql
as
$code$
declare _id uuid;
begin
    insert into auth.users(email, names, lastname, password_hash, photo_url, phone, is_admin, is_student)
    values (_email, _names, _lastname, ext.crypt(_password, ext.gen_salt('md5')), _photo_url, _phone, _is_admin, _is_student)
    returning id into _id;
    return _id;
end
$code$;