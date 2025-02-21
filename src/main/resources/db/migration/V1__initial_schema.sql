CREATE TABLE ingredients (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    quantity INT NOT NULL CHECK (quantity >= 0)
);

CREATE TABLE drinks (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE drink_ingredients (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    drink_id INT NOT NULL REFERENCES drinks(id) ON DELETE CASCADE,
    ingredient_id INT NOT NULL REFERENCES ingredients(id) ON DELETE CASCADE,
    amount INT NOT NULL CHECK (amount > 0),
    UNIQUE (drink_id, ingredient_id)
);

CREATE TABLE orders (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    drink_id INT NOT NULL REFERENCES drinks(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE statistics (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    drink_id INT NOT NULL REFERENCES drinks(id) ON DELETE CASCADE,
    order_count INT NOT NULL DEFAULT 0,
    UNIQUE (drink_id)
);
