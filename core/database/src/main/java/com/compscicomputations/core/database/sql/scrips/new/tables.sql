
CREATE TABLE features (
    id SMALLINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    module_name TEXT NOT NULL UNIQUE,
    title TEXT NOT NULL UNIQUE,
    class_name TEXT,
    method_name TEXT
);

CREATE TABLE users_features (
    uid VARCHAR(28) NOT NULL,
    feature_id INT NOT NULL,
    PRIMARY KEY (uid,feature_id)
);

CREATE TABLE feedbacks (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    title TEXT NOT NULL,
    content TEXT NOT NULL,
    image_url TEXT,
    uid VARCHAR(28) NOT NULL
);

CREATE TABLE histories (
    id BIGINT NOT NULL,
    uid VARCHAR(28) NOT NULL,
    feature_id SMALLINT NOT NULL,
    data JSONB NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timestampnow(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT timestampnow(),
    PRIMARY KEY(id, uid)
);


ALTER TABLE users_features ADD CONSTRAINT users_features_uid_users_uid
    FOREIGN KEY (uid) REFERENCES users(uid) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE users_features ADD CONSTRAINT users_features_feature_id_features_id
    FOREIGN KEY (feature_id) REFERENCES features(id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE students ADD CONSTRAINT students_uid_users_uid
    FOREIGN KEY (uid) REFERENCES users(uid) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE admins ADD CONSTRAINT admins_uid_users_uid
    FOREIGN KEY (uid) REFERENCES users(uid) ON DELETE CASCADE ON UPDATE CASCADE;
-- ALTER TABLE admins ADD CONSTRAINT admins_code_id_admins_codes_id
--     FOREIGN KEY (code_id) REFERENCES admins_codes(id) ON DELETE RESTRICT ON UPDATE CASCADE;
-- ALTER TABLE users_metadata ADD CONSTRAINT users_metadata_uid_users_uid
--     FOREIGN KEY (uid) REFERENCES users(uid) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE feedbacks ADD CONSTRAINT feedbacks_uid_users_uid
    FOREIGN KEY (uid) REFERENCES users(uid) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE histories ADD CONSTRAINT histories_feature_id_features_id
    FOREIGN KEY (feature_id) REFERENCES features(id) ON DELETE NO ACTION ON UPDATE NO ACTION;