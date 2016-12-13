package edu.calvin.cs262;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by elc3 on 11/16/2016.
 */
public class Workout {

    private List<Exercise> exercise_list;
    private String workout_name;
    private Boolean completed;
    private Date workout_date;
    private Integer userID, id;

    Workout() { }

    Workout(String Name, Integer userID, Integer id) {
        exercise_list = new ArrayList<>();
        this.workout_name = Name;
        completed = false;
        workout_date = Calendar.getInstance().getTime();
        this.userID = userID;
        this.id = id;
    }

    Workout(String Name, Integer userID) {
        exercise_list = new ArrayList<>();
        this.workout_name = Name;
        completed = false;
        workout_date = Calendar.getInstance().getTime();
        this.userID = userID;
        id = -1;
    }

    public String getWorkout_name() { return workout_name; }

    public void addExercise(Exercise e) { exercise_list.add(e); }

    public Integer getUserID() { return userID; }

    public Integer getID() { return id; }

    public void setID(Integer new_id) { this.id = new_id; }

    public List<Exercise> getExercise_list() { return exercise_list; }

}