
drop table if exists auth.users;
create table if not exists auth.users(
    id uuid primary key default ext.gen_random_uuid(),
    email text unique not null,
    password text default null,
    names text not null,
    lastname text not null,
    image bytea default null,
    phone text default null,
    created_at timestamp default ext.nowsast() not null,
    updated_at timestamp default null,
    is_email_verified boolean default false not null
);

drop table if exists auth.otps;
create table if not exists auth.otps(
    id integer primary key generated always as identity,
    email text not null unique,
    otp text not null,
    valid_until timestamp default (ext.nowsast() + '5 min'::interval) not null,

    foreign key (email) references auth.users(email) on update cascade on delete cascade
);

drop table if exists auth.admins;
create table if not exists auth.admins (
    id uuid primary key,
    assigned_by uuid default null,
    is_super boolean default false not null,
    admin_since timestamp default ext.nowsast() not null,

    foreign key (id) references auth.users(id) on delete cascade on update cascade,
    foreign key (assigned_by) references auth.admins(id) on delete set null on update cascade
);

drop table if exists auth.students;
create table if not exists auth.students (
    id uuid primary key,
    university text not null,
    school text not null,
    course text not null,

    foreign key (id) references auth.users(id) on delete cascade on update cascade
);