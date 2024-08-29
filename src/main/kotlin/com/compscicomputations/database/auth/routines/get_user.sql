drop function if exists auth.get_user;
create or replace function auth.get_user(
    _email text,
    _password text
) returns auth.users
    security definer
    language plpgsql
as
$code$
declare _user auth.users;
begin
    select * into strict _user
    from auth.users
    where email like lower(_email);

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
        raise exception 'Your email: % does not exists as a user!', _email
        using hint = 'Register a new account.';

end
$code$;