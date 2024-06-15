create or replace function update_last_seen(_uid varchar) returns timestamp with time zone
    strict
    security definer
    parallel safe
    cost 10
    language plpgsql
as
$$
declare
    _ts timestamptz;
begin
    update users_metadata
    set last_seen_at = (now() at time zone 'SAST')
    where uid = _uid
    returning last_seen_at into _ts;
    return _ts;
end;
$$;

alter function update_last_seen(varchar) owner to postgres;
grant execute on function update_last_seen(varchar) to postgres, anon, authenticated, service_role, dashboard_user;