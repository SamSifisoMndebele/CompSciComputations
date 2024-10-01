drop function if exists public.get_my_feedbacks;
create or replace function public.get_my_feedbacks(
    _email text
) returns setof public.feedbacks
    language plpgsql
as
$$
begin
    return query select * from public.feedbacks
    where user_email = _email;
end
$$;

drop function if exists public.get_responded_feedbacks;
create or replace function public.get_responded_feedbacks(
    _email text default null
)
    returns setof public.feedbacks
    language plpgsql
as
$$
begin
     if _email isnull then
         return query select * from public.feedbacks where response_message notnull;
     else
         return query select * from public.feedbacks where response_message notnull and user_email = _email;
     end if;
end
$$;
