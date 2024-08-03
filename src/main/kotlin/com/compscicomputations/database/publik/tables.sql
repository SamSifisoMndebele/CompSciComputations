
drop table if exists public.onboarding_items;
create table public.onboarding_items (
    id int primary key generated always as identity not null,
    title text not null unique,
    description text unique,
    image_bytes bytea
);

drop table if exists public.feedbacks;
create table public.feedbacks (
    id int primary key generated always as identity,
    subject text not null,
    message text not null,
    suggestion text,
    image_bytes bytea,
    user_id int default null,
    created_at timestamp default ext.nowsast() not null,

    foreign key (user_id) references auth.users(id) on delete set null on update cascade
);





CREATE TABLE features (
    id SMALLINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    module_name TEXT NOT NULL UNIQUE,
    title TEXT NOT NULL UNIQUE,
    class_name TEXT,
    method_name TEXT
);
