package team190.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import openrio.powerup.MatchData;
import team190.models.AutoSequence;

/**
 * Created by Kevin O'Brien on 1/25/2018.
 */
public class AutoScale extends CommandGroup {
    public AutoScale(StartSide start) {
        MatchData.OwnedSide scale = MatchData.getOwnedSide(MatchData.GameFeature.SCALE);

        if (scale.equals(MatchData.OwnedSide.LEFT)) {
            if (start.equals(StartSide.LEFT)) {
                addSequential(new FollowTrajectory(AutoSequence.StartLeftScaleLeft));
            } else if (start.equals(StartSide.RIGHT)) {
                addSequential(new FollowTrajectory(AutoSequence.StartRightScaleLeft));
            }
            // Go get and place the first cube
            addSequential(new FollowTrajectory(AutoSequence.ScaleLeftCollectCubeOne));
            addSequential(new FollowTrajectory(AutoSequence.ScaleLeftPlaceCubeOne));

            // Go get and place the second cube
            addSequential(new FollowTrajectory(AutoSequence.ScaleLeftCollectCubeTwo));
            addSequential(new FollowTrajectory(AutoSequence.ScaleLeftPlaceCubeTwo));

        } else if (scale.equals(MatchData.OwnedSide.RIGHT)) {
            if (start.equals(StartSide.LEFT)) {
                addSequential(new FollowTrajectory(AutoSequence.StartLeftScaleRight));
            } else if (start.equals(StartSide.RIGHT)) {
                addSequential(new FollowTrajectory(AutoSequence.StartRightScaleRight));
            }
            // go get and place the first cube
            addSequential(new FollowTrajectory(AutoSequence.ScaleRightCollectCubeOne));
            addSequential(new FollowTrajectory(AutoSequence.ScaleRightPlaceCubeOne));

            // Go get and place the second cube
            addSequential(new FollowTrajectory(AutoSequence.ScaleRightCollectCubeTwo));
            addSequential(new FollowTrajectory(AutoSequence.ScaleRightPlaceCubeTwo));
        }
    }

    public enum StartSide {
        LEFT, RIGHT
    }
}
