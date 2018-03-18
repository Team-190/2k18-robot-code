package team190.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import team190.robot.commands.collector.CollectorIntake;
import team190.robot.commands.elevator.ElevatorPositionCarriage;
import team190.robot.commands.elevator.ElevatorPositionIntake;

public class CollectCube extends CommandGroup {

    private boolean canIntake;

    public CollectCube() {
        addSequential(new ElevatorPositionIntake());
        addSequential(new CollectorIntake());
        addSequential(new ElevatorPositionCarriage());
        //addSequential(new CollectorTransferCarriage());
    }
/*
    @Override
    protected void initialize() {
        super.initialize();

        canIntake = Robot.carriage.hasCube();
    }

    @Override
    protected boolean isFinished() {
        return super.isFinished() || !canIntake;
    }*/
}
