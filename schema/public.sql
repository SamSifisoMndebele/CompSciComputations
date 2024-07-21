create type public.source_type as enum (
    'LOTTIE_URL',
    'IMAGE_URL',
    'IMAGE_BYTES'
);

drop table if exists public.onboarding_items;
create table public.onboarding_items2 (
    id int primary key generated always as identity not null,
    source bytea not null,
    title text not null,
    description text,
    source_type public.source_type not null,
    unique (title, description)
);

insert into public.onboarding_items2(source, title, description, source_type)
values ('13215'::bytea, 'title2', 'description2', 'IMAGE_BYTES');


drop table if exists public.feedbacks;
create table public.feedbacks (
    id int primary key generated always as identity,
    title text not null,
    body text not null,
    image_url text,
    user_id uuid default null,

    foreign key (user_id) references auth.users(id) on delete set null on update cascade
);

CREATE TABLE features (
    id SMALLINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    module_name TEXT NOT NULL UNIQUE,
    title TEXT NOT NULL UNIQUE,
    class_name TEXT,
    method_name TEXT
);
