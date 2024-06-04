
CREATE TABLE "user" (
    "uid" VARCHAR(28) PRIMARY KEY,
    "display_name" TEXT NOT NULL CHECK ( LENGTH(display_name) > 1 ),
    "email" TEXT UNIQUE NOT NULL,
    "phone" TEXT UNIQUE,
    "photo_url" TEXT,
    "user_type" USERTYPE NOT NULL
);