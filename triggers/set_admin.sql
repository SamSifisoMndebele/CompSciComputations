drop trigger if exists set_admin_trigger on auth.admins_pins;
drop function if exists auth.handle_set_admin;

create or replace function auth.handle_set_admin()
returns trigger
    language plpgsql
as
$$
declare _id uuid;
begin
    update auth.users
    set is_admin = true,
        updated_at = (now() at time zone 'SAST')
    where email = new.email
    returning id into _id;

    insert into auth.admins(id, pin_id)
    values (_id, new.id);

    return new;
end $$;

create or replace trigger set_admin_trigger
after insert on auth.admins_pins
for each row
execute function auth.handle_set_admin();
