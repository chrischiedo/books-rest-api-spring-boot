-- Insert into books table
INSERT INTO books(title, author, description, isbn)
    VALUES ('Things Fall Apart', 'Chinua Achebe', 'A story of colonial Nigeria', '178-2-16');
INSERT INTO books(title, author, description, isbn)
    VALUES ('The River And The Source', 'Margaret Ogolla', 'A story about the girl child in an African setting', '211-3-17');
INSERT INTO books(title, author, description, isbn)
    VALUES ('Dreams From My Father', 'Barack Obama', 'A memoir of the former US President', '312-4-18');
INSERT INTO books(title, author, description, isbn)
    VALUES ('Animal Farm', 'George Orwell', 'A political satire', '422-5-19');

-- Insert into users table
INSERT INTO users(username, password, authority)
    VALUES ('user1', 'pass1', 'ROLE_USER');
INSERT INTO users(username, password, authority)
    VALUES ('user2', 'pass2', 'ROLE_ADMIN');