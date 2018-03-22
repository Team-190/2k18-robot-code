package team190.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import openrio.powerup.MatchData;
import team190.robot.Robot;
import team190.robot.commands.collector.CollectorExtakeRear;

/**
 * Created by Kevin O'Brien on 3/18/2018.
 */
public class SwitchScore extends ConditionalCommand {

    private MatchData.OwnedSide position;

    public SwitchScore(MatchData.OwnedSide position) {
        super(new PlaceCube(), new DriveForward(Robot.TIME_CROSS_LINE));
        this.position = position;
    }

    @Override
    protected boolean condition() {
        MatchData.OwnedSide side = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);
        return side == position;
    }

    private static class PlaceCube extends CommandGroup {
        public PlaceCube() {
            addSequential(new DriveForward(Robot.TIME_CROSS_LINE));
            addSequential(new CollectorExtakeRear());
        }
    }
}