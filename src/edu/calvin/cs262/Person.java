package edu.calvin.cs262;

/**
 * A Person class (POJO) for the person relation
 * Represents the person table for CS262dCleaningCrew
 *
 * @author rga6
 */
public class Person {

    private int id;
    private String name, email, phonenumber, role;

    Person() { /* a default constructor, required by Gson */  }

    Person(int id, String emailaddress, String name, String phonenumber,
           String role) {
        this.id = id;
        this.emailaddress = emailaddress;
        this.name = name;
        this.phonenumber = phonenumber;
        this.role = role;
    }

    public int getId() {
        return id;
    }
    public String getEmailaddress() {
        return emailaddress;
    }
    public String getName() {
        return name;
    }
    public String getPhonenumber() { return phonenumber; }
    public String getRole() { return role; }

    public void setId(int id) {
        this.id = id;
    }
    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPhonenumber(String phonenumber) { this.phonenumber = phonenumber; }
    public void setRole(String role) { this.role = role; }

}
