drop view auth.admins_users;

create or replace view auth.admins_users
as
select u.id,
       u.email,
       u.names,
       u.lastname,
       u.photo_url,
       u.phone,
       u.is_student,
       u.created_at,
       u.updated_at,
       a.pin_id,
       a.admin_since
from auth.users u
join auth.admins a using (id);

comment on view auth.admins_users is 'SELECT, UPDATE or DELETE on VIEW: Admin Users.';

create or replace rule _UPDATE
as
on update to auth.admins_users
do instead (
    update auth.users
    set email = new.email,
        names = new.names,
        lastname = new.lastname,
        photo_url = new.photo_url,
        phone = new.phone,
        is_student = new.is_student,
        updated_at = (now() at time zone 'SAST')
    where id = new.id;
);

create or replace rule _DELETE
as
on delete to auth.admins_users
do instead (
    update auth.users
    set is_admin = false,
        updated_at = (now() at time zone 'SAST')
    where id = old.id;
    delete from auth.admins
    where id = old.id;
);


-- SAMPLE QUERIES

select * from auth.admins_users;

update auth.admins_users
set email = 'new_email',
    lastname = 'new_lastname'
where id = '29741171-435f-4f0d-be92-5b7ae5cab201';

delete from auth.admins_users
where id = '9b65160d-04f9-434f-a18e-852367446432';