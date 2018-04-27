package team190.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import edu.wpi.first.wpilibj.command.WaitForChildren;
import openrio.powerup.MatchData;
import team190.models.AutoSequence;
import team190.robot.commands.CollectCube;
import team190.robot.commands.DelayedCommand;
import team190.robot.commands.collector.CollectorExtakeRear;
import team190.robot.commands.drivetrain.DriveSequence;
import team190.robot.commands.elevator.ElevatorPositionCarriage;
import team190.robot.commands.elevator.ElevatorPositionHigh;

/**
 * Created by Kevin O'Brien on 3/14/2018.
 */
public class ScaleSwitchOrDriveForward extends ConditionalCommand {

    private MatchData.OwnedSide position;

    public ScaleSwitchOrDriveForward(MatchData.OwnedSide position) {
        super(new ScoreScale(), new SwitchOrDriveForward(position));
        this.position = position;
    }

    @Override
    protected boolean condition() {
        MatchData.OwnedSide side = MatchData.getOwnedSide(MatchData.GameFeature.SCALE);
        return side == position;
    }

    /**
     * Created by Kevin O'Brien on 3/14/2018.
     */

    public static class ScoreScale extends CommandGroup {

        ScoreScale() {
            addParallel(new DelayedCommand(2, new ElevatorPositionHigh()));

            addSequential(new DriveSequence(AutoSequence.StartRightScaleRight));
            addSequential(new WaitForChildren());
            addSequential(new CollectorExtakeRear());

            // Pick up next cube
            addSequential(new ElevatorPositionCarriage());
            addParallel(new CollectCube());
            addSequential(new DriveSequence(AutoSequence.ScaleRightCollectCubeOne, false));
            //addSequential(new DriveForTimeAndSpeed(-0.2, 0.25));
            addSequential(new WaitForChildren());

            addParallel(new ElevatorPositionHigh());
            addSequential(new DriveSequence(AutoSequence.ScaleRightPlaceCubeOne, false));
            addSequential(new WaitForChildren());
            addSequential(new CollectorExtakeRear());
        }
    }

}
