-- =========================
-- CATEGORY
-- =========================
INSERT INTO category (category_id, name, description, status, created_at, updated_at)
VALUES
(1, 'Fiction', 'Fictional books and novels', 'ACTIVE', NOW(), NOW()),
(2, 'Technology', 'Programming and tech books', 'ACTIVE', NOW(), NOW()),
(3, 'Business', 'Business and management books', 'ACTIVE', NOW(), NOW());

-- =========================
-- BOOK
-- =========================
INSERT INTO book (
    book_id, name, author, price, quantity, isbn,
    cover_image, status, created_at, updated_at, category_id
) VALUES
(1, 'Clean Code', 'Robert C. Martin', 25.00, 10, '9780132350884',
 'clean-code.jpg', 'ACTIVE', NOW(), NOW(), 2),

(2, 'Effective Java', 'Joshua Bloch', 30.00, 8, '9780134685991',
 'effective-java.jpg', 'ACTIVE', NOW(), NOW(), 2),

(3, 'The Alchemist', 'Paulo Coelho', 15.00, 20, '9780061122415',
 'alchemist.jpg', 'ACTIVE', NOW(), NOW(), 1);

-- =========================
-- USERS
-- =========================
INSERT INTO users (
    id, username, email, password,
    role, status, created_at, updated_at
) VALUES
(1, 'admin', 'admin@bookstore.com',
   '$2a$10$1LN.AqBzFpABfR9C8sdWcudT/hGS1kO9wiaz/HD0sfE.x0tNkKCeu', -- password is "password123"
 'ADMIN', 'ACTIVE', CURDATE(), CURDATE()),

(2, 'john_doe', 'john@gamil.com',
   '$2a$10$1LN.AqBzFpABfR9C8sdWcudT/hGS1kO9wiaz/HD0sfE.x0tNkKCeu', -- password is "password123"
 'CUSTOMER', 'ACTIVE', CURDATE(), CURDATE());

-- =========================
-- USER PROFILE
-- =========================
INSERT INTO users_profile (
    id, user_id, profile_image, phone, address, date_of_birth,
    created_at, updated_at
) VALUES
(1, 2, 'john.jpg', '12345678', 'Manama, Bahrain',
 '1999-05-15', CURDATE(), CURDATE());

-- =========================
-- ORDERS
-- =========================
INSERT INTO orders (
    id, user_id, order_date, total_price, status
) VALUES
(1, 2, NOW(), 55.00, 'CONFIRMED');

-- =========================
-- ORDER ITEMS
-- =========================
INSERT INTO order_items (
    id, order_id, book_id, quantity, unit_price, subtotal,
    created_at, updated_at
) VALUES
(1, 1, 1, 1, 25.00, 25.00, NOW(), NOW()),
(2, 1, 2, 1, 30.00, 30.00, NOW(), NOW());
