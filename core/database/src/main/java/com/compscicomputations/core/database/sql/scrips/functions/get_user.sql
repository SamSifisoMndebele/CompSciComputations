drop function get_user(_uid varchar(28));
create or replace function get_user(_uid varchar(28)) returns user_obj
    strict
    security definer
    parallel safe
    cost 25
    language plpgsql
as
$$
declare _user user_obj := null;
begin
    select
        fu.uid,
        fu.email,
        u.phone::text,
        u.usertype,
        u.is_admin,
        (to_jsonb(um) || fu.attrs)
            - ('{uid,localId,federatedId,createdAt,validSince,' ||
               'lastRefreshAt,providerUserInfo,lastLoginAt}')::text[]
    into _user
    from firebase_users fu
    join users u on fu.uid = u.uid
    join users_metadata um on u.uid = um.uid
    where u.uid = _uid;
    return _user;
end
$$;
comment on function get_user(varchar) is 'Returns user information of the given uid.';
alter function get_user(varchar) owner to postgres;
-- REVOKE ALL ON FUNCTION get_user(varchar) FROM PUBLIC;
grant execute on function get_user(varchar) to postgres, anon, authenticated, service_role, dashboard_user;

