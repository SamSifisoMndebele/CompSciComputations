
drop function if exists upsert_user;
create or replace function upsert_user(
    _uid varchar,
    _phone text DEFAULT NULL,
    _usertype text DEFAULT NULL,
    _is_admin boolean DEFAULT NULL
) returns users
    security definer
    cost 10
    language plpgsql
as
$$
declare
    _user users;
begin
    select * into _user from users u where u.uid = _uid;
    insert into users
    values (
            _uid,
            (select fu.email from firebase_users fu where fu.uid = _uid),
            coalesce(_phone, _user.phone),
            coalesce(_usertype::usertype, _user.usertype),
            coalesce(_is_admin, _user.is_admin))
    on conflict (uid)
    do update
    set email = excluded.email,
        phone = excluded.phone,
        usertype = excluded.usertype,
        is_admin = excluded.is_admin
    returning * into _user;
    return _user;
end
$$;

alter function upsert_user(varchar, text, text, boolean) owner to postgres;
grant execute on function upsert_user(varchar, text, text, boolean) to postgres, anon, authenticated, service_role, dashboard_user;

