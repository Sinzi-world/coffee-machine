INSERT INTO ingredients (name, quantity)
VALUES
('coffee', 500),
('water', 1000),
('milk', 1000);


INSERT INTO drinks (name)
VALUES
('Espresso'),
('Americano'),
('Cappuccino');


INSERT INTO drink_ingredients (drink_id, ingredient_id, amount)
VALUES
(1, 1, 7),
(1, 2, 30),

(2, 1, 5),
(2, 2, 130),

(3, 1, 20),
(3, 2, 60),
(3, 3, 100);