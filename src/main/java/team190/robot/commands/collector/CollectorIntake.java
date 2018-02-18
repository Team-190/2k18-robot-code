package team190.robot.commands.collector;


import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;
import team190.robot.subsystems.Collector;

/**
 *
 */
public class CollectorIntake extends Command {

    public CollectorIntake() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.collector);

    }

    @Override
    protected void initialize() {
        //TODO: add timeout

    }

    @Override
    protected void execute() {
        Robot.collector.intake(Collector.IntakeMode.Intake);
    }

    @Override
    protected boolean isFinished() {
        return Robot.collector.hasCube() || Robot.carriage.hasCube();
    }

    @Override
    protected void end() {
        Robot.collector.intake(Collector.IntakeMode.Stop);
    }

    @Override
    protected void interrupted() {
        end();
    }
}
