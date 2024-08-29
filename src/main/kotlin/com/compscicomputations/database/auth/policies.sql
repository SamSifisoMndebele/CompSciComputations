ALTER TABLE auth.users ENABLE ROW LEVEL SECURITY;

CREATE POLICY user_sel_policy ON auth.users
    FOR SELECT
    USING (true);
CREATE POLICY user_mod_policy ON auth.users
    USING (id = current_user);



create policy "Enable insert for authenticated users only"
    on auth.users
    for insert to authenticated
    with check (true);

create policy "Enable delete for users based on user_id"
    on auth.users
    for delete using ((select auth.id()) = id);