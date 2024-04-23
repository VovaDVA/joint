ALTER TABLE profiles DROP CONSTRAINT fk410q61iev7klncmpqfuo85ivh;

ALTER TABLE profiles
    ADD CONSTRAINT fk_user_id
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE;
