#!/bin/bash

# Environment variables
NAME=CalvinFitness
DIR=/var/cs262/$NAME/src/
PID=$DIR/$USER-$NAME.pid

# Write our PID file
echo $$ > $DIR/$USER-$NAME.pid

# Change to our working directory
cd $DIR

# Run this script to compile/start the cs262 data service.
javac -cp "../lib/*" edu/calvin/cs262/User.java edu/calvin/cs262/Workout.java edu/calvin/cs262/Exercise.java edu/calvin/cs262/FitnessResource.java
java -cp ".:../lib/*" edu.calvin.cs262.FitnessResource
