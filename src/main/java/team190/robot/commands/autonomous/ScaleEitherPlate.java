package team190.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import openrio.powerup.MatchData;
import team190.models.AutoSequence;
import team190.robot.commands.autonomous.ScaleSwitchOrDriveForward;
import team190.robot.commands.collector.CollectorExtakeRear;
import team190.robot.commands.drivetrain.DriveSequence;
import team190.robot.commands.elevator.ElevatorPositionCarriage;
import team190.robot.commands.elevator.ElevatorPositionHigh;

/**
 * Created by Kevin O'Brien on 4/4/2018.
 */
public class ScaleEitherPlate extends ConditionalCommand {
    public ScaleEitherPlate() {
        super(new ScaleSwitchOrDriveForward(MatchData.OwnedSide.RIGHT), new ScaleCrossover());
    }

    @Override
    protected boolean condition() {
        MatchData.OwnedSide side = MatchData.getOwnedSide(MatchData.GameFeature.SCALE);
        return side == MatchData.OwnedSide.RIGHT;
    }

    /**
     * Created by Kevin O'Brien on 1/25/2018.
     */
    public static class ScaleCrossover extends CommandGroup {

        public ScaleCrossover() {
            // start moving elevator at start of CommandGroup
            addSequential(new ElevatorPositionCarriage());
            // Drive to the Scale
            addSequential(new DriveSequence(AutoSequence.StartRightScaleLeft));
            addSequential(new ElevatorPositionHigh());
            addSequential(new CollectorExtakeRear());
        }
    }
}
