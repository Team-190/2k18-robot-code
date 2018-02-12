package team190.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import openrio.powerup.MatchData;
import team190.models.AutoSequence;

/**
 * Created by Kevin O'Brien on 1/25/2018.
 */
public class AutoScale extends CommandGroup {

    public AutoScale() {
        addSequential(new DriveDistance(AutoSequence.StartRightScaleLeft));
        addSequential(new DriveDistance(AutoSequence.ScaleLeftCollectCubeOne, false));
        addSequential(new DriveDistance(AutoSequence.ScaleLeftPlaceCubeOne, false));
    }
    public AutoScale(StartSide start) {

        /*
        MatchData.OwnedSide scale = MatchData.getOwnedSide(MatchData.GameFeature.SCALE);

        if (scale.equals(MatchData.OwnedSide.LEFT)) {
            if (start.equals(StartSide.LEFT)) {
                addSequential(new FollowSequence(AutoSequence.StartLeftScaleLeft));
            } else if (start.equals(StartSide.RIGHT)) {
                addSequential(new FollowSequence(AutoSequence.StartRightScaleLeft));
            }
            // Go get and place the first cube
            addSequential(new FollowSequence(AutoSequence.ScaleLeftCollectCubeOne));
            addSequential(new FollowSequence(AutoSequence.ScaleLeftPlaceCubeOne));

            // Go get and place the second cube
            addSequential(new FollowSequence(AutoSequence.ScaleLeftCollectCubeTwo));
            addSequential(new FollowSequence(AutoSequence.ScaleLeftPlaceCubeTwo));

        } else if (scale.equals(MatchData.OwnedSide.RIGHT)) {
            if (start.equals(StartSide.LEFT)) {
                addSequential(new FollowSequence(AutoSequence.StartLeftScaleRight));
            } else if (start.equals(StartSide.RIGHT)) {
                addSequential(new FollowSequence(AutoSequence.StartRightScaleRight));
            }
            // go get and place the first cube
            addSequential(new FollowSequence(AutoSequence.ScaleRightCollectCubeOne));
            addSequential(new FollowSequence(AutoSequence.ScaleRightPlaceCubeOne));

            // Go get and place the second cube
            addSequential(new FollowSequence(AutoSequence.ScaleRightCollectCubeTwo));
            addSequential(new FollowSequence(AutoSequence.ScaleRightPlaceCubeTwo));
        }
        */
    }

    public enum StartSide {
        LEFT, RIGHT
    }
}
