CREATE TABLE users_features (
    uid VARCHAR(28) NOT NULL,
    feature_id INT NOT NULL,
    PRIMARY KEY (uid,feature_id)
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
