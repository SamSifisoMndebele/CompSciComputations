
drop table if exists public.onboarding_items;
create table public.onboarding_items (
    id int primary key generated always as identity not null,
    title text not null unique,
    description text unique,
    image bytea
);

drop table if exists public.feedbacks;
create table if not exists public.feedbacks (
    id int primary key generated always as identity,
    subject text not null,
    message text not null,
    suggestion text,
    image bytea,
    user_id uuid default null,
    created_at timestamp default ext.nowsast() not null,

    foreign key (user_id) references auth.users(id) on delete set null on update cascade
);

drop table if exists public.features;
create table if not exists public.features (
    id smallint primary key generated always as identity,
    name text not null unique,
    module text not null unique,
    clazz text not null default 'MainActivity',
    icon bytea not null
);
