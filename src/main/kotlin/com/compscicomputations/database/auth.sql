drop table if exists auth.users;
drop table if exists auth.files;
-- drop table if exists auth.admins_pins;
-- drop table if exists auth.admins;
-- drop table if exists auth.students;
-- drop table if exists auth.password_otp;


create table if not exists auth.files(
     id integer generated always as identity primary key,
     name text not null,
     description text,
     data bytea not null,
     size text not null,
     created_at timestamptz default (now() at time zone 'SAST') not null
);

create table if not exists auth.users(
    id uuid primary key default ext.gen_random_uuid(),
    email text unique not null,
    password_hash text default null,
    display_name text not null,
    photo_id integer unique default null,
    phone text default null,
    is_admin boolean default false not null,
    is_student boolean default false not null,
    is_email_verified boolean default false not null,
    created_at timestamptz default (now() at time zone 'SAST') not null,
    updated_at timestamptz default null,

    foreign key (photo_id) references auth.files on delete cascade on update cascade
);