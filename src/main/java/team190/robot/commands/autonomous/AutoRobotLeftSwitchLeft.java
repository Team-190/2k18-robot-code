package team190.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import openrio.powerup.MatchData;
import team190.robot.Robot;

/**
 * Created by Kevin O'Brien on 3/18/2018.
 */
public class AutoRobotLeftSwitchLeft extends ConditionalCommand {
    public AutoRobotLeftSwitchLeft() {
        this(new DriveAndSpit(), new DriveForward(Robot.TIME_CROSS_LINE));
    }

    public AutoRobotLeftSwitchLeft(Command onTrue, Command onFalse) {
        super(onTrue, onFalse);
    }

    @Override
    protected boolean condition() {
        MatchData.OwnedSide side = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);
        return side == MatchData.OwnedSide.LEFT;
    }
}