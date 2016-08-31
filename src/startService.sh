#!/bin/bash

# Run this script to compile/start the cs262 data service.

javac -cp "../lib/*" edu/calvin/cs262/Player.java src/edu/calvin/cs262/MonopolyResource.java
java -cp ".:../lib/*" edu.calvin.cs262.MonopolyResource
