package edu.calvin.cs262;

/**
 * A Building class (POJO) for the building relation
 * Represents the building table for CS262dCleaningCrew
 *
 * @author rga6
 */
public class Building {

    private int id;
    private String name, shorthand;

    Building() { /* a default constructor, required by Gson */  }

    Building(int id, String name, String shorthand) {
        this.id = id;
        this.name = name;
        this.shorthand = shorthand;
    }

    public int getId() {
        return id;
    }
    public String getShorthand() {
        return shorthand;
    }
    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setShorthand(String shorthand) {
        this.shorthand = shorthand;
    }
    public void setName(String name) {
        this.name = name;
    }

}
