drop table if exists auth.users;
create table if not exists auth.users(
    id uuid primary key default ext.gen_random_uuid() not null,
    email text unique not null,
    names text not null,
    last_name text not null,
    password_hash text default null,
    photo_url text default null,
    phone text default null,
    is_admin boolean default false not null,
    is_student boolean default false not null,
    created_at timestamptz default (now() at time zone 'SAST') not null,
    updated_at timestamptz default null
);

drop table if exists auth.admins_pins;
create table if not exists auth.admins_pins(
    id int primary key generated always as identity,
    email text unique not null,
    pin_hash text not null
);

drop table if exists auth.admins;
create table if not exists auth.admins (
    id uuid primary key not null,
    pin_id int not null,
    admin_since timestamptz default (now() at time zone 'SAST') not null,

    foreign key (id) references auth.users(id) on delete cascade on update cascade ,
    foreign key (pin_id) references auth.admins_pins(id) on delete cascade on update cascade
);

drop table if exists auth.students;
create table if not exists auth.students (
    id uuid primary key not null,
    course text not null,
    school text not null,

    foreign key (id) references auth.users(id) on delete cascade on update cascade
);