drop view auth.students_users;
create or replace view auth.students_users
as
select u.id,
       u.email,
       u.names,
       u.lastname,
       u.photo_url,
       u.phone,
       u.is_admin,
       u.created_at,
       u.updated_at,
       s.course,
       s.school
from auth.users u
join auth.students s using (id);

comment on view auth.students_users is 'SELECT, UPDATE or DELETE on VIEW: Student Users.';

create or replace rule _UPDATE
as
on update to auth.students_users
do instead (
    update auth.users
    set email = new.email,
        names = new.names,
        lastname = new.lastname,
        photo_url = new.photo_url,
        phone = new.phone,
        is_admin = new.is_admin,
        updated_at = (now() at time zone 'SAST')
    where id = new.id;

    update auth.students
    set course = new.course,
        school = new.school
    where id = new.id;
);


create or replace rule _DELETE
as
on delete to auth.students_users
do instead (
    update auth.users
    set is_student = false,
        updated_at = (now() at time zone 'SAST')
    where id = old.id;
    delete from auth.students
    where id = old.id;
);


-- SAMPLE QUERIES

select * from auth.students_users;

update auth.students_users
set email = 'new_email',
    lastname = 'new_lastname'
where id = '29741171-435f-4f0d-be92-5b7ae5cab201';

delete from auth.students_users
where id = '29741171-435f-4f0d-be92-5b7ae5cab201';