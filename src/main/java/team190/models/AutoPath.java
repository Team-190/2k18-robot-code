package team190.models;

/**
 * Created by Kevin O'Brien on 1/25/2018.
 */
public enum AutoPath {
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

    private String directory = "/home/lvuser/Paths";

    public String getLeftCSV() {
        return directory + "/" + name() + "left_detailed.csv";
    }

    public String getRightCSV() {
        return directory + "/" + name() + "right_detailed.csv";
    }
}
