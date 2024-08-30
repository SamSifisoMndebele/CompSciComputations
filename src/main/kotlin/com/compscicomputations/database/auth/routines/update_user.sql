drop function if exists auth.update_user;
create or replace function auth.update_user(
    _id uuid,
    _names text,
    _lastname text,
    _image bytea default null,
    _phone text default null,
    _is_email_verified boolean default false,
    _is_student boolean default false,
    _university text default null,
    _school text default null,
    _course text default null
) returns auth.user_row
    language plpgsql
as
$code$
declare
    _user auth.user_row;
begin
    update auth.users
    set names = _names,
        lastname = _lastname,
        image = _image,
        phone = _phone,
        is_email_verified = _is_email_verified
    where id = _id;

    if _is_student then
        insert into auth.students(id, university, school, course)
        values (_id, _university, _school, _course)
        on conflict (id)
        do update
        set university = excluded.university,
            school = excluded.school,
            course = excluded.course;
    else
        delete from auth.students
        where students.id = _id;
    end if;

    select * into strict _user
    from auth.select_user(_id := _id);
    return _user;

exception
    when no_data_found then
        raise exception no_data_found
        using message = _id::text || ': does not exists as a user!',
            hint = 'Register a new account.';

end
$code$;