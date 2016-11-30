package edu.calvin.cs262;

/**
 * A Task class (POJO) for the Task relation
 * Represents the Task table for CS262dCleaningCrew
 *
 * @author rga6
 */
public class Task {

    private int id, roomID;
    private String description;
    private boolean isComplete;

    Task() { /* a default constructor, required by Gson */  }

    Task(int id, int roomID, String description, boolean isComplete) {
        this.id = id;
        this.description = description;
        this.roomID = roomID;
        this.isComplete = isComplete;
    }

    public int getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
    public int getRoomID() {
        return roomID;
    }
    public boolean getIsComplete() { return isComplete; }

    public void setId(int id) {
        this.id = id;
    }
    public void setTaskNumber(String description) {
        this.description = description;
    }
    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }
    public void setIsComplete(boolean isComplete) { this.isComplete = isComplete; }


}
