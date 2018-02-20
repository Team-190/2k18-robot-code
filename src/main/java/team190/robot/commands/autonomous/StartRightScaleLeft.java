package team190.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitForChildren;
import team190.models.AutoSequence;
import team190.robot.commands.CollectCube;
import team190.robot.commands.collector.CollectorExtakeFront;
import team190.robot.commands.drivetrain.DriveSequence;
import team190.robot.commands.elevator.ElevatorPositionCarriage;
import team190.robot.commands.elevator.ElevatorPositionHigh;

/**
 * Created by Kevin O'Brien on 1/25/2018.
 */
public class StartRightScaleLeft extends CommandGroup {

    public StartRightScaleLeft() {
        // start moving elevator at start of CommandGroup
        addParallel(new ElevatorPositionHigh());
        // Drive to the Scale
        addSequential(new DriveSequence(AutoSequence.StartRightScaleLeft));
        // wait for elevator to be in position
        addSequential(new WaitForChildren());
        // Extake the cube
        addSequential(new CollectorExtakeFront());
        // Move elevator to carriage height
        addParallel(new ElevatorPositionCarriage());
        // Drive to the first cube on the switch
        addSequential(new DriveSequence(AutoSequence.ScaleLeftCollectCubeOne, false));
        // Wait for elevator to be in position
        addSequential(new WaitForChildren());
        // collect the cube and return to carriage height
        addSequential(new CollectCube());
        // Move elevator to placement position
        addParallel(new ElevatorPositionHigh());
        // Drive to scale
        addSequential(new DriveSequence(AutoSequence.ScaleLeftPlaceCubeOne, false));
        // Wait for elevator to be in position
        addSequential(new WaitForChildren());
        // Extake the cube
        addSequential(new CollectorExtakeFront());
    }
}
