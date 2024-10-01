drop function if exists public.insert_feedback;
create or replace function public.insert_feedback(
    _subject text,
    _message text,
    _suggestion text default '',
    _image bytea default null,
    _user_email text default null
)
    returns int
    language plpgsql
as
$$
declare _id int;
begin
    insert into public.feedbacks(subject, message, suggestion, image, user_email)
    values (_subject, _message, _suggestion, _image, _user_email)
    returning id into strict _id;

    return _id;
exception
    when foreign_key_violation then
        insert into public.feedbacks(subject, message, suggestion, image)
        values (_subject, _message, _suggestion, _image);
end
$$;