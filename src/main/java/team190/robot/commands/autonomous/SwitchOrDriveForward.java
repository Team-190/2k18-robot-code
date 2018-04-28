package team190.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import openrio.powerup.MatchData;
import team190.robot.models.AutoSequence;
import team190.robot.Robot;
import team190.robot.commands.collector.CollectorExtakeRear;
import team190.robot.commands.drivetrain.DriveForTimeAndSpeed;
import team190.robot.commands.drivetrain.DriveSequence;
import team190.robot.commands.elevator.ElevatorPositionCarriage;

/**
 * Created by Kevin O'Brien on 4/27/2018.
 */
public class SwitchOrDriveForward extends ConditionalCommand {
    private MatchData.OwnedSide position;

    public SwitchOrDriveForward(MatchData.OwnedSide position) {
        super(new ScoreSwitch(position), new DriveForTimeAndSpeed(Robot.TIME_CROSS_LINE));
        this.position = position;
    }

    @Override
    protected boolean condition() {
        MatchData.OwnedSide side = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);
        return side == position;
    }

    public static class ScoreSwitch extends CommandGroup {

        ScoreSwitch(MatchData.OwnedSide position) {
            AutoSequence driveSwitch = AutoSequence.StartRightSwitchRight;
            if (position == MatchData.OwnedSide.LEFT) {
                driveSwitch = AutoSequence.StartLeftSwitchLeft;
            }
            // start moving elevator at start of CommandGroup
            addSequential(new ElevatorPositionCarriage());
            addSequential(new DriveSequence(driveSwitch));
            addSequential(new CollectorExtakeRear());
        }
    }
}
