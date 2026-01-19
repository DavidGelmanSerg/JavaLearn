--liquibase formatted sql

--changeset developer:David
CREATE TABLE Users(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(20) NOT NULL,
    deleted BOOLEAN,
    online BOOLEAN
);
CREATE TABLE Chats(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    creatorId INT NOT NULL,
    deleted BOOLEAN,
    FOREIGN KEY(creatorId) REFERENCES Users (id)
);
CREATE TABLE Users_Chats(
    userId INT NOT NULL,
    chatId INT NOT NULL,
    PRIMARY KEY(userId, chatId),
    FOREIGN KEY (userId) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (chatId) REFERENCES Chats(id) ON DELETE CASCADE
);
CREATE TABLE Messages(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    content VARCHAR(1000) NOT NULL,
    creationDateTime TIMESTAMP NOT NULL,
    creatorId INT,
    chatId INT NOT NULL,
    deleted BOOLEAN,
    FOREIGN KEY (creatorId) REFERENCES Users(id) ON DELETE SET NULL,
    FOREIGN KEY (chatId) REFERENCES Chats(id) ON DELETE CASCADE
);
CREATE TABLE Sessions(
    sessionId VARCHAR(36) NOT NULL,
    userId INT NOT NULL,
    expiredDate TIMESTAMP NOT NULL,
    PRIMARY KEY(sessionId),
    FOREIGN KEY (userId) REFERENCES Users(id) ON DELETE CASCADE
);



