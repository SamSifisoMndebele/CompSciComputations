drop procedure if exists public.insert_feedback;
create or replace procedure public.insert_feedback(
    _subject text,
    _message text,
    _suggestion text default null,
    _image_bytes bytea default null,
    _user_id int default null
)
    language plpgsql
as
$$
begin
    insert into public.feedbacks(subject, message, suggestion, image_bytes, user_id)
    values (_subject, _message, _suggestion, _image_bytes, _user_id);

exception
    when foreign_key_violation or numeric_value_out_of_range then
        insert into public.feedbacks(subject, message, suggestion, image_bytes)
        values (_subject, _message, _suggestion, _image_bytes);
end;
$$