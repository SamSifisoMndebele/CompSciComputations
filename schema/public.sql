CREATE TABLE features (
    id SMALLINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    module_name TEXT NOT NULL UNIQUE,
    title TEXT NOT NULL UNIQUE,
    class_name TEXT,
    method_name TEXT
);

CREATE TABLE feedbacks (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    title TEXT NOT NULL,
    content TEXT NOT NULL,
    image_url TEXT,
    uid VARCHAR(28) NOT NULL
);

