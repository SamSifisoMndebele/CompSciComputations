create or replace function handle_user_metadata() returns trigger
    language plpgsql
as
$$
declare
    _created_at timestamptz;
    _timestamp timestamptz := (now() at time zone 'SAST');
begin
    select created_at into _created_at from firebase_users where uid like new.uid;
    if tg_table_name = 'users' then
        if new.is_admin then
            insert into users_metadata(uid, created_at, updated_at, admin_since)
            values (new.uid, _created_at, _timestamp, _timestamp)
            on conflict (uid)
            do update
            set updated_at = excluded.updated_at,
                admin_since = excluded.admin_since;
            return new;
        end if;
    end if;
    insert into users_metadata(uid, created_at, updated_at)
    values (new.uid, _created_at, _timestamp)
    on conflict (uid)
    do update
    set updated_at = excluded.updated_at;
    return new;
end
$$;
alter function handle_user_metadata() owner to postgres;
grant execute on function handle_user_metadata() to anon, authenticated, service_role;


create or replace trigger update_user_metadata
after insert or update on users
    for each row
    execute function handle_user_metadata();

create or replace trigger update_student_metadata
after insert or update on students
    for each row
    execute function handle_user_metadata();

create or replace trigger update_admin_metadata
after insert or update on admins
    for each row
    execute function handle_user_metadata();