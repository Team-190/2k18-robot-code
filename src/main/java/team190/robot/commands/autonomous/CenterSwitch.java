package team190.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import openrio.powerup.MatchData;
import team190.robot.commands.collector.CollectorExtakeRear;
import team190.robot.commands.drivetrain.DriveSequence;
import team190.robot.commands.elevator.ElevatorPositionCarriage;
import team190.robot.models.AutoSequence;

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
        ScoreSwitchCenter(MatchData.OwnedSide side) {
            AutoSequence driveSwitch = AutoSequence.StartCenterSwitchRight;
            //AutoSequence collectCube = AutoSequence.SwitchRightCollectCube;
            //AutoSequence placeCube = AutoSequence.SwitchRightPlaceCube;
            if (side == MatchData.OwnedSide.LEFT) {
                driveSwitch = AutoSequence.StartCenterSwitchLeft;
                //collectCube = AutoSequence.SwitchLeftCollectCube;
                //placeCube = AutoSequence.SwitchLeftPlaceCube;
            }
            addSequential(new ElevatorPositionCarriage());
            addSequential(new DriveSequence(driveSwitch));
            addSequential(new CollectorExtakeRear());
            /*
            addParallel(new CollectCube());
            addSequential(new DriveSequence(collectCube, false));
            addSequential(new WaitForChildren());
            addSequential(new DriveSequence(placeCube, false));
            addSequential(new CollectorExtakeRear());
            */
        }
    }
}
