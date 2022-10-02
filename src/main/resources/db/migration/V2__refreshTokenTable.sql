CREATE TABLE IF NOT EXISTS refresh_token
(
    id          BIGSERIAL NOT NULL PRIMARY KEY,
    token       UUID      NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    user_id     UUID      NOT NULL,
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES "user" (id)
);