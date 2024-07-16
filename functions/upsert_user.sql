create or replace function auth.upsert_user(
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
    on conflict (email)
    do update
    set names = excluded.names,
        lastname = excluded.lastname,
        password_hash = excluded.password_hash,
        photo_url = excluded.photo_url,
        phone = excluded.phone,
        is_admin = excluded.is_admin,
        is_student = excluded.is_student,
        updated_at = (now() at time zone 'SAST')
    returning id into _id;
    return _id;
end
$code$;