
-- Drop previous versions of the tables if they they exist, in reverse order of foreign keys.
DROP TABLE IF EXISTS WorkoutAccess;
DROP TABLE IF EXISTS CalvinUser;

-- Create Schema
CREATE TABLE CalvinUser (
	ID integer PRIMARY KEY, 
	login varchar(50),
	password varchar(50)
	);

CREATE TABLE WorkoutAccess(
	OwnerID integer References CalvinUser(ID),
	ViewerID integer REFERENCES CalvinUser(ID),
	Workouts varchar(10000000) 
	);


--Grant access to data 
GRANT SELECT ON CalvinUser TO PUBLIC;
GRANT SELECT ON WorkoutAccess TO PUBLIC;

--Sample Records
INSERT INTO CalvinUser VALUES (1, 'mattkotva', 'samplepassword');
INSERT INTO CalvinUser VALUES (2, 'JohnDoe', 'password2');
INSERT INTO CalvinUser VALUES (3, 'Keith Vander Linden', 'password3');

INSERT INTO WorkoutAccess VALUES (1, 2, 'squat 100 10 lunge 50 7 pushup 0 10');
INSERT INTO WorkoutAccess VALUES(3, 1, 'bench 135 10 shoulder press 8 6');
INSERT INTO WorkoutAccess VALUES(3, 1, 'pulldown 120 10 row 50 8 curl 25 10 fly 25 10' );

--Sample Queries

SELECT WorkoutAccess.Workouts
FROM WorkoutAccess
WHERE OwnerID = 3
AND ViewerID = 1;

--SELECT Workouts
--FROM WorkoutAccess
--Where OwnerID = 1
--And ViewerID = 2;


