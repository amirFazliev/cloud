create schema cloud;

CREATE TABLE cloud.users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL,
                       password VARCHAR(50) NOT NULL,
                       authToken VARCHAR(100) NOT NULL
);

create table cloud.files (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             user_id INT,
                             filename VARCHAR(100) NOT NULL,
                             size INT NOT NULL,
                             created_at DATETIME,
                             FOREIGN KEY (user_id) REFERENCES users(id)
);

