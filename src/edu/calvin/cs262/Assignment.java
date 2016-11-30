package edu.calvin.cs262;

import java.util.Date;
import java.sql.Timestamp;

/**
 * A Assignment class (POJO) for the assignment relation
 * Represents the assignment table for CS262dCleaningCrew
 *
 * @author rga6
 */
public class Assignment {

    private int id, taskID;
    private String personID, comment;
    private Timestamp completeTime;

    Assignment() { /* a default constructor, required by Gson */  }

    Assignment(int id, int taskID, String personID, String comment, Timestamp completeTime) {
        this.id = id;
        this.taskID = taskID;
        this.personID = personID;
        this.comment = comment;
        this.completeTime = completeTime;
    }

    public int getId() {
        return id;
    }
    public int getTaskID() {
        return taskID;
    }
    public String getPersonID() {
        return personID;
    }
    public String getComment() { return comment; }
    public Timestamp getCompleteTime() { return completeTime; }

    public void setId(int id) {
        this.id = id;
    }
    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }
    public void setPersonID(String personID) {
        this.personID = personID;
    }
    public void setComment(String comment) { this.comment = comment; }
    public void setCompleteTime(Timestamp completeTime) { this.completeTime = completeTime; }

}
