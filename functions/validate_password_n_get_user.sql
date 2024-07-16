create or replace function auth.validate_password_n_get_user(
    _email text,
    _password text
) returns auth.users
    security definer
    language plpgsql
as
$code$
declare
    _users auth.users;
begin
    if ext.isNullOrBlank(_email) or ext.isNullOrBlank(_password) then
        raise exception 'Invalid Parameters, email and password should not be null or empty';
    end if;
    select * into _users
    from auth.users
    where email like _email;

    if _users.password_hash is null then
        raise exception 'The user with email: % does not exists or password auth not available!', _email;
    elsif _users.password_hash <> ext.crypt(_password, _users.password_hash) then
        raise exception 'The user password: % is not valid!', _password;
    else
        return _users;
    end if;
end
$code$;