package edu.calvin.cs262;

/**
 * Created by elc3 on 11/16/2016.
 */
public class Exercise {

    private String Name;
    private int Sets, Reps, Weight;

    Exercise() { }

    Exercise(String Name, int Reps, int Sets, int Weight) {
        this.Name = Name;
        this.Sets = Sets;
        this.Reps = Reps;
        this.Weight = Weight;
    }

    public String getName() { return Name; }

    public int getSets() { return Sets; }

    public int getReps() { return Reps; }

    public int getWeight() { return Weight; }


}
