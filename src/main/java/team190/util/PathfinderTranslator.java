package team190.util;

import com.ctre.phoenix.motion.TrajectoryPoint;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;
import team190.models.AutoSequence;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Kevin O'Brien on 1/25/2018.
 */
public class PathfinderTranslator {

    private double WHEELDIAMETER_FT = 4.0 / 12.0; // 4 inch diameter wheels
    private double WHEELCIRCUMFERENCE_FT = 3.14 * WHEELDIAMETER_FT;
    private double REV_PER_FT = 1.0 / WHEELCIRCUMFERENCE_FT;
    private double TICKS_PER_REV = 1024.0 * 3.0 * (50.0/34.0); // Encoder PPR: 256 (*4 for quadrature), Vex: "Encoder output spins at 3x the speed of the output shaft", "then a 50:34 reduction"
    private double TICKS_PER_FT = TICKS_PER_REV * REV_PER_FT;
    private double HUNDRED_MS_PER_SEC = 10.0;
    private Trajectory leftTraj;
    private Trajectory rightTraj;
    private TrajectoryPoint trajPoint = new TrajectoryPoint();
    private int pidfSlot;

    /**
     * Generate a Pathfinder Trajectory based upon supplied Waypoints
     *
     * @param points   Array of Pathfinder Waypoints
     * @param pidfSlot The pidfSlot on the Talon SRX that should be used for motion profiling
     */
    public PathfinderTranslator(Waypoint points[], int pidfSlot) {
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC,
                Trajectory.Config.SAMPLES_HIGH, 0.01, 5, 10, 60);
        Trajectory trajectory = Pathfinder.generate(points, config);

        TankModifier mod = new TankModifier(trajectory);
        mod.modify(2.15); //wheelbase

        leftTraj = mod.getLeftTrajectory();
        rightTraj = mod.getRightTrajectory();

        this.pidfSlot = pidfSlot;
    }

    /**
     * @param leftCSV  file path for Pathfinder csv left profile
     * @param rightCSV file path for Pathfinder csv right profile
     * @param pidfSlot The pidfSlot on the Talon SRX that should be used for motion profiling
     */
    private PathfinderTranslator(String leftCSV, String rightCSV, int pidfSlot) {
        File left = new File(leftCSV);
        File right = new File(rightCSV);
        leftTraj = Pathfinder.readFromCSV(left);
        rightTraj = Pathfinder.readFromCSV(right);

        this.pidfSlot = pidfSlot;
    }

    public PathfinderTranslator(AutoSequence path, int pidfSlot) {
        this(path.getLeftCSV(), path.getRightCSV(), pidfSlot);
    }

    /**
     * Convert a Pathfinder segment to an SRX trajectory point
     *
     * @param seg         Pathfinder segment
     * @param zeroPos     Should we zero the SRX encoders (i.e. is it the first point?)
     * @param isLastPoint Should we hold the position after this point (i.e. is it the last point?)
     * @return a trajectory point for the talon srx
     */
    private TrajectoryPoint processSegment(Trajectory.Segment seg, boolean zeroPos, boolean isLastPoint) {
        // convert from feet to Native Units
        trajPoint.position = seg.position * TICKS_PER_FT;
        // convert from feet/s to native units / 100ms
        trajPoint.velocity = seg.velocity * TICKS_PER_FT * HUNDRED_MS_PER_SEC;
        trajPoint.headingDeg = seg.heading;
        trajPoint.profileSlotSelect0 = pidfSlot;
        trajPoint.timeDur = TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_10ms;
        trajPoint.zeroPos = zeroPos;
        trajPoint.isLastPoint = isLastPoint;

        return trajPoint;
    }

    public TrajectoryPoint[] getLeftTrajectoryPoints() {
        return translateTrajectory(leftTraj);
    }

    public TrajectoryPoint[] getRightTrajectoryPoints() {
        return translateTrajectory(rightTraj);
    }

    /**
     * @param traj Pathfinder trajectory
     * @return Array of TrajectoryPoint for SRX
     */
    private TrajectoryPoint[] translateTrajectory(Trajectory traj) {
        ArrayList<TrajectoryPoint> points = new ArrayList<>();
        for (int i = 0; i < traj.length(); i++) {
            boolean zeroPos = (i == 0);
            boolean isLastPoint = ((i + i) == traj.length());
            TrajectoryPoint point = processSegment(traj.get(i), zeroPos, isLastPoint);
            points.add(point);
        }
        return points.toArray(new TrajectoryPoint[points.size()]);
    }
}
