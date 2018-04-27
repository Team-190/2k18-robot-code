package team190.robot.commands.collector;

import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;
import team190.robot.subsystems.Carriage;
import team190.robot.subsystems.Collector;

public class CollectorExtakeRear extends Command {

    public CollectorExtakeRear() {
        requires(Robot.collector);
        requires(Robot.carriage);
    }

    @Override
    protected void initialize() {
        setTimeout(0.5);
    }

    @Override
    protected void execute() {
        if (!isFinished()) {
            Robot.carriage.move(Carriage.CarriageMode.Extake);
            Robot.collector.move(Collector.IntakeMode.Intake);
        }
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
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
