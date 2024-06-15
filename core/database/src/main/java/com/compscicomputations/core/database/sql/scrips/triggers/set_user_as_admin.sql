create or replace function handle_set_user_as_admin() returns trigger
    language plpgsql
as
$$
declare
    _uid varchar;
begin
    update users
    set is_admin = true
    where email = new.email
    returning uid into _uid;

    if _uid is not null then
        insert into admins
        values (_uid, new.id);
    end if;
    return new;
end $$;
alter function handle_set_user_as_admin() owner to postgres;
grant execute on function handle_set_user_as_admin() to anon, authenticated, service_role;


create or replace trigger set_user_as_admin
after insert or update on admins_codes
for each row
execute function handle_set_user_as_admin();
