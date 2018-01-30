package team190.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import team190.robot.commands.collector.CollectorIntake;
import team190.robot.commands.elevator.ElevatorPositionCarriage;
import team190.robot.commands.elevator.ElevatorPositionIntake;

public class CollectCube extends CommandGroup {

    public CollectCube() {
        addSequential(new ElevatorPositionIntake());
        addSequential(new CollectorIntake());
        addSequential(new ElevatorPositionCarriage());

    }

    @Override
    protected boolean isFinished() {
        return super.isFinished(); // TODO return true if either super.isFinished() or if a cube is already loaded
    }
}
