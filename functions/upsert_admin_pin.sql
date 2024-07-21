drop procedure if exists auth.upsert_admin_pin;
create or replace procedure auth.upsert_admin_pin(_email text, _pin text)
    language plpgsql
as
$$
begin
    insert into auth.admins_pins(email, pin_hash)
    values (_email, ext.crypt(_pin, ext.gen_salt('md5')))
    on conflict (email)
    do update
    set pin_hash = excluded.pin_hash;
end
$$;