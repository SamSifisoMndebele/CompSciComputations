create schema storage;

drop table if exists storage.files;
create table if not exists storage.files(
     id integer generated always as identity primary key,
     name text not null,
     description text,
     data bytea not null,
     size text not null,
     created_at timestamptz default (now() at time zone 'SAST') not null
);