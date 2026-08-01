CREATE TABLE products (
    id          UUID         PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    price       NUMERIC(10,2) NOT NULL,
    currency    VARCHAR(3)   NOT NULL,
    status      VARCHAR(50)  NOT NULL
);
