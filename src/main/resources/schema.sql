DROP TABLE IF EXISTS status, users, friends, ratings, films, genres,film_genre,film_likes CASCADE;

CREATE TABLE IF NOT EXISTS status
(
    status_id TINYINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name      VARCHAR(20) UNIQUE
);

CREATE TABLE IF NOT EXISTS users
(
    user_id  BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    login    VARCHAR(20) NOT NULL UNIQUE,
    name     VARCHAR(50),
    birthday DATE         NOT NULL
);

CREATE TABLE IF NOT EXISTS friends
(
    user_id BIGINT REFERENCES users(user_id),
    friend_id BIGINT REFERENCES users(user_id),
    status_id TINYINT REFERENCES status(status_id),
    PRIMARY KEY(user_id, friend_id),
    CONSTRAINT friend_id_not_user_id CHECK (user_id <> friends.friend_id)
);

CREATE TABLE IF NOT EXISTS ratings
(
    rating_id INT PRIMARY KEY,
    name VARCHAR(10) UNIQUE
);

CREATE TABLE IF NOT EXISTS films
(
    film_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(200) NOT NULL,
    release_date DATE NOT NULL,
    duration BIGINT NOT NULL,
    rating_id TINYINT REFERENCES ratings(rating_id),
    CONSTRAINT duration_positive CHECK (duration >= 0)
);

CREATE TABLE IF NOT EXISTS genres
(
    genre_id INT PRIMARY KEY,
    name VARCHAR(50) UNIQUE
);

CREATE TABLE IF NOT EXISTS film_genre
(
    film_id BIGINT REFERENCES films(film_id),
    genre_id INT REFERENCES genres(genre_id),
    PRIMARY KEY(film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS film_likes
(
    film_id BIGINT REFERENCES films(film_id),
    user_id BIGINT REFERENCES users(user_id),
    PRIMARY KEY(film_id, user_id)
);
