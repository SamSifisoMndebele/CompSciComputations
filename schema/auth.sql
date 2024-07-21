-- drop table if exists auth.users;
-- drop table if exists auth.admins_pins;
-- drop table if exists auth.admins;
-- drop table if exists auth.students;
-- drop table if exists auth.users_images;
-- drop table if exists auth.password_otp;


create table if not exists auth.users(
    id uuid primary key default ext.gen_random_uuid(),
    email text unique not null,
    password_hash text not null,
    display_name text not null,
    photo_url text default null,
    phone text default null,
    is_admin boolean default false not null,
    is_student boolean default false not null,
    is_email_verified boolean default false not null,
    created_at timestamptz default (now() at time zone 'SAST') not null,
    updated_at timestamptz default null
);

create table if not exists auth.users_images(
    id integer generated always as identity primary key,
    user_id uuid not null,
    name text not null,
    description text,
    data bytea not null,
    size text not null,
    created_at timestamptz default (now() at time zone 'SAST') not null,

    foreign key (user_id) references auth.users on update cascade on delete cascade
);

create table if not exists auth.admins_pins(
    id int generated always as identity primary key,
    email text unique not null,
    pin_hash text not null,
    created_at timestamptz default (now() at time zone 'SAST') not null
);

create table if not exists auth.admins (
    id uuid primary key,
    pin_id int not null,
    admin_since timestamptz default (now() at time zone 'SAST') not null,

    foreign key (id) references auth.users on delete cascade on update cascade ,
    foreign key (pin_id) references auth.admins_pins(id) on delete cascade on update cascade
);

create table if not exists auth.students (
    id uuid primary key,
    university text not null,
    school text not null,
    course text not null,

    foreign key (id) references auth.users(id) on delete cascade on update cascade
);




create table if not exists auth.password_otp(
    id integer generated always as identity primary key,
    email text not null,
    otp_hash text not null,
    created_at timestamptz default (now() at time zone 'SAST') not null,
    valid_until timestamptz default (now() at time zone 'SAST') + '10 min'::interval not null,

    foreign key (email) references auth.users (email) on update cascade on delete cascade
);


