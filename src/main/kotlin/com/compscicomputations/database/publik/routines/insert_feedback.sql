drop procedure if exists public.insert_feedback;
create or replace procedure public.insert_feedback(
    _subject text,
    _message text,
    _suggestion text default '',
    _image bytea default null,
    _user_email text default null
)
    language plpgsql
as
$$
begin
    insert into public.feedbacks(subject, message, suggestion, image, user_email)
    values (_subject, _message, _suggestion, _image, _user_email);

exception
    when foreign_key_violation then
        insert into public.feedbacks(subject, message, suggestion, image)
        values (_subject, _message, _suggestion, _image);
end
$$;