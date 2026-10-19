-- Update users table to set password manually (temp workaround - not recommended!)
UPDATE users
SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
WHERE username IN ('user1', 'user2');

-- Existing seed books are owned by the seeded administrator.
ALTER TABLE books ADD COLUMN owner_id BIGINT;
UPDATE books SET owner_id = (SELECT user_id FROM users WHERE username = 'user2');

ALTER TABLE books ALTER COLUMN owner_id SET NOT NULL;
ALTER TABLE books ADD CONSTRAINT fk_books_owner FOREIGN KEY (owner_id) REFERENCES users(user_id);
ALTER TABLE users ADD CONSTRAINT uk_users_username UNIQUE (username);