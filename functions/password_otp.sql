drop function if exists auth.create_password_otp;
drop function if exists auth.validate_password_otp;

create or replace function auth.create_password_otp(
    _email text
) returns boolean
    security definer
    language plpgsql
as
$code$
declare
    _pass_otp auth.password_otp := null;
begin
    if ext.isNullOrBlank(_email) then
        raise exception 'Invalid Parameters, email should not be null or empty';
    end if;

    insert into auth.password_otp (email, otp_hash)
    values (_email, ext.gen_random_otp());

--     Get latest OTP
    select * into _pass_otp
    from auth.password_otp
    where email like _email
    order by created_at desc
    limit 1;

--     Validate OTP
    if _pass_otp is null or _pass_otp.otp_hash <> ext.crypt(_otp, _pass_otp.otp_hash) then
        delete from auth.password_otp where email = _email;
        raise exception 'The OTP: % is not valid!', _otp using hint = 'Request a new OTP.';
    elseif _pass_otp.valid_until > now() then
        delete from auth.password_otp where email = _email;
        raise exception 'The OTP: % has expired!', _otp using hint = 'Request a new OTP.';
    end if;

    return true;
end
$code$;


create or replace function auth.validate_password_otp(
    _email text,
    _otp text
) returns boolean
    security definer
    language plpgsql
as
$code$
declare
    _pass_otp auth.password_otp := null;
begin
    if ext.isNullOrBlank(_email) or ext.isNullOrBlank(_otp) then
        raise exception 'Invalid Parameters, email and otp should not be null or empty';
    end if;

--     Get latest OTP
    select * into _pass_otp
    from auth.password_otp
    where email like _email
    order by created_at desc
    limit 1;

--     Validate OTP
    if _pass_otp is null or _pass_otp.otp_hash <> ext.crypt(_otp, _pass_otp.otp_hash) then
        delete from auth.password_otp where email = _email;
        raise exception 'The OTP: % is not valid!', _otp using hint = 'Request a new OTP.';
    elseif _pass_otp.valid_until > now() then
        delete from auth.password_otp where email = _email;
        raise exception 'The OTP: % has expired!', _otp using hint = 'Request a new OTP.';
    end if;

    return true;
end
$code$;