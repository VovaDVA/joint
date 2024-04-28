CREATE TABLE "user_verification_codes"
(
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    code VARCHAR(6) NOT NULL,
    request_type VARCHAR(20) NOT NULL CHECK (request_type IN ('password_reset', 'user_deletion')),
    expiration_time TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP + INTERVAL '2 minutes' NOT NULL
);
