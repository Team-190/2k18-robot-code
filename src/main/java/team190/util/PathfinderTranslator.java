package team190.util;

import com.ctre.phoenix.motion.TrajectoryPoint;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Kevin O'Brien on 1/25/2018.
 */
public class PathfinderTranslator {

    private Trajectory leftTraj;
    private Trajectory rightTraj;

    private int pidfSlot;

    public PathfinderTranslator(Waypoint points[], int pidfSlot) {
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC,
                Trajectory.Config.SAMPLES_HIGH, 0.05, 14, 10, 60);
        Trajectory trajectory = Pathfinder.generate(points, config);

        TankModifier mod = new TankModifier(trajectory);
        mod.modify(2.1); //wheelbase

        leftTraj = mod.getLeftTrajectory();
        rightTraj = mod.getRightTrajectory();

        this.pidfSlot = pidfSlot;
    }

    public PathfinderTranslator(String leftCSV, String rightCSV, int pidfSlot) {
        File left = new File(leftCSV);
        File right = new File(rightCSV);
        leftTraj = Pathfinder.readFromCSV(left);
        rightTraj = Pathfinder.readFromCSV(right);

        this.pidfSlot = pidfSlot;
    }

    public TrajectoryPoint processSegment(Trajectory.Segment seg, boolean zeroPos, boolean isLastPoint) {
        TrajectoryPoint point = new TrajectoryPoint();
        point.position = seg.position;
        point.velocity = seg.velocity;
        point.headingDeg = seg.heading;
        point.profileSlotSelect0 = pidfSlot;
        point.timeDur = TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_10ms;
        point.zeroPos = zeroPos;
        point.isLastPoint = isLastPoint;

        return point;
    }

    public TrajectoryPoint[] getLeftTrajectoryPoints() {
        return translateTrajectory(leftTraj);
    }

    public TrajectoryPoint[] getRightTrajectoryPoints() {
        return translateTrajectory(rightTraj);
    }

    public TrajectoryPoint[] translateTrajectory(Trajectory traj) {
        ArrayList<TrajectoryPoint> points = new ArrayList<>();
        for (int i = 0; i < traj.length(); i++) {
            boolean zeroPos = (i == 0);
            boolean isLastPoint = ((i+i) == traj.length());
            TrajectoryPoint point = processSegment(traj.get(i), zeroPos, isLastPoint);
            points.add(point);
        }
        return (TrajectoryPoint[]) points.toArray();
    }
}
