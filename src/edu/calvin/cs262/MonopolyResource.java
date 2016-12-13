package edu.calvin.cs262;

import com.google.gson.Gson;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * This module implements a RESTful service for the player table of the monopoly database.
 * Only the player relation is supported, not the game or playergame objects.
 * The server requires Java 1.7 (not 1.8).
 *
 * I tested these services using IDEA's REST Client test tool. Run the server and open
 * Tools-TestRESTService and set the appropriate HTTP method, host/port, path and request body and then press
 * the green arrow (submit request).
 *
 * See the readme.txt for instructions on how to deploy this application as a webservice.
 *
 * @author kvlinden
 * @version summer, 2015 - original version
 * @version summer, 2016 - upgraded to GSON/JSON; added Player POJO; removed unneeded libraries
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
     * GET method that returns a particular monopoly player based on ID
     *
     * @param id a player id in the monopoly database
     * @return a JSON version of the player record, if any, with the given id
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
     * GET method that returns a particular monopoly player based on ID
     *
     * @param id a player id in the monopoly database
     * @return a JSON version of the player record, if any, with the given id
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
     * GET method that returns a list of all monopoly players
     *
     * @return a JSON list representation of the player records
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
     * POST method for creating an instance of Person with a new, unique ID
     * number. We do this because POST is not idempotent, meaning that running
     * the same POST several times creates multiple objects with unique IDs but
     * otherwise having the same field values.
     * <p>
     * The method creates a new, unique ID by querying the player table for the
     * largest ID and adding 1 to that. Using a DB sequence would be a better solution.
     *
     * @param taskLine a JSON representation of the player (ID ignored)
     * @return a JSON representation of the new player
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
     * @param id the ID of the player to be deleted
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
    //private static final String DB_LOGIN_ID = "postgres";
    //private static final String DB_PASSWORD = "postgres";
    //private static final String PORT = "8084";

    /*
     * Utility method that does the database query, potentially throwing an SQLException,
     * returning a player object (or null).
     */
    private List<Task> retrieveTasks(String id) throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<Task> tasks = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
            statement = connection.createStatement();
            rs = statement.executeQuery(SELECT Task.id, description, isComplete, roomnumber, Building.name FROM Task, Assignment, Room, Building WHERE Task.id=Assignment.taskID AND Assignment.personID='" + id + "' AND Task.roomID = Room.id AND Room.buildingID = Building.ID ORDER BY Task.id;");
            while (rs.next()) {
                tasks.add(new Task(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getBoolean(4)));
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
     * returning a player object (or null).
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
    * Utility method that adds the given player using a new,unique ID, potentially throwing an SQLException,
    * returning the new player
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
    * returning the player, potentially new.
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
