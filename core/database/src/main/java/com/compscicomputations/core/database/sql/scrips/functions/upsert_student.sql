create or replace function upsert_student(
    _uid character varying,
    _phone text DEFAULT NULL,
    _usertype text DEFAULT NULL,
    _course text DEFAULT NULL,
    _school text DEFAULT NULL
) returns boolean
    security definer
    language plpgsql
as
$$
declare
    _student students;
begin
    select * into _student from students s where s.uid = _uid;
    if not exists(select * from users where uid = _uid) then
        perform upsert_user(_uid, _phone, _usertype, false);
    end if;
    insert into students
    values (_uid,
            coalesce(_course, _student.course),
            coalesce(_school, _student.school))
    on conflict (uid)
    do update
    set course = excluded.course,
        school = excluded.school;
    return true;
end
$$;

alter function upsert_student(varchar, text, text, text, text) owner to postgres;
grant execute on function upsert_student(varchar, text, text, text, text) to postgres, anon, authenticated, service_role, dashboard_user;