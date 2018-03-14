package team190.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import openrio.powerup.MatchData;
import team190.models.AutoSequence;
import team190.robot.Robot;
import team190.robot.commands.drivetrain.DriveSequence;

/**
 * Created by Kevin O'Brien on 3/14/2018.
 */
public class AutoStartRightThisSide extends ConditionalCommand {

    public AutoStartRightThisSide() {
        this(new DriveSequence(AutoSequence.StartRightScaleRight), new DriveForward(Robot.TIME_CROSS_LINE));
    }

    public AutoStartRightThisSide(Command onTrue, Command onFalse) {
        super(onTrue, onFalse);
    }

    @Override
    protected boolean condition() {
        MatchData.OwnedSide side = MatchData.getOwnedSide(MatchData.GameFeature.SCALE);
        return side == MatchData.OwnedSide.RIGHT;
    }
}
