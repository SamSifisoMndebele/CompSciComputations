
INSERT INTO admin_code(code)
VALUES ('12345678'),
       ('24681012'),
       ('CODE1234');

INSERT INTO admin_role(name)
VALUES ('Admin'),
       ('Tutor'),
       ('Manager');

select insert_student_user('5M63', '_displayName', '_email',
              '_phone', '_photoUrl', '_course', '_school');

CALL insert_admin_user('tpxFBdqCXcannCKik7Jrdghbiy52', '_displayName', '_email2',
              '_phone2', '_photoUrl', 'Admin', 'CODE1234');


INSERT INTO student
VALUES ('jhbk451', 'yuhgjnj', 'bhjbnmk')
ON CONFLICT (uid)
DO UPDATE SET course = EXCLUDED.course
RETURNING *;