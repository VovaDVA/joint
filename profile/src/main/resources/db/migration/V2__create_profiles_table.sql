CREATE TABLE "profiles"
(
    id BIGINT PRIMARY KEY,
	user_id BIGINT NOT NULL,
	avatar VARCHAR(255),
	banner VARCHAR(255),
	birthday TIMESTAMP,
	country VARCHAR(255),
	city VARCHAR(255),
	phone VARCHAR(255),
	last_edited TIMESTAMP,
	FOREIGN KEY (user_id) REFERENCES "users" (id)
	ON DELETE CASCADE
    	ON UPDATE CASCADE
);


