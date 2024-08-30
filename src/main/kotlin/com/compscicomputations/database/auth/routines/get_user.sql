drop function if exists auth.get_user;
create or replace function auth.get_user(
    _email text,
    _password text
) returns auth.user_row
    security definer
    language plpgsql
as
$code$
declare _user auth.user_row;
begin
    select * into strict _user
    from auth.select_user(_email := _email);

    if _user.password is null then
        raise exception 'Your email: %, do not have a password!', _email
        using hint = 'Reset your password.';
    elsif _user.password <> ext.crypt(_password, _user.password) then
        raise exception 'Your password: % is not valid!', _password;
    else
        return _user;
    end if;

exception
    when no_data_found then
        raise exception no_data_found
        using message = 'User with email: ' || _email || ' does not exists',
            hint = 'Register a new account.';
end
$code$;


drop function if exists auth.select_user;
create or replace function auth.select_user(
    _id uuid default null,
    _email text default null
) returns auth.user_row
    security definer
    language plpgsql
as
$code$
declare _user auth.user_row;
begin
    if _id isnull and _email isnull then
        raise exception 'Invalid arguments, specify the user id or email!';
    end if;

    select id,
           email,
           password,
           names,
           lastname,
           image,
           phone,
           created_at,
           updated_at,
           is_email_verified,
           s.id notnull,
           university,
           school,
           course,
           a.id notnull,
           is_super,
           admin_since,
           assigned_by
    into strict _user
    from auth.users
    natural left join auth.students s
    natural left join auth.admins a
    where id = _id or email like lower(_email);

    return _user;
exception
    when no_data_found then
        raise exception no_data_found
        using message = coalesce(_id::text, _email) || ': does not exists as a user!',
            hint = 'Register a new account.';
    when too_many_rows then
        raise exception too_many_rows
        using message = 'The user id and email does not belong to one user.',
            hint = 'Perform the function by _id or _email but not both.';
end
$code$;

