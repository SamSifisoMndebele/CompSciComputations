CREATE SCHEMA if not exists ext;

CREATE EXTENSION IF NOT EXISTS pgcrypto
    SCHEMA ext
    CASCADE;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp"
    SCHEMA ext
    CASCADE;


-- Custom EXTENSIONS
create or replace function ext.isNullOrBlank(value text)
returns boolean
    language plpgsql
as
$$
begin
    return coalesce(length(trim(value)) = 0, true);
end;
$$;

create or replace function ext.isNullOrEmpty(value text)
returns boolean
    language plpgsql
as
$$
begin
    return coalesce(length(value) = 0, true);
end;
$$;

create or replace function ext.gen_random_otp(_digits int default 6)
returns text
    strict
    language plpgsql
as
$$
begin
    return floor((random() * 9 + 1) * power(10, _digits - 1))::text;
end;
$$;

create or replace function ext.nowSAST()
returns timestamp
    stable
    strict
    language plpgsql
as
$$
begin
    return (now() at time zone 'Africa/Johannesburg');
end;
$$;