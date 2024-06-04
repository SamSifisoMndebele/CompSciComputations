-- DROP TYPE IF EXISTS "usertype" CASCADE;
DROP TABLE IF EXISTS "user" CASCADE;
DROP TABLE IF EXISTS "admin" CASCADE;
DROP TABLE IF EXISTS "student" CASCADE;
DROP TABLE IF EXISTS "user_metadata" CASCADE;
DROP TABLE IF EXISTS "admin_role" CASCADE;
DROP TABLE IF EXISTS "admin_code" CASCADE;
DROP TABLE IF EXISTS "feature" CASCADE;
DROP TABLE IF EXISTS "user_feature" CASCADE;
DROP TABLE IF EXISTS feedback CASCADE;

CREATE TYPE "usertype" AS ENUM (
    'Admin',
    'Student',
    'Other'
);

CREATE TABLE "user" (
    "uid" VARCHAR(28) PRIMARY KEY,
    "display_name" TEXT NOT NULL CHECK ( LENGTH(display_name) > 1 ),
    "email" TEXT UNIQUE NOT NULL,
    "phone" TEXT UNIQUE,
    "photo_url" TEXT,
    "user_type" USERTYPE NOT NULL
);

CREATE TABLE "admin" (
    "uid" VARCHAR(28) PRIMARY KEY,
    "role_id" SMALLINT NOT NULL,
    "code_id" SMALLINT NOT NULL
);

CREATE TABLE "student" (
    "uid" VARCHAR(28) PRIMARY KEY,
    "course" TEXT NOT NULL,
    "school" TEXT NOT NULL
);

CREATE TABLE "user_metadata" (
    "uid" VARCHAR(28) PRIMARY KEY,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT TIMESTAMPNOW(),
    "last_sign_in_at" TIMESTAMPTZ NOT NULL DEFAULT TIMESTAMPNOW(),
    "updated_at" TIMESTAMPTZ,
    "last_seen_at" TIMESTAMPTZ,
    "banned_until" TIMESTAMPTZ,
    "deleted_at" TIMESTAMPTZ
);

CREATE TABLE "admin_role" (
    "id" SMALLINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    "name" TEXT NOT NULL
);

CREATE TABLE "admin_code" (
    "id" SMALLINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    "code" CHARACTER(4) NOT NULL UNIQUE CHECK ( LENGTH(code) = 4 )
);

CREATE TABLE "feature" (
    "id" SMALLINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    "module_name" TEXT NOT NULL UNIQUE,
    "title" TEXT NOT NULL UNIQUE,
    "class_name" TEXT,
    "method_name" TEXT
);

CREATE TABLE "user_feature" (
    "uid" VARCHAR(28) NOT NULL,
    "feature_id" INT NOT NULL,
    PRIMARY KEY ("uid","feature_id")
);

CREATE TABLE "feedback" (
    "id" INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    "title" TEXT NOT NULL,
    "content" TEXT NOT NULL,
    "image_url" TEXT,
    "uid" VARCHAR(28) NOT NULL
);

CREATE TABLE "history" (
    "id" BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    "feat_id" INT NOT NULL,
    "data" JSONB NOT NULL,
    "time" TIMESTAMPTZ NOT NULL
);


ALTER TABLE "user_feature" ADD CONSTRAINT "user_feature_uid_user_uid"
    FOREIGN KEY ("uid") REFERENCES "user"("uid") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "user_feature" ADD CONSTRAINT "user_feature_feature_id_feature_id"
    FOREIGN KEY ("feature_id") REFERENCES "feature"("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "student" ADD CONSTRAINT "student_uid_user_uid"
    FOREIGN KEY ("uid") REFERENCES "user"("uid") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "admin" ADD CONSTRAINT "admin_uid_user_uid"
    FOREIGN KEY ("uid") REFERENCES "user"("uid") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "admin" ADD CONSTRAINT "admin_code_id_admin_code_id"
    FOREIGN KEY ("code_id") REFERENCES "admin_code"("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "admin" ADD CONSTRAINT "admin_role_id_admin_role_id"
    FOREIGN KEY ("role_id") REFERENCES "admin_role"("id") ON DELETE SET NULL ON UPDATE CASCADE;
ALTER TABLE "user_metadata" ADD CONSTRAINT "user_metadata_uid_user_uid"
    FOREIGN KEY ("uid") REFERENCES "user"("uid") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "feedback" ADD CONSTRAINT "feedback_uid_user_uid"
    FOREIGN KEY ("uid") REFERENCES "user"("uid") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "history" ADD CONSTRAINT "history_ft_id_feature_id"
    FOREIGN KEY ("feat_id") REFERENCES "feature"("id") ON DELETE NO ACTION ON UPDATE NO ACTION;