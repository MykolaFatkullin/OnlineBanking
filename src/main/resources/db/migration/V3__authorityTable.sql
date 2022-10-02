CREATE TABLE IF NOT EXISTS authority
(
    id        BIGSERIAL   NOT NULL PRIMARY KEY,
    authority varchar(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_authority
(
    user_id      UUID      NOT NULL,
    authority_id BIGSERIAL NOT NULL,
    PRIMARY KEY (user_id, authority_id),
    CONSTRAINT fk_user_authority_user FOREIGN KEY (user_id) REFERENCES "user" (id),
    CONSTRAINT fk_user_authority_authority FOREIGN KEY (authority_id) REFERENCES authority (id)
);

INSERT INTO authority (id, authority)
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN')

