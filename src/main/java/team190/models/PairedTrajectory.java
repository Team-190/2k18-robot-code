package team190.models;

import jaci.pathfinder.Trajectory;

/**
 * Created by Kevin O'Brien on 2/6/2018.
 */
public class PairedTrajectory {
    private Trajectory left;
    private Trajectory right;

    public PairedTrajectory(Trajectory left, Trajectory right) {
        this.left = left;
        this.right = right;
    }

    public Trajectory getLeft() {
        return left;
    }

    public void setLeft(Trajectory left) {
        this.left = left;
    }

    public Trajectory getRight() {
        return right;
    }

    public void setRight(Trajectory right) {
        this.right = right;
    }
}
