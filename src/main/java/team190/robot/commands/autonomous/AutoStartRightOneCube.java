package team190.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import openrio.powerup.MatchData;
import team190.robot.Robot;

/**
 * Created by Kevin O'Brien on 2/19/2018.
 */
public class AutoStartRightOneCube extends ConditionalCommand {
    public AutoStartRightOneCube() {
        // TODO: add the ScaleRight Command
        this(new StartRightScaleLeft(), new DriveForward(Robot.TIME_CROSS_LINE));
    }

    public AutoStartRightOneCube(Command onTrue, Command onFalse) {
        super(onTrue, onFalse);
    }

    @Override
    protected boolean condition() {
        MatchData.OwnedSide side = MatchData.getOwnedSide(MatchData.GameFeature.SCALE);
        return side == MatchData.OwnedSide.LEFT;
    }
}
