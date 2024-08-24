drop procedure if exists public.insert_feedback;
create or replace procedure public.insert_feedback(
    _subject text,
    _message text,
    _suggestion text default null,
    _image bytea default null,
    _user_id uuid default null
)
    language plpgsql
as
$$
begin
    insert into public.feedbacks(subject, message, suggestion, image, user_id)
    values (_subject, _message, _suggestion, _image, _user_id);

exception
    when foreign_key_violation or numeric_value_out_of_range then
        insert into public.feedbacks(subject, message, suggestion, image)
        values (_subject, _message, _suggestion, _image);
end;
$$