
create or replace view admins_users
AS
    SELECT *
    FROM users u
    NATURAL JOIN admins a
    JOIN admins_codes ac ON a.code_id = ac.id;

create or replace view students_users
AS
    SELECT * FROM users
    NATURAL JOIN students;
