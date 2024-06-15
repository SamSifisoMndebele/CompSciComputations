
create or replace function delete_user(_uid varchar(28))
returns boolean
as $$
begin
    call check_error(_uid, 'The UID field can not be null or empty.');
    delete from users
    where uid = _uid;
    return true;
end $$
language plpgsql;

create or replace function delete_student_profile(_uid varchar(28))
returns boolean
as $$
begin
    call check_error(_uid, 'The UID field can not be null or empty.');
    delete from students
    where uid = _uid;
    return true;
end $$
language plpgsql;

create or replace function delete_admin_profile(_uid varchar(28))
returns users
as $$
declare _user users;
begin
    call check_error(_uid, 'The UID field can not be null or empty.');
    delete from admins
    where uid = _uid;
    update users
    set usertype = 'Student',
        is_admin = false
    where uid = _uid
    returning * into _user;
    return _user;
end $$
language plpgsql;
