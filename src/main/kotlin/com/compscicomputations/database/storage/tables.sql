create schema storage;

drop table if exists storage.files;
create table if not exists storage.files(
     id uuid primary key default ext.gen_random_uuid(),
     name text not null,
     description text,
     data bytea not null,
     size text not null,
     created_at timestamp default ext.nowsast() not null
);