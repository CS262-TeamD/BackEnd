#!/bin/bash

# source the env.sh file
source env.sh

# Write our PID file
echo $$ > $DIR/$USER-$NAME.pid
echo `which java`
echo `which javac`

# Change to our working directory
cd $DIR

# Run this script to compile/start the cs262 data service.
javac -cp "../lib/*" edu/calvin/cs262/Player.java edu/calvin/cs262/MonopolyResource.java edu/calvin/cs262/Assignment.java edu/calvin/cs262/Building.java edu/calvin/cs262/Person.java edu/calvin/cs262/Room.java edu/calvin/cs262/Task.java
java -cp ".:../lib/*" edu.calvin.cs262.MonopolyResource
