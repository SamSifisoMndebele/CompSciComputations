DROP TABLE IF EXISTS "user";
DROP TABLE IF EXISTS "feature";
DROP TABLE IF EXISTS "user_feature";
DROP TABLE IF EXISTS "student";
DROP TABLE IF EXISTS "admin";
DROP TABLE IF EXISTS "role";
DROP TABLE IF EXISTS "professional";
DROP TABLE IF EXISTS "feedbacks";
DROP TABLE IF EXISTS "history";
DROP TABLE IF EXISTS "tutorials";
DROP TABLE IF EXISTS "admin_code";


CREATE TABLE "user" (
"uid" VARCHAR(32) PRIMARY KEY NOT NULL,
"display_name" VARCHAR,
"email" VARCHAR UNIQUE,
"phone" VARCHAR UNIQUE,
"photo_url" VARCHAR,
"user_type" VARCHAR NOT NULL,
"created_at" TIMESTAMPTZ NOT NULL,
"updated_at" TIMESTAMPTZ NOT NULL,
"last_sign_in_at" TIMESTAMPTZ,
"last_seen_at" TIMESTAMPTZ,
"banned_until" TIMESTAMPTZ,
"deleted_at" TIMESTAMPTZ,
"is_anonymous" BOOLEAN NOT NULL DEFAULT false);

CREATE TABLE "feature" (
"id" INTEGER PRIMARY KEY NOT NULL,
"module_name" TEXT NOT NULL UNIQUE,
"title" TEXT NOT NULL UNIQUE,
"class_name" TEXT,
"method_name" TEXT);

CREATE TABLE "user_feature" (
"uid" VARCHAR(32) NOT NULL,
"feature_id" INTEGER NOT NULL,
PRIMARY KEY ("uid","feature_id"));

CREATE TABLE "student" (
"uid" VARCHAR(32) PRIMARY KEY NOT NULL,
"course" VARCHAR NOT NULL,
"school" VARCHAR NOT NULL);

CREATE TABLE "admin" (
"uid" VARCHAR(32) PRIMARY KEY NOT NULL,
"code_id" INTEGER NOT NULL,
"role_id" INTEGER);

CREATE TABLE "role" (
"id" INTEGER PRIMARY KEY NOT NULL,
"name" VARCHAR NOT NULL);

CREATE TABLE "professional" (
"uid" VARCHAR(32) PRIMARY KEY NOT NULL,
"profession" VARCHAR NOT NULL);

CREATE TABLE "feedbacks" (
"id" INTEGER PRIMARY KEY NOT NULL,
"title" VARCHAR NOT NULL,
"content" VARCHAR NOT NULL,
"image_url" VARCHAR,
"uid" VARCHAR(32) NOT NULL);

CREATE TABLE "history" (
"id" INTEGER PRIMARY KEY NOT NULL,
"ft_id" INTEGER NOT NULL,
"data" JSONB NOT NULL,
"time" TIMESTAMP NOT NULL);

CREATE TABLE "tutorials" (
"id" INTEGER NOT NULL,
"step" INTEGER NOT NULL,
"title" VARCHAR NOT NULL,
"url" VARCHAR NOT NULL,
"content" VARCHAR NOT NULL,
"image_url" VARCHAR,
"updated_at" TIMESTAMP NOT NULL,
"ft_id" INTEGER NOT NULL,
PRIMARY KEY ("id","step"));

CREATE TABLE "admin_code" (
"id" INTEGER PRIMARY KEY NOT NULL,
"code" CHARACTER(8) NOT NULL UNIQUE);

ALTER TABLE "user_feature" ADD CONSTRAINT "user_feature_uid_user_uid" FOREIGN KEY ("uid") REFERENCES "user"("uid") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "user_feature" ADD CONSTRAINT "user_feature_feature_id_feature_id" FOREIGN KEY ("feature_id") REFERENCES "feature"("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "student" ADD CONSTRAINT "student_uid_user_uid" FOREIGN KEY ("uid") REFERENCES "user"("uid") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "admin" ADD CONSTRAINT "admin_uid_user_uid" FOREIGN KEY ("uid") REFERENCES "user"("uid") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "admin" ADD CONSTRAINT "admin_code_id_admin_code_id" FOREIGN KEY ("code_id") REFERENCES "admin_code"("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "admin" ADD CONSTRAINT "admin_role_id_role_id" FOREIGN KEY ("role_id") REFERENCES "role"("id") ON DELETE SET NULL ON UPDATE CASCADE;
ALTER TABLE "professional" ADD CONSTRAINT "professional_uid_user_uid" FOREIGN KEY ("uid") REFERENCES "user"("uid") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "feedbacks" ADD CONSTRAINT "feedbacks_uid_user_uid" FOREIGN KEY ("uid") REFERENCES "user"("uid") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "history" ADD CONSTRAINT "history_ft_id_feature_id" FOREIGN KEY ("ft_id") REFERENCES "feature"("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "tutorials" ADD CONSTRAINT "tutorials_ft_id_feature_id" FOREIGN KEY ("ft_id") REFERENCES "feature"("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
