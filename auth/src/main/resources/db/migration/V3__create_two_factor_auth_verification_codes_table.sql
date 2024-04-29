CREATE TABLE "two_factor_auth_verification_codes"
(
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    code VARCHAR(6) NOT NULL,
    expiration_time TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP + INTERVAL '2 minutes'
);
