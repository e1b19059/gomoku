CREATE TABLE players (
    id IDENTITY,
    name CHAR NOT NULL
);
CREATE TABLE matches (
    id IDENTITY,
    player1 INT NOT NULL,
    player2 INT NOT NULL,
    isActive boolean NOT NULL,
    winner INT
);