package team190.models;

/**
 * Sequences that make up an auto. Each possible autonomous will likely use a combination of AutoSequences.
 * For example, if you started on the left and had possession of the left plate of the scale, wanted to collect 2
 * power cubes: You would combine the following AutoSequence's: StartLeftScaleLeft, ScaleLeftCollectCubeOne,
 * ScaleLeftPlaceCubeOne, ScaleLeftCollectCubeTwo, ScaleLeftPlaceCubeTwo
 * Created by Kevin O'Brien on 1/25/2018.
 */
public enum AutoSequence {
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
    ForwardTenFeet;

    private final String directory = "/home/lvuser/Sequences";

    public String getLeftCSV() {
        return directory + "/" + name() + "left_detailed.csv";
    }

    public String getRightCSV() {
        return directory + "/" + name() + "right_detailed.csv";
    }
}
