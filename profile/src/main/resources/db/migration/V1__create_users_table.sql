UPDATE TABLE "users"
(
    id BIGINT PRIMARY KEY,
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL,
	registration_date TIMESTAMP,
	last_login TIMESTAMP

);


