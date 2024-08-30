drop procedure if exists auth.set_admin;
create or replace procedure auth.set_admin(
    _id uuid,
    _assigned_by uuid,
    _is_super boolean default false
) language plpgsql
as
$code$
declare
    _rec record;
begin
    select is_super into strict _rec
    from auth.admins
    where id = _assigned_by and is_super;

    insert into auth.admins (id, assigned_by, is_super)
    values (_id, _assigned_by, _is_super)
    on conflict (id)
    do update
    set assigned_by = excluded.assigned_by,
        is_super = excluded.is_super;

exception
    when no_data_found then
        raise exception no_data_found
        using message = 'You are not a super admin.',
            hint = 'Only a super admin is allowed to assign admins.';

    when foreign_key_violation then
        raise exception foreign_key_violation
        using message = 'The user: ' || _id || ' does not exists.';
end
$code$;