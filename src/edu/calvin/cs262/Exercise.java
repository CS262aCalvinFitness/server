package edu.calvin.cs262;

/**
 * Created by elc3 on 11/16/2016.
 */
public class Exercise {

    private String name;
    private int sets, reps, weights;

    Exercise() { }

    Exercise(String Name, int Reps, int Sets, int Weight) {
        this.name = Name;
        this.sets = Sets;
        this.reps = Reps;
        this.weights = Weight;
    }

    public String getName() { return name; }

    public int getSets() { return sets; }

    public int getReps() { return reps; }

    public int getWeight() { return weights; }


}
