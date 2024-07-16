create or replace procedure auth.insert_admin(
    _email text,
    _pin text,
    _names text,
    _last_name text,
    _password text default null,
    _photo_url text default null,
    _phone text default null,
    _is_student boolean default false
) language plpgsql
as
$code$
declare
    _pin_id int;
    _id uuid;
begin
    select auth.validate_admin_pin(_email, _pin)
    into _pin_id;

    insert into auth.users(email, names, last_name, password_hash, photo_url, phone, is_admin, is_student)
    values (_email, _names, _last_name, ext.crypt(_password, ext.gen_salt('md5')), _photo_url, _phone, true, _is_student)
    on conflict (email)
    do update
    set names = excluded.names,
        last_name = excluded.last_name,
        password_hash = excluded.password_hash,
        photo_url = excluded.photo_url,
        phone = excluded.phone,
        is_admin = excluded.is_admin,
        updated_at = (now() at time zone 'SAST')
    returning id into _id;

    insert into auth.admins (id, pin_id)
    values (_id, _pin_id);
end
$code$;