CREATE TABLE IF NOT EXISTS users (
                                    id SERIAL PRIMARY KEY,
                                    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    age INT NOT NULL
    );

-- Добавим несколько тестовых записей
INSERT INTO users (name, email, age) VALUES
                                        ('John Doe', 'john.doe@example.com', 30),
                                        ('Jane Smith', 'jane.smith@example.com', 25),
                                        ('Bob Johnson', 'bob.johnson@example.com', 40);