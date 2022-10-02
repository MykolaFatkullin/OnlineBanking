CREATE TABLE IF NOT EXISTS public.user
(
    id         UUID         NOT NULL PRIMARY KEY,
    username   VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL
);
