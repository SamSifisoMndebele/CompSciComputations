CREATE SCHEMA if not exists ext;

CREATE EXTENSION IF NOT EXISTS pgcrypto
    SCHEMA ext
    CASCADE;

-- CREATE EXTENSION IF NOT EXISTS citext
--     SCHEMA ext
--     CASCADE;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp"
    SCHEMA ext
    CASCADE;


-- Custom EXTENSIONS
create or replace function ext.isNullOrBlank(value text)
returns boolean
    language plpgsql
as
$code$
begin
    return coalesce(length(trim(value)) = 0, true);
end;
$code$;
create or replace function ext.isNullOrEmpty(value text)
returns boolean
    language plpgsql
as
$code$
begin
    return coalesce(length(value) = 0, true);
end;
$code$;