package team190.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import team190.models.AutoSequence;
import team190.robot.commands.drivetrain.DriveSequence;

/**
 * Created by Kevin O'Brien on 1/25/2018.
 */
public class StartRightScaleLeft extends CommandGroup {

    public StartRightScaleLeft() {
        addSequential(new DriveSequence(AutoSequence.StartRightScaleLeft));
        addSequential(new DriveSequence(AutoSequence.ScaleLeftCollectCubeOne, false));
        addSequential(new DriveSequence(AutoSequence.ScaleLeftPlaceCubeOne, false));
    }
}
