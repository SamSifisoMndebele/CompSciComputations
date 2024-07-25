
-- drop table if exists auth.students;
-- drop table if exists auth.password_otp;

drop table if exists auth.users;
create table if not exists auth.users(
    id int primary key generated always as identity,
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


create table if not exists auth.password_otp(
    id integer generated always as identity primary key,
    email text not null,
    otp_hash text not null,
    valid_until timestamptz default (now() at time zone 'SAST') + '5 min'::interval not null,

    foreign key (email) references auth.users (email) on update cascade on delete cascade
);

drop table if exists auth.admins_pins;
create table if not exists auth.admins_pins(
    id int generated always as identity primary key,
    email text unique not null,
    pin_hash text not null
);

drop table if exists auth.admins;
create table if not exists auth.admins (
    id int primary key,
    pin_id int not null,
    admin_since timestamptz default (now() at time zone 'SAST') not null,

    foreign key (id) references auth.users on delete cascade on update cascade ,
    foreign key (pin_id) references auth.admins_pins(id) on delete cascade on update cascade
);

create table if not exists auth.students (
    id int primary key,
    university text not null,
    school text not null,
    course text not null,

    foreign key (id) references auth.users(id) on delete cascade on update cascade
);