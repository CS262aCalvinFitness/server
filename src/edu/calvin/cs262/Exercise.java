package edu.calvin.cs262;

/**
 * Created by elc3 on 11/16/2016.
 */
public class Exercise {

    private String name;
    private int sets, reps, weights, ID, workout_ID;

    Exercise() { }

    Exercise(String Name, int Reps, int Sets, int Weight, int ID, int workout_ID) {
        this.name = Name;
        this.sets = Sets;
        this.reps = Reps;
        this.weights = Weight;
        this.ID = ID;
        this.workout_ID = workout_ID;
    }

    public String getName() { return name; }

    public int getSets() { return sets; }

    public int getReps() { return reps; }

    public int getWeight() { return weights; }

    public void setID(Integer new_id) { this.ID = new_id; }

    public int getID() { return ID; }

    public int getWorkoutID() { return workout_ID; }

}
