package team190.models;

import com.ctre.phoenix.motion.TrajectoryPoint;

/**
 * Created by Kevin O'Brien on 2/6/2018.
 */
public class PairedTrajectoryPoints {
    public TrajectoryPoint[] left;
    public TrajectoryPoint[] right;

    public PairedTrajectoryPoints(TrajectoryPoint[] left, TrajectoryPoint[] right) {
        this.left = left;
        this.right = right;
    }
}
