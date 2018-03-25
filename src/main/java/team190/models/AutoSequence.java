package team190.models;

import edu.wpi.first.wpilibj.DriverStation;
import jaci.pathfinder.Pathfinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * Sequences that make up an auto. Each possible autonomous will likely use a combination of AutoSequences.
 * For example, if you started on the left and had possession of the left plate of the scale, wanted to collect 2
 * power cubes: You would combine the following AutoSequence's: StartLeftScaleLeft, ScaleLeftCollectCubeOne,
 * ScaleLeftPlaceCubeOne, ScaleLeftCollectCubeTwo, ScaleLeftPlaceCubeTwo
 * Created by Kevin O'Brien on 1/25/2018.
 */
public enum AutoSequence {
    StartRightScaleLeft,
    ScaleLeftCollectCubeOne,
    ScaleLeftPlaceCubeOne,

    ScaleRightCollectCubeOne,

    StartRightScaleRight,
    StartLeftScaleLeft,

    StartRightSwitchRight,

    StartCenterSwitchLeft,
    StartCenterSwitchRight;

    private static HashMap<AutoSequence, PairedTrajectory> trajectories;
    private final String directory = "/home/lvuser/sequences";

    public static void loadTrajectories() throws FileNotFoundException {
        trajectories = new HashMap<>();
        for (AutoSequence sequence : AutoSequence.values()) {
            File lFile = new File(sequence.getLeftCSV());
            File rFile = new File(sequence.getRightCSV());

            if (!lFile.exists()) {
                DriverStation.reportWarning(sequence.getLeftCSV() + " not found.", false);
                throw new FileNotFoundException();
            } else if (!rFile.exists()) {
                DriverStation.reportWarning(sequence.getRightCSV() + " not found.", false);
                throw new FileNotFoundException();
            } else {
                trajectories.put(sequence, new PairedTrajectory(Pathfinder.readFromCSV(lFile), Pathfinder.readFromCSV(rFile)));
            }
        }
    }

    public PairedTrajectory getPairedTrajectory() throws FileNotFoundException {
        if (trajectories == null) {
            loadTrajectories();
        }
        return trajectories.get(this);
    }

    public String getLeftCSV() {
        return directory + "/" + name() + "_left_detailed.csv";
    }

    public String getRightCSV() {
        return directory + "/" + name() + "_right_detailed.csv";
    }
}
