package team190.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import edu.wpi.first.wpilibj.command.WaitForChildren;
import openrio.powerup.MatchData;
import team190.models.AutoSequence;
import team190.robot.Robot;
import team190.robot.commands.CollectCube;
import team190.robot.commands.autonomous.scale.StartLeftScaleLeft;
import team190.robot.commands.collector.CollectorExtakeRear;
import team190.robot.commands.collector.CollectorIntake;
import team190.robot.commands.drivetrain.DriveSequence;
import team190.robot.commands.elevator.ElevatorPositionCarriage;
import team190.robot.commands.elevator.ElevatorPositionHigh;

/**
 * Created by Kevin O'Brien on 3/14/2018.
 */
public class ScaleSwitchOrDriveForward extends ConditionalCommand {

    private MatchData.OwnedSide position;
    public ScaleSwitchOrDriveForward(MatchData.OwnedSide position) {
        super(new ScaleScore(position), new CheckSwitch(position));
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

    public static class ScaleScore extends CommandGroup {

        public ScaleScore(MatchData.OwnedSide position) {
            AutoSequence driveScale;
            AutoSequence driveCubeOne;
            if (position == MatchData.OwnedSide.LEFT) {
                driveScale = AutoSequence.StartLeftScaleLeft;
                driveCubeOne = AutoSequence.ScaleLeftCollectCubeOne;
            } else {
                driveScale = AutoSequence.StartRightScaleRight;
                driveCubeOne = AutoSequence.ScaleRightCollectCubeOne;
            }
            // start moving elevator at start of CommandGroup
            addSequential(new ElevatorPositionCarriage());
            //addParallel(new DelayedCommand(2.0, new ElevatorPositionHigh()));
            // Drive to the Scale
            addSequential(new DriveSequence(driveScale));

            addSequential(new ElevatorPositionHigh());
            //addSequential(new DriveForward(0.25));
            addSequential(new CollectorExtakeRear());

            // Pick up next cube
            addSequential(new ElevatorPositionCarriage());
            addParallel(new CollectCube());
            addSequential(new DriveSequence(AutoSequence.ScaleRightCollectCubeOne));
            addSequential(new WaitForChildren());

            /*
            addParallel(new ElevatorPositionCarriage());
            addSequential(new DriveSequence(AutoSequence.ScaleLeftCollectCubeOne));
            addSequential(new WaitForChildren());
            addSequential(new CollectCube());
            addParallel(new ElevatorPositionHigh());
            addSequential(new DriveSequence(AutoSequence.ScaleLeftPlaceCubeOne));
            addSequential(new WaitForChildren());
            addSequential(new CollectorExtakeRear());
            */

        }
    }

    public static class CheckSwitch extends ConditionalCommand {
        private MatchData.OwnedSide position;
        public CheckSwitch(MatchData.OwnedSide position) {
            super(new SwitchPath(position), new DriveForward(Robot.TIME_CROSS_LINE));
            this.position = position;
        }

        @Override
        protected boolean condition() {
            MatchData.OwnedSide side = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);
            return side == position;
        }

    }

    public static class SwitchPath extends CommandGroup {

        public SwitchPath(MatchData.OwnedSide position) {
            AutoSequence driveSwitch = AutoSequence.StartRightSwitchRight;
            // start moving elevator at start of CommandGroup
            addSequential(new ElevatorPositionCarriage());
            addSequential(new DriveSequence(driveSwitch));
            addSequential(new CollectorExtakeRear());
        }
    }

}
