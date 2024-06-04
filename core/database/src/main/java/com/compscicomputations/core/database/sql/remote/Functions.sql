
CREATE OR REPLACE PROCEDURE insert_user(
    _uid TEXT,
    _displayName TEXT,
    _email TEXT,
    _phone TEXT,
    _photoUrl TEXT,
    _userType TEXT
)
AS $$
BEGIN
    call check_error(_uid, 'The UID field can not be null or empty.');
    call check_error(_displayName, 'The DISPLAY NAME field can not be null or empty.');
    call check_error(_email, 'The EMAIL field can not be null or empty.');
    call check_error(_userType, 'The USER TYPE field can not be null or empty.');

    INSERT INTO "user" VALUES (_uid, _displayName, _email, _phone, _photoUrl, _userType::USERTYPE);
    INSERT INTO user_metadata VALUES (_uid);
END $$
LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION insert_admin_user (
    _uid VARCHAR,
    _displayName VARCHAR,
    _email VARCHAR,
    _phone VARCHAR,
    _photoUrl VARCHAR,
    _role_name VARCHAR,
    _code_chars CHARACTER(8)
) RETURNS BOOLEAN
AS $$
DECLARE
    _role_id INTEGER;
    _code_id INTEGER;
BEGIN
    SELECT insert_user(_uid, _displayName, _email, _phone, _photoUrl, 'Admin');
    SELECT r.id INTO _role_id FROM admin_role r WHERE r.name LIKE _role_name;
    IF _role_id ISNULL THEN
        RAISE EXCEPTION 'There is no such role available';
    END IF;
    SELECT ac.id INTO _code_id FROM admin_code ac WHERE ac.code = _code_chars;
    IF _code_id ISNULL THEN
        RAISE EXCEPTION 'The admin secrete code: % is not valid!', _code_chars;
    END IF;
    INSERT INTO admin VALUES (_uid, _role_id, _code_id);
    RETURN TRUE;
END $$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION insert_student_user (
    _uid VARCHAR,
    _displayName VARCHAR,
    _email VARCHAR,
    _phone VARCHAR,
    _photoUrl VARCHAR,
    _course VARCHAR,
    _school VARCHAR
)
RETURNS BOOLEAN
AS $$
BEGIN
    SELECT insert_user(_uid, _displayName, _email, _phone, _photoUrl, 'Student');
    INSERT INTO student VALUES (_uid, _course, _school);
    RETURN TRUE;

END $$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION timestampNow()
RETURNS TIMESTAMPTZ
AS $$
BEGIN
    RETURN NOW() AT TIME ZONE 'SAST';
END $$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_last_seen(_uid TEXT)
RETURNS TIMESTAMPTZ
AS $$
DECLARE TS TIMESTAMPTZ DEFAULT TIMESTAMPNOW();
BEGIN
    UPDATE user_metadata
    SET last_seen_at = TS
    WHERE uid = _uid;
    RETURN TS;
END $$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_last_sign_in(_uid TEXT)
RETURNS TIMESTAMPTZ
AS $$
DECLARE TS TIMESTAMPTZ DEFAULT TIMESTAMPNOW();
BEGIN
    UPDATE user_metadata
    SET last_sign_in_at = TS
    WHERE uid = _uid;
    RETURN TS;
END $$
LANGUAGE plpgsql;