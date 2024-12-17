--DROP TABLE users CASCADE;
--DROP TABLE items CASCADE;
--DROP TABLE bookings CASCADE;
--DROP TABLE requests CASCADE;
--DROP TABLE comments CASCADE;
CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(50) NOT NULL,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    owner INTEGER NOT NULL,
    description VARCHAR(500) NOT NULL,
    available BOOLEAN NOT NULL,
    request INTEGER,
    CONSTRAINT fk_owner_id FOREIGN KEY (owner) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    item INTEGER NOT NULL,
    requester INTEGER NOT NULL,
    owner INTEGER NOT NULL,
    CONSTRAINT fk_item_id FOREIGN KEY (item) REFERENCES items (id),
    CONSTRAINT fk_requester_id FOREIGN KEY (requester) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    item INTEGER NOT NULL,
    booker INTEGER NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_item_id2 FOREIGN KEY (item) REFERENCES items (id),
    CONSTRAINT fk_booker_id FOREIGN KEY (booker) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    item_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    text VARCHAR(200) NOT NULL,
    created TIMESTAMP NOT NULL,
    CONSTRAINT fk_item_id3 FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id)
)