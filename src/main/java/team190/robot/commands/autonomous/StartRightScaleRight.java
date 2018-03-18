package team190.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitForChildren;
import team190.models.AutoSequence;
import team190.robot.commands.CollectCube;
import team190.robot.commands.DelayedCommand;
import team190.robot.commands.collector.CollectorExtakeFront;
import team190.robot.commands.drivetrain.DriveSequence;
import team190.robot.commands.elevator.ElevatorPositionCarriage;
import team190.robot.commands.elevator.ElevatorPositionHigh;

/**
 * Created by Kevin O'Brien on 3/14/2018.
 */

public class StartRightScaleRight extends CommandGroup {

    public StartRightScaleRight() {
        // start moving elevator at start of CommandGroup
        addSequential(new ElevatorPositionCarriage());
        addParallel(new DelayedCommand(2.0, new ElevatorPositionHigh()));
        // Drive to the Scale
        addSequential(new DriveSequence(AutoSequence.StartRightScaleRight));
        // wait for elevator to be in position
        addSequential(new WaitForChildren());
        // Extake the cube
        addSequential(new CollectorExtakeFront());
    }
}
