INSERT INTO players (name) VALUES ('player1');
INSERT INTO players (name) VALUES ('player2');

INSERT INTO matches (player1,player2,isActive) VALUES (1,2,True);
INSERT INTO matches (player1,player2,isActive,winner) VALUES (1,2,False,1);
INSERT INTO matches (player1,player2,isActive,winner) VALUES (2,1,False,2);
