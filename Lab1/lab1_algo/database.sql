CREATE TABLE random_numbers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    value INT
);

INSERT INTO random_numbers (value)
SELECT FLOOR(1 + (RAND() * 100))
FROM information_schema.columns
LIMIT 10;

SELECT * FROM random_numbers;