package team190.models;

/**
 * Created by Kevin O'Brien on 1/25/2018.
 */
public enum AutoPath {
    StartLeftScaleLeft,
    StartLeftScaleRight,
    StartRightScaleLeft,
    StartRightScaleRight,

    ScaleLeftCollectCubeOne,
    ScaleLeftPlaceCubeOne,
    ScaleLeftCollectCubeTwo,
    ScaleLeftPlaceCubeTwo,

    ScaleRightCollectCubeOne,
    ScaleRightPlaceCubeOne,
    ScaleRightCollectCubeTwo,
    ScaleRightPlaceCubeTwo;

    private String directory = "/home/lvuser/Paths";

    public String getLeftCSV() {
        return directory + "/" + name() + "left_detailed.csv";
    }

    public String getRightCSV() {
        return directory + "/" + name() + "right_detailed.csv";
    }
}
