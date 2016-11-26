package edu.calvin.cs262;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by elc3 on 11/16/2016.
 */
public class Workout {

    private List<Exercise> exercise_list;
    private String workout_name;
    private Boolean completed;

    Workout() { }

    Workout(String Name) {
        exercise_list = new ArrayList<>();
        this.workout_name = Name;
        completed = false;
    }

    public String getWorkout_name() { return workout_name; }

    public void addExercise(Exercise e) { exercise_list.add(e); }


}