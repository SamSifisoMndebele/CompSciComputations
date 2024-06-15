--- DROP TYPE IF EXISTS USERTYPE CASCADE;
DROP TABLE IF EXISTS admins_roles CASCADE;
DROP TABLE IF EXISTS features CASCADE;
DROP TABLE IF EXISTS users_features CASCADE;
DROP TABLE IF EXISTS feedbacks CASCADE;
DROP TABLE IF EXISTS histories CASCADE;


DROP FOREIGN TABLE IF EXISTS firebase_users;
CREATE FOREIGN TABLE firebase_users (
    uid VARCHAR(28) NOT NULL,
    email TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    attrs JSONB NOT NULL
)
SERVER firebase_server
OPTIONS ( OBJECT 'auth/users' );

DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users (
    uid VARCHAR(28) PRIMARY KEY,
    email TEXT NOT NULL UNIQUE,
--     display_name TEXT,                   jsonb_extract_path_text(attrs, 'displayName')
    phone VARCHAR(16),
--     photo_url TEXT NULL,                 jsonb_extract_path_text(attrs, 'photoUrl')
    usertype USERTYPE NOT NULL,
    is_admin BOOLEAN NOT NULL
--     is_email_verified BOOLEAN NOT NULL   jsonb_extract_path_text(attrs, 'emailVerified')::boolean
);

DROP TABLE IF EXISTS users_metadata CASCADE;
CREATE TABLE users_metadata (
    uid VARCHAR(28) PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL DEFAULT (NOW() AT TIME ZONE 'SAST'),
    updated_at TIMESTAMPTZ NOT NULL,
    admin_since TIMESTAMPTZ DEFAULT NULL,
    last_seen_at TIMESTAMPTZ DEFAULT NULL,
    banned_until TIMESTAMPTZ DEFAULT NULL,
    deleted_at TIMESTAMPTZ DEFAULT NULL,

    FOREIGN KEY (uid) REFERENCES users(uid) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS admins_codes CASCADE;
CREATE TABLE admins_codes (
    id SMALLINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    email TEXT UNIQUE NOT NULL,
    hash_code TEXT NOT NULL UNIQUE
);

DROP TABLE IF EXISTS admins CASCADE;
CREATE TABLE admins (
    uid VARCHAR(28) PRIMARY KEY,
    code_id SMALLINT NOT NULL,

    FOREIGN KEY (uid) REFERENCES users(uid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (code_id) REFERENCES admins_codes(id) ON DELETE RESTRICT ON UPDATE CASCADE
);

DROP TABLE IF EXISTS students CASCADE;
CREATE TABLE students (
    uid VARCHAR(28) PRIMARY KEY,
    course TEXT NOT NULL,
    school TEXT NOT NULL,

    FOREIGN KEY (uid) REFERENCES users(uid) ON DELETE CASCADE ON UPDATE CASCADE
);