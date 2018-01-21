package team190.robot.commands;



import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;
import team190.robot.subsystems.Carriage;

/**
 *
 */
public class CarriagePlace extends Command {

    public CarriagePlace() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.carriage);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.carriage.move(Carriage.CarriageMode.Place);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return !Robot.carriage.inCarriage();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.carriage.move(Carriage.CarriageMode.Stop);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.carriage.move(Carriage.CarriageMode.Stop);
    }
}
