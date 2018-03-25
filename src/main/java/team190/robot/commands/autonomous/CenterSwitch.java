package team190.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import openrio.powerup.MatchData;
import team190.models.AutoSequence;
import team190.robot.commands.collector.CollectorExtakeRear;
import team190.robot.commands.drivetrain.DriveSequence;
import team190.robot.commands.elevator.ElevatorPositionCarriage;

/**
 * Created by Kevin O'Brien on 3/25/2018.
 */

public class CenterSwitch extends ConditionalCommand {
    public CenterSwitch() {
        super(new ScoreSwitchCenter(MatchData.OwnedSide.LEFT), new ScoreSwitchCenter(MatchData.OwnedSide.RIGHT));
    }


    @Override
    protected boolean condition() {
        MatchData.OwnedSide side = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);
        return side == MatchData.OwnedSide.LEFT;
    }

    private static class ScoreSwitchCenter extends CommandGroup {
        public ScoreSwitchCenter(MatchData.OwnedSide side) {
            AutoSequence driveSwitch = AutoSequence.StartCenterSwitchRight;
            if (side == MatchData.OwnedSide.LEFT) {
                driveSwitch = AutoSequence.StartCenterSwitchLeft;
            }
            addSequential(new ElevatorPositionCarriage());
            addSequential(new DriveSequence(driveSwitch));
            addSequential(new CollectorExtakeRear());
        }
    }
}
