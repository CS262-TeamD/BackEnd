package edu.calvin.cs262;

/**
 * A Task class (POJO) for the Task relation
 * Represents the Task table for CS262dCleaningCrew
 *
 * @author rga6
 */
public class MainTask {

    private int id, roomNumber;
    private String description, buildingName;
    private boolean isComplete;

    MainTask() { /* a default constructor, required by Gson */  }

    MainTask(int id, String description, int roomNumber, String buildingName, boolean isComplete) {
        this.id = id;
        this.description = description;
        this.roomNumber = roomNumber;
        this.buildingName = buildingName;
        this.isComplete = isComplete;
    }

    public int getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
    public int getRoomNumber() {
        return roomNumber;
    }
    public String getBuildingName() { return buildingName; }
    public boolean getIsComplete() { return isComplete; }

    public void setId(int id) {
        this.id = id;
    }
    public void setTaskNumber(String description) {
        this.description = description;
    }
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
    public void setBuildingName(String buildingName) { this.buildingName = buildingName; }
    public void setIsComplete(boolean isComplete) { this.isComplete = isComplete; }


}
