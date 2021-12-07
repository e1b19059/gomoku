CREATE TABLE players (
    id IDENTITY,
    name CHAR NOT NULL,
    turn boolean
);
CREATE TABLE matches (
    id IDENTITY,
    player1 INT NOT NULL,
    player2 INT NOT NULL,
    isActive boolean NOT NULL,
    winner INT
);

CREATE TABLE matchinfo (
    id IDENTITY,
    player1 INT NOT NULL,
    player2 INT NOT NULL,
    palyer2Active boolean NOT NULL,
);
