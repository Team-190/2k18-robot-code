package team190.robot.commands.collector;

import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;
import team190.robot.subsystems.Carriage;
import team190.robot.subsystems.Collector;

public class CollectorExtakeFront extends Command {

    private boolean canExtake;

    public CollectorExtakeFront() {
        requires(Robot.collector);
        requires(Robot.carriage);
    }

    @Override
    protected void initialize() {
        canExtake = Robot.carriage.hasCube();

        setTimeout(1); // TODO find actual timeout for extake front
    }

    @Override
    protected void execute() {
        if (!isFinished()) {
            Robot.collector.move(Collector.IntakeMode.Extake);
            Robot.carriage.move(Carriage.CarriageMode.Stop);
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
        Robot.collector.move(Collector.IntakeMode.Stop);
        Robot.carriage.move(Carriage.CarriageMode.Stop);
    }
}
