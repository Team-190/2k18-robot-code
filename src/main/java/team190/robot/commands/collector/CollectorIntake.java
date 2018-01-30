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

    // Called just before this Command runs the first time
    protected void initialize() { }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.collector.intake(Collector.IntakeMode.Intake);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.collector.cubeGrabbed(); // TODO maybe also finish if a cube is detected in the carriage?
    }
    // Called once after isFinished returns true
    protected void end() {
    	Robot.collector.intake(Collector.IntakeMode.Stop);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.collector.intake(Collector.IntakeMode.Stop);
    }
}
