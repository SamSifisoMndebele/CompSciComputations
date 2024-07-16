create or replace function auth.handle_delete_admin()
returns trigger
    language plpgsql
as
$$
begin
    update auth.users
    set is_admin = false
    where id = old.id;
    return old;
end $$;

create or replace trigger delete_admin_trigger
after delete on auth.admins
for each row
execute function auth.handle_delete_admin();
