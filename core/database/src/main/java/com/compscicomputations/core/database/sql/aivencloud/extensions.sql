CREATE SCHEMA extensions;
CREATE SCHEMA vault;


CREATE EXTENSION IF NOT EXISTS pgcrypto
    SCHEMA extensions
    CASCADE;

CREATE EXTENSION IF NOT EXISTS pgjwt
    SCHEMA extensions
    CASCADE;

-- CREATE EXTENSION IF NOT EXISTS pgsodium
--     SCHEMA extensions
--     CASCADE;

CREATE EXTENSION IF NOT EXISTS citext
    SCHEMA extensions
    CASCADE;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp"
    SCHEMA extensions
    CASCADE;

CREATE EXTENSION IF NOT EXISTS wrappers
    WITH SCHEMA extensions
    CASCADE;

-- CREATE EXTENSION IF NOT EXISTS pg_stat_statements
--     SCHEMA extensions
--     CASCADE;

CREATE EXTENSION IF NOT EXISTS supabase_vault
    WITH SCHEMA vault
    CASCADE;