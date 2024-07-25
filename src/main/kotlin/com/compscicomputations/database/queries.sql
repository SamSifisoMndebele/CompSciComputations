SET TIME ZONE 'Africa/Johannesburg';

SELECT
    name,
    abbrev,
    utc_offset,
    is_dst
FROM pg_timezone_names;