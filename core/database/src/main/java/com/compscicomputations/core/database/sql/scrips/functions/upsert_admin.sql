create or replace function upsert_admin_code(_email text, _code text) returns boolean
    security definer
    language plpgsql
as
$$
begin
    insert into admins_codes(email, hash_code)
    values (_email, crypt(_code, gen_salt('md5')))
    on conflict (email)
    do update
    set hash_code = excluded.hash_code;
    return true;
end;
$$;
alter function upsert_admin_code(text, text) owner to postgres;
grant execute on function upsert_admin_code(text, text) to postgres, anon, authenticated, service_role, dashboard_user;


create or replace function upsert_admin(
    _uid varchar,
    _phone text DEFAULT NULL,
    _usertype text DEFAULT NULL,
    _admin_code text DEFAULT NULL
) returns boolean
    security definer
    language plpgsql
as
$$
declare
    _user users;
    _student students;
    _code admins_codes;
begin
    select * into _student from students s where s.uid = _uid;
    select * into _user from users where uid = _uid;
    if _user is null then
        select * into _user from upsert_user(_uid, _phone, _usertype, true);
    end if;
    select * into _code from admins_codes
    where email = _user.email;
    if _code is null or (_code.hash_code != crypt(_admin_code, _code.hash_code)) then
        raise exception 'The admin secrete code: % is not valid!', _admin_code;
    end if;
    insert into admins
    values (_uid, _code.id)
    on conflict(uid)
    do update
    set code_id = excluded.code_id;
    return true;
end
$$;
alter function upsert_admin(varchar, text, text, text) owner to postgres;
grant execute on function upsert_admin(varchar, text, text, text) to postgres, anon, authenticated, service_role, dashboard_user;