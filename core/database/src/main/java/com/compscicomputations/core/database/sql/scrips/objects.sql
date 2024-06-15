drop type user_obj cascade;
create type user_obj as (
    uid               varchar(28),  --not null key
    email             text,         --not null unique
--     display_name      text,         --null
    phone             text,         --null
--     photo_url         text,         --null
    usertype          usertype,     --not null
    is_admin          boolean,      --not null
--     is_email_verified boolean,      --not null
    metadata          jsonb         --null
);
alter type user_obj owner to postgres;


create type usertype as enum (
    'Student',
    'Tutor',
    'Other',
    'Admin',
    'Lecturer',
    'Developer'
);
alter type usertype owner to postgres;