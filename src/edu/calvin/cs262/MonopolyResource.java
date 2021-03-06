package edu.calvin.cs262;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.server.impl.model.parameter.multivalued.ExtractorContainerException;
import com.sun.net.httpserver.HttpServer;
import sun.rmi.runtime.Log;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import java.io.Console;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.*;

/**
 * This module implements a RESTful service for the cs262dCleaningCrew.
 * This server implements classes for each relation in the database.
 * The server requires Java 1.7 (not 1.8).
 *
 * @author kvlinden, peteroostema
 * @version summer, 2015 - original version
 * @version December, 2016 - altered code to work with cs262 Team D's DB
 */
@Path("/cs262dCleaningCrew/")
public class MonopolyResource {

    /**
     * a hello-world resource
     *
     * @return a simple string value
     */
    @SuppressWarnings("SameReturnValue")
    @GET
    @Path("/hello")
    @Produces("text/plain")
    public String getClichedMessage() {
        return "Hello, Cleaning Crew!";
    }

    /**
     * GET method that returns the tasks for a particular user id
     *
     * @param id an employee id in the CleaningCrew database
     * @return a JSON version of the task record, if any, with the given id
     */
    @GET
    @Path("/task/{id}")
    @Produces("application/json")
    public String getTasks(@PathParam("id") String id) {
        try {
            return new Gson().toJson(retrieveTasks(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * GET method that returns a information about the other users in the 
     *  CleaningCrew database
     *
     * @param id a user id in the CleaningCrew database
     * @return a JSON version of the users in the database minus the current
     *   user.
     */
    @GET
    @Path("/contact/{id}")
    @Produces("application/json")
    public String getContacts(@PathParam("id") String id) {
        try {
            return new Gson().toJson(retrieveContacts(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * GET method that returns a list of all tasks in the system
     *
     * @return a JSON list representation of the task records
     */
    @GET
    @Path("/tasks")
    @Produces("application/json")
    public String getTask() {
        try {
            return new Gson().toJson(retrieveTasks());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    /**
//     * PUT method for creating an instance of Person with a given ID - If the
//     * player already exists, update the fields using the new player field values. We do this
//     * because PUT is idempotent, meaning that running the same PUT several
//     * times is the same as running it exactly once.
//     *
//     * @param id         the ID for the new player, assumed to be unique
//     * @param playerLine a JSON representation of the player; the id parameter overrides any id in this line
//     * @return JSON representation of the updated player, or NULL for errors
//     */
//    @PUT
//    @Path("/player/{id}")
//    @Consumes("application/json")
//    @Produces("application/json")
//    public String putPlayer(@PathParam("id") int id, String playerLine) {
//        try {
//            Player player = new Gson().fromJson(playerLine, Player.class);
//            player.setId(id);
//            return new Gson().toJson(addOrUpdatePlayer(player));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * PUT method for creating an instance of Task with a given ID - If the
     * task already exists, update the fields using the new task fields
     * because PUT is idempotent, meaning that running the same PUT several
     * times is the same as running it exactly once.
     *
     * @param id         the ID for the new task, assumed to be unique
     * @param taskLine a JSON representation of a task; the id paramete$
     * @return JSON representation of the updated taskr, or NULL for errors
     */
    @PUT
    @Path("/task/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public String putTask(@PathParam("id") int id, String taskLine) {
        try {
            Task task = new Gson().fromJson(taskLine, Task.class);
            task.setId(id);
            //System.out.println("put");
            return new Gson().toJson(setTaskComplete(task));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * PUT method for creating an instance of Contact with a given ID - If the
     * contact already exists, update the fields using the new contact field va$
     * because PUT is idempotent, meaning that running the same PUT several
     * times is the same as running it exactly once.
     *
     * @param id         the ID for the new contact, assumed to be unique
     * @param personLine a JSON representation of the contact; the id paramete$
     * @return JSON representation of the updated contact, or NULL for errors
     */
    @PUT
    @Path("/info")
    @Consumes("application/json")
    @Produces("text/plain")
    public String putContactInfo(String personLine) {
        try {
            Person person = new Gson().fromJson(personLine, Person.class);
            //System.out.println("put");
            updatePerson(person);
            //System.out.print(person.toString());
            return "Information for " + person.getPhonenumber() + " Updated";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * PUT method for changing the comment in a task record
     *
     * @param id         the ID for the task to be edited, assumed to be unique
     * @param commentLine a JSON representation of a task; the id paramete$
     * @return JSON representation of the updated task, or NULL for errors
     */
    @PUT
    @Path("/comment")
    @Consumes("application/json")
    @Produces("text/plain")
    public String putComment(String commentLine) {
        try {
            MainTask comment = new Gson().fromJson(commentLine, MainTask.class);
            //System.out.println("put");
            updateComment(comment);
            //System.out.print(person.toString());
            return commentLine.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * POST method for creating an instance of Person with a new, unique ID
     * number. We do this because POST is not idempotent, meaning that running
     * the same POST several times creates multiple objects with unique IDs but
     * otherwise having the same field values.
     * <p>
     * The method creates a new, unique ID by querying the task table for the
     * largest ID and adding 1 to that. Using a DB sequence would be a better solution.
     *
     * @param taskLine a JSON representation of the task (ID ignored)
     * @return a JSON representation of the new task
     */
    @POST
    @Path("/task")
    @Consumes("application/json")
    @Produces("application/json")
    public String postTask(String taskLine) {
        try {
            Task task = new Gson().fromJson(taskLine, Task.class);
            return new Gson().toJson(addNewTask(task));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * DELETE method for deleting and instance of player with the given ID. If
     * the player doesn't exist, then don't delete anything. DELETE is idempotent, so
     * the result of sending the same command multiple times should be the same as
     * sending it exactly once.
     *
     * @param id the ID of the task to be deleted
     * @return null
     */
    @DELETE
    @Path("/task/{id}")
    @Produces("application/json")
    public String deleteTask(@PathParam("id") int id) {
        try {
            Task x = new Task(id, 0, "deleted", false);
            Task y = deleteTask(x);
            return new Gson().toJson(y);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** DBMS Utility Functions *********************************************/

    /**
     * Constants for a local Postgresql server with the monopoly database
     */
    //private static final String DB_URI = "jdbc:postgresql://cs262.cs.calvin.edu:5432/cs262dCleaningCrew";
    private static final String DB_LOGIN_ID = "postgres";
    private static final String DB_PASSWORD = "Listen-Anywhere-6";
    private static final String PORT = "8084";

    private static final String DB_URI = "jdbc:postgresql://localhost:5432/cs262dCleaningCrew";
//    private static final String DB_LOGIN_ID = "postgres";
//    private static final String DB_PASSWORD = "postgres";
    //private static final String PORT = "8084";

    /*
     * Utility method that does the database query, potentially throwing an SQLException,
     * returning  task objects (or null).
     */
    private List<MainTask> retrieveTasks(String id) throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<MainTask> tasks = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT Task.id, description, roomnumber, Building.name, Assignment.Comment, isComplete FROM Task, Assignment, Room, Building WHERE Task.id=Assignment.taskID AND Assignment.personID='" + id + "' AND Task.roomID = Room.id AND Room.buildingID = Building.ID ORDER BY Task.id;");
            while (rs.next()) {
                tasks.add(new MainTask(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getString(5), rs.getBoolean(6)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw (e);
        } finally {
            rs.close();
            statement.close();
            connection.close();
        }
        return tasks;
    }

    /*
     * Utility method that does the database query, potentially throwing an SQLException,
     * returning a task object (or null).
     */
    private List<Person> retrieveContacts(String id) throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<Person> persons = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM Person WHERE NOT Person.id='" + id + "';");
            while (rs.next()) {
                persons.add(new Person(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }
        } catch (SQLException e) {
            throw (e);
        } finally {
            rs.close();
            statement.close();
            connection.close();
        }
        return persons;
    }



    /*
    * Utility method that does the database query, potentially throwing an SQLException,
    * returning a list of name-value map objects (potentially empty).
    */
    private List<Task> retrieveTasks() throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<Task> tasks = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM Task ORDER BY Task.id;");
            while (rs.next()) {
                tasks.add(new Task(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getBoolean(4)));
            }
        } catch (SQLException e) {
            throw (e);
        } finally {
            rs.close();
            statement.close();
            connection.close();
        }
        return tasks;
    }

    private Task setTaskComplete(Task task) {
        Connection connection = null;
        Statement statement = null;
        Boolean new_value = task.getIsComplete();
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
            statement = connection.createStatement();
            statement.executeUpdate("UPDATE Task SET isComplete=" + new_value.toString()
                    + " WHERE Task.id=" + task.getId() + ";");
            task.setIsComplete(new_value);
            return task;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /*
    * Utility method that adds the given task using a new,unique ID, potentially throwing an SQLException,
    * returning the new task
    */
    private Task addNewTask(Task task) throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT MAX(id) FROM Task");
            if (rs.next()) {
                task.setId(rs.getInt(1) + 1);
            } else {
                throw new RuntimeException("failed to find unique ID...");
            }
            statement.executeUpdate("INSERT INTO Task VALUES (" + task.getId() + ", '" + task.getRoomID() + "', '"
                    + task.getDescription() + "', '" + task.getIsComplete() + "')");
        } catch (SQLException e) {
            throw (e);
        } finally {
            rs.close();
            statement.close();
            connection.close();
        }
        return task;
    }

    /*
    * Utility method that does the database update, potentially throwing an SQLException,
    * returning the task, newly deleted
    */
    public Task deleteTask(Task task) throws Exception {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
            statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM Task WHERE id=" + task.getId());
        } catch (SQLException e) {
            throw (e);
        } finally {
            statement.close();
            connection.close();
        }
        return task;
    }

    public Person updatePerson(Person person) throws Exception {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
            statement = connection.createStatement();
            statement.executeUpdate("UPDATE Person SET phonenumber='" + person.getPhonenumber() + "', email='"
                    + person.getEmailaddress() + "' WHERE Person.id='" + person.getId() + "';");
            return person;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

//    private MainTask updateComment(MainTask task) {
//        Connection connection = null;
//        Statement statement = null;
//        String new_comment = task.getComment();
//        try {
//            Class.forName("org.postgresql.Driver");
//            connection = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
//            statement = connection.createStatement();
//            System.out.print("got here");
//            statement.executeUpdate("UPDATE Assignment SET comment='" + new_comment
//                    + "' WHERE Task.id=" + task.getId() + ";");
//            task.setComment(new_comment);
//            return task;
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                statement.close();
//                connection.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
    public void updateComment(MainTask comment) throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        Assignment assignment = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT Assignment.id, taskid, personid, comment, completetime FROM Task, Assignment WHERE Task.id=" + comment.getId() + ";");
            while (rs.next()) {
                assignment = new Assignment(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getTimestamp(5));
            }
            statement.executeUpdate("UPDATE Assignment SET comment='" + comment.getComment()
                    + "' WHERE Assignment.id=" + comment.getId() + ";");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** Main *****************************************************/

    /**
     * Run this main method to fire up the service.
     *
     * @param args command-line arguments (ignored)
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServerFactory.create("http://localhost:" + PORT + "/");
        server.start();

        System.out.println("Server running...");
        System.out.println("Web clients should visit: http://localhost:" + PORT + "/monopoly");
        System.out.println("Android emulators should visit: http://LOCAL_IP_ADDRESS:" + PORT + "/monopoly");
        System.out.println("Hit return to stop...");
        //noinspection ResultOfMethodCallIgnored
        System.in.read();
        System.out.println("Stopping server...");
        server.stop(0);
        System.out.println("Server stopped...");
    }
}
