
-- drop table if exists auth.admins_pins;
-- drop table if exists auth.admins;
-- drop table if exists auth.students;
-- drop table if exists auth.password_otp;

drop table if exists auth.users;
create table if not exists auth.users(
    id uuid primary key default ext.gen_random_uuid(),
    email text unique not null,
    password_hash text default null,
    display_name text not null,
    image_bytes bytea default null,
    phone text default null,
    is_admin boolean default false not null,
    is_student boolean default false not null,
    is_email_verified boolean default false not null,
    created_at timestamptz default (now() at time zone 'SAST') not null,
    updated_at timestamptz default null
);