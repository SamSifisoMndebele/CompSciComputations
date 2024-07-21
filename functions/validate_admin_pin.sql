drop function if exists auth.validate_admin_pin;
create or replace function auth.validate_admin_pin(
    _email text,
    _pin text
) returns int
    security definer
    language plpgsql
as
$code$
declare
    _admin_pin auth.admins_pins;
begin
    if ext.isNullOrBlank(_email) or ext.isNullOrBlank(_pin) then
        raise exception 'Invalid Parameters, email and pin should not be null or empty';
    end if;

    select * into _admin_pin
    from auth.admins_pins
    where email like _email;

    if _admin_pin is null then
        raise exception 'The user with email: % is not recognized as admin!', _email;
    elsif _admin_pin.pin_hash <> ext.crypt(_pin, _admin_pin.pin_hash) then
        raise exception 'The admin secrete code: % is not valid!', _pin;
    else
        return _admin_pin.id;
    end if;
end
$code$;