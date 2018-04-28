package team190.robot.models;

/**
 * Sequences that make up an auto. Each possible autonomous will likely use a combination of AutoSequences.
 * For example, if you started on the left and had possession of the left plate of the scale, wanted to collect 2
 * power cubes: You would combine the following AutoSequence's: StartLeftScaleLeft, ScaleLeftCollectCubeOne,
 * ScaleLeftPlaceCubeOne, ScaleLeftCollectCubeTwo, ScaleLeftPlaceCubeTwo
 * Created by Kevin O'Brien on 1/25/2018.
 */
public enum AutoSequence {
    StartRightScaleLeft,

    StartRightScaleRight,
    ScaleRightCollectCubeOne,
    ScaleRightPlaceCubeOne,

    StartLeftSwitchLeft,
    StartRightSwitchRight,

    StartCenterSwitchLeft,
    StartCenterSwitchRight;

    private final String directory = "/home/lvuser/sequences";

    public String getLeftCSV() {
        return directory + "/" + name() + "_left_detailed.csv";
    }

    public String getRightCSV() {
        return directory + "/" + name() + "_right_detailed.csv";
    }
}
