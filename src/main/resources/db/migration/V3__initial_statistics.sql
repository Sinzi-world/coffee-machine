CREATE TABLE drink_statistics (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    drink_id INT NOT NULL UNIQUE REFERENCES drinks(id) ON DELETE CASCADE,
    drink_name TEXT NOT NULL UNIQUE,
    order_count INT NOT NULL DEFAULT 0
);
