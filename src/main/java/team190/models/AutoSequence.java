package team190.models;

import team190.robot.subsystems.Drivetrain;
import team190.util.PathfinderTranslator;

import java.util.HashMap;

/**
 * Sequences that make up an auto. Each possible autonomous will likely use a combination of AutoSequences.
 * For example, if you started on the left and had possession of the left plate of the scale, wanted to collect 2
 * power cubes: You would combine the following AutoSequence's: StartLeftScaleLeft, ScaleLeftCollectCubeOne,
 * ScaleLeftPlaceCubeOne, ScaleLeftCollectCubeTwo, ScaleLeftPlaceCubeTwo
 * Created by Kevin O'Brien on 1/25/2018.
 */
public enum AutoSequence {
    ForwardTenFeet,
    ScaleLeftCollectCubeOne,
    StartRightScaleLeft;
    /*
    // Starting Positions => Scale
    StartLeftScaleLeft,
    StartLeftScaleRight,
    StartRightScaleLeft,
    StartRightScaleRight,

    // Left Scale => Collect and Place Power Cubes
    ScaleLeftCollectCubeOne,
    ScaleLeftPlaceCubeOne,
    ScaleLeftCollectCubeTwo,
    ScaleLeftPlaceCubeTwo,

    // Right Scale => Collect and Place Power Cubes
    ScaleRightCollectCubeOne,
    ScaleRightPlaceCubeOne,
    ScaleRightCollectCubeTwo,
    ScaleRightPlaceCubeTwo,

    // Starting Positions => Switch
    StartLeftSwitchLeft,
    StartRightSwitchRight,

    // Basic Drive Straights
    ForwardFiveFeet,
    ForwardTenFeet,

    // Other
    TestSCurve,
    CoursePath;*/

    private static HashMap<AutoSequence, PairedTrajectoryPoints> trajectories;

    public PairedTrajectoryPoints getPairedTrajectoryPoints() {
        if (trajectories == null) {
            loadTrajectories();
        }
        return trajectories.get(this);
    }

    public static void loadTrajectories() {
        trajectories = new HashMap<>();
        for (AutoSequence sequence: AutoSequence.values()) {
            PathfinderTranslator path = new PathfinderTranslator(sequence, Drivetrain.HIGH_GEAR_PROFILE);
            trajectories.put(sequence, new PairedTrajectoryPoints(path.getLeftTrajectoryPoints(), path.getRightTrajectoryPoints()));
        }
    }

    private final String directory = "/home/lvuser/sequences";

    public String getLeftCSV() {
        return directory + "/" + name() + "_left_detailed.csv";
    }

    public String getRightCSV() {
        return directory + "/" + name() + "_right_detailed.csv";
    }
}
