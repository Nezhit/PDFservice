INSERT INTO roles (id, name) VALUES
    (1, 'ROLE_USER'),
    (2, 'ROLE_MODERATOR'),
    (3, 'ROLE_ADMIN');
-- Вставка данных в таблицу users
INSERT INTO users (id, date, email, password, username)
VALUES ( 5,'2024-01-09 23:54:57.421255', 'vlad.mishikhin@mail.ru', '$2a$10$vIKHxDSXcXQOfPDg1Rx1h.OtEeE8hjfL/EzMsRLav0a6oxjjHhruq', 'user');

-- Вставка данных в таблицу user_roles
INSERT INTO user_roles (user_id, role_id)
VALUES (5, 1);