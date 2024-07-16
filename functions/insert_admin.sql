create or replace procedure auth.insert_admin(
    _email text,
    _pin text,
    _names text,
    _last_name text,
    _password text DEFAULT NULL,
    _photo_url text DEFAULT NULL,
    _phone text DEFAULT NULL
) language plpgsql
as
$code$
declare
    _pin_id int;
    _id uuid;
begin
    select auth.validate_admin_pin(_email, _pin) into _pin_id;

    select auth.insert_user(_email,_names,_last_name,
                            ext.crypt(_password, ext.gen_salt('md5')),
                            _photo_url,_phone,true) into _id;

    insert into auth.admins (id, pin_id)
    values (_id, _pin_id);
end
$code$;