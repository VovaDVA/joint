CREATE TABLE "verification_codes"
(
    id BIGINT PRIMARY KEY,
    user_id BIGINT,
    code VARCHAR(6),
    expiration_time TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP + INTERVAL '2 minutes'
);
