
create or replace view auth.admins_users
AS
    SELECT * FROM auth.users
    NATURAL JOIN auth.admins;

create or replace view auth.students_users
AS
    SELECT * FROM auth.users
    NATURAL JOIN auth.students;
