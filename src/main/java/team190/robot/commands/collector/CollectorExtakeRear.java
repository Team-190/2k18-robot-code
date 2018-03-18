package team190.robot.commands.collector;

import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;
import team190.robot.subsystems.Carriage;
import team190.robot.subsystems.Collector;

public class CollectorExtakeRear extends Command {

    private boolean canExtake;

    public CollectorExtakeRear() {
        requires(Robot.collector);
        requires(Robot.carriage);
    }

    @Override
    protected void initialize() {
        canExtake = Robot.collector.hasCube();

        setTimeout(3); // TODO find actual timeout for extake rear
    }

    @Override
    protected void execute() {
        if (!isFinished()) {
            Robot.carriage.move(Carriage.CarriageMode.Extake);
            Robot.collector.move(Collector.IntakeMode.Stop);
        }
    }

    @Override
    protected boolean isFinished() {
        return !canExtake || isTimedOut();
    }

    @Override
    protected void interrupted() {
        end();
    }

    @Override
    protected void end() {
        Robot.carriage.move(Carriage.CarriageMode.Stop);
        Robot.collector.move(Collector.IntakeMode.Stop);
    }
}
