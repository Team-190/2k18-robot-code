package team190.models;

/**
 * Created by Kevin O'Brien on 1/25/2018.
 */
public enum AutoPaths {
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

    AutoPaths() {}

    private String directory = "/home/lvuser/Paths";


}
