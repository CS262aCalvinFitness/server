package edu.calvin.cs262;

import com.google.gson.Gson;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@Path("/fitness")
public class FitnessResource {

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
        return "Hello, Jersey!";
    }

    /**
     * GET method that returns a list of all calvinfitness users
     *
     * @return a JSON list representation of the user records
     */
    @GET
    @Path("/users")
    @Produces("application/json")
    public String getUsers() {
        try {
            return new Gson().toJson(retrieveUsers());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * GET method that returns all the workouts shared with the given ID
     *
     * @return a JSON list representation of the Shared Workouts
     *
     */
    @GET
    @Path("/sharedworkouts/{id}")
    @Produces("application/json")
    public String getSharedWorkouts(@PathParam("id") int id) {
        try {
            return new Gson().toJson(retrieveSharedWorkouts(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Utility Methods *****************************************************/

    /* Constants for a local Postgresql server with the calvinfitness database */
    private static final String DB_URI = "jdbc:postgresql://localhost:5432/calvinfitness";
    private static final String DB_LOGIN_ID = "postgres";
    private static final String DB_PASSWORD = "postgres";
    private static final String PORT = "8081";

    /*
    * Utility method that does the database query, potentially throwing an SQLException,
    * returning a list of name-value map objects (potentially empty).
    */
    private List<String> retrieveUsers() throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();
        List<String> usernames = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM CalvinUser");
            while (rs.next()) {
                users.add(new User(rs.getInt(1), rs.getString(2)));
            }
            for (User object: users) {
                usernames.add(object.getUsername());
            }
        } catch (SQLException e) {
            throw (e);
        } finally {
            rs.close();
            statement.close();
            connection.close();
        }
        return usernames;
    }

    /*
     * Utility method that does the database query, potentially throwing an SQLException,
     * returning list of workout objects (or null).
     */
    private Workout retrieveSharedWorkouts(int id) throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<Exercise> exercises = new ArrayList<>();
        Workout workout = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = statement.executeQuery("SELECT * FROM CalvinUser, Workout, Exercise, SharedWorkout WHERE SharedWorkout.ViewerID=" + id + " AND SharedWorkout.WorkoutID = Workout.ID AND Exercise.WorkoutID = Workout.ID AND CalvinUser.ID=" + id);
            if (rs.next()) {
                workout = new Workout(rs.getString(5));
            }
            rs.beforeFirst();
            while (rs.next()) {
                exercises.add(new Exercise(rs.getString(8), rs.getInt(10), rs.getInt(9), rs.getInt(11)));
            }
            for (Exercise exercise: exercises) {
                workout.addExercise(exercise);
            }
        } catch (SQLException e) {
            throw (e);
        } finally {
            rs.close();
            statement.close();
            connection.close();
        }
        return workout;
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
        System.out.println("Web clients should visit: http://localhost:" + PORT + "/fitness");
        System.out.println("Android emulators should visit: http://LOCAL_IP_ADDRESS:" + PORT + "/fitness");
        System.out.println("Hit return to stop...");
        //noinspection ResultOfMethodCallIgnored
        System.in.read();
        System.out.println("Stopping server...");
        server.stop(0);
        System.out.println("Server stopped...");
    }
}