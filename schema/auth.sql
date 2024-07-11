drop type if exists Usertype cascade;
create type Usertype as enum (
    'STUDENT',
    'TUTOR',
    'EXPERT',
    'OTHER'
);

drop table if exists users;
create table if not exists users(
    uid varchar(28) not null primary key,
    email text unique not null,
    display_name text not null,
    photo_url text default null,
    phone text unique default null,
    is_email_verified boolean not null default false,
    usertype auth.usertype default 'STUDENT' not null,
    created_at timestamptz default (now() at time zone 'SAST') not null,
    updated_at timestamptz default null,
    last_seen_at timestamptz default null,
    banned_until timestamptz default null,
    deleted_at timestamptz default null
);

drop table if exists students;
create table if not exists students (
    uid varchar(28) primary key ,
    course text not null ,
    school text not null ,

    foreign key (uid) references users(uid) on delete cascade on update cascade
);

drop table if exists admins_codes;
create table if not exists admins_codes(
    id int primary key generated always as identity,
    email text unique not null,
    hash_code text not null,
    created_at timestamptz default (now() at time zone 'SAST') not null,
    valid_until timestamptz default (now() at time zone 'SAST') + interval '1 day' not null
);

drop table if exists admins;
create table if not exists admins (
    uid varchar(28) primary key ,
    code_id int not null,
    admin_since timestamptz default (now() at time zone 'SAST') not null,

    foreign key (uid) references users(uid) on delete cascade on update cascade ,
    foreign key (code_id) references admins_codes(id) on delete restrict on update cascade
);