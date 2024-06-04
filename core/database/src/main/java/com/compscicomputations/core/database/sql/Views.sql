DROP VIEW admin_user;

CREATE VIEW admin_user
AS
    SELECT u.*, ac.code, r.name
    FROM "user" u
    NATURAL JOIN "admin" a
    JOIN admin_code ac ON a.code_id = ac.id
    JOIN role r ON a.role_id = r.id;

CREATE VIEW student_user
AS
    SELECT * FROM "user"
    NATURAL JOIN "student";

CREATE VIEW professional_user
AS
    SELECT * FROM "user"
    NATURAL JOIN professional

