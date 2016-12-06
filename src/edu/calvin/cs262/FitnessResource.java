package edu.calvin.cs262;

import com.google.gson.Gson;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;

import javax.ws.rs.*;
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
    @Path("/sharedworkouts")
    @Produces("application/json")
    public String getSharedWorkouts() {
        try {
            return new Gson().toJson(retrieveSharedWorkouts());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * POST method for creating an instance of Shared Workout with a new, unique ID
     * number. We do this because POST is not idempotent, meaning that running
     * the same POST several times creates multiple objects with unique IDs but
     * otherwise having the same field values.
     *
     * The method creates a new, unique ID by querying the workout table for the
     * largest ID and adding 1 to that. Using a DB sequence would be a better solution.
     *
     * @param workoutLine a JSON representation of the player (ID ignored)
     * @return a JSON representation of the new player
     */
    @POST
    @Path("/sharedworkouts")
    @Consumes("application/json")
    @Produces("application/json")
    public String postSharedWorkout(String workoutLine) {
        try {
            Workout workout = new Gson().fromJson(workoutLine, Workout.class);
            return new Gson().toJson(addNewWorkout(workout));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Utility Methods *****************************************************/

    /* Constants for a local Postgresql server with the calvinfitness database */
    private static final String DB_URI = "jdbc:postgresql://localhost:5432/calvinfitness";
    private static final String DB_LOGIN_ID = "postgres";
    private static final String DB_PASSWORD = "Listen-Anywhere-6";
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
    private List<Workout> retrieveSharedWorkouts() throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<Exercise> exercises = new ArrayList<>();
        List<Workout> workout_list = new ArrayList<>();
        List<String> workout_names = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = statement.executeQuery("SELECT * FROM CalvinUser, Workout, Exercise WHERE CalvinUser.ID = Workout.UserID AND Exercise.WorkoutID = Workout.ID");
            while (rs.next()) {
                String temp_name = rs.getString(5);
                Integer temp_ID = rs.getInt(1);
                Integer temp_UserID = rs.getInt(3);
                if (!workout_names.contains(temp_name)) {
                    workout_names.add(temp_name);
                    workout_list.add(new Workout(temp_name, temp_ID, temp_UserID));
                }
            }
            rs.beforeFirst();
            while (rs.next()) {
                for (Workout workout: workout_list) {
                    if (workout.getWorkout_name().equals(rs.getString(5))) {
                        workout.addExercise(new Exercise(rs.getString(8), rs.getInt(10), rs.getInt(9), rs.getInt(11)));
                    }
                }
            }
        } catch (SQLException e) {
            throw (e);
        } finally {
            rs.close();
            statement.close();
            connection.close();
        }
        return workout_list;
    }

    /*
     * Utility method that adds the given shared workout using a new,unique ID, potentially throwing an SQLException,
     * returning the new shared workout
     */
    private Workout addNewWorkout(Workout workout) throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT MAX(ID) FROM Workout");
            if (rs.next()) {
                    workout.setID(rs.getInt(1));
            } else {
                throw new RuntimeException("Failed to find unique ID...");
            }
            statement.executeUpdate("INSERT INTO Workout VALUES (" + workout.getID() + ", " + workout.getUserID() + ", '" + workout.getWorkout_name() + "')");
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