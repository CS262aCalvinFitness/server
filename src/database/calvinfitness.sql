-- Drop previous versions of the tables if they they exist, in reverse order of foreign keys.
DROP TABLE IF EXISTS Exercise;
DROP TABLE IF EXISTS Workout;
DROP TABLE IF EXISTS CalvinUser;

-- Create Schema
CREATE TABLE CalvinUser (
	ID integer PRIMARY KEY, 
	username varchar(50)
	);

CREATE TABLE Workout (
	ID integer PRIMARY KEY,
	UserID integer REFERENCES CalvinUser(ID),
	Name varchar(50)
	);

CREATE TABLE Exercise (
	ID integer PRIMARY KEY,
	WorkoutID integer REFERENCES Workout(ID),
	Name varchar(50),
	Sets integer,
	Reps integer,
	Weight integer
	);

-- Grant access to data 
GRANT SELECT ON CalvinUser TO PUBLIC;
GRANT SELECT ON Workout TO PUBLIC;
GRANT SELECT ON Exercise TO PUBLIC;

-- Sample records
INSERT INTO CalvinUser VALUES (1, 'mdk22');
INSERT INTO CalvinUser VALUES (2, 'mitchstark10');
INSERT INTO CalvinUser VALUES (3, 'rlc32');

INSERT INTO Workout VALUES (10, 1, 'Monday Workout');
INSERT INTO Workout VALUES (11, 1, 'Difficult Workout');
INSERT INTO Workout VALUES (12, 2, 'Legs Day');
INSERT INTO Workout VALUES (13, 3, 'Easy Day');

INSERT INTO Exercise VALUES (50, 10, 'Bench Press', 5, 5, 100);
INSERT INTO Exercise VALUES (51, 10, 'Dumbell Squats', 4, 10, 75);
INSERT INTO Exercise VALUES (52, 11, 'Bench Press', 5, 5, 150);
INSERT INTO Exercise VALUES (53, 12, 'Back Squats', 4, 5, 100);
INSERT INTO Exercise VALUES (54, 12, 'Shoulder Press', 6, 10, 60);
INSERT INTO Exercise VALUES (55, 13, 'Bench Press', 5, 5, 80);