package edu.calvin.cs262;

/**
 * A Room class (POJO) for the room relation
 * Represents the room table for CS262dCleaningCrew
 *
 * @author rga6
 */
public class Room {

    private int id, roomNumber, buildingID;

    Room() { /* a default constructor, required by Gson */  }

    Room(int id, int roomNumber, int buildingID) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.buildingID = buildingID;
    }

    public int getId() {
        return id;
    }
    public int getRoomNumber() {
        return roomNumber;
    }
    public int getBuildingID() {
        return buildingID;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
    public void setBuildingID(int buildingID) {
        this.buildingID = buildingID;
    }

}
