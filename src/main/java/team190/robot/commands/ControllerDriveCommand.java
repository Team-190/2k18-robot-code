package team190.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;

public class ControllerDriveCommand extends Command {

    public ControllerDriveCommand() {
        requires(Robot.drivetrain);
    }



    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        double left = Robot.m_oi.getLeftY();
        double right = Robot.m_oi.getRightY();
        Robot.drivetrain.drive(ControlMode.PercentOutput, left, right);
    }

    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.drivetrain.drive(ControlMode.PercentOutput, 0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }

}
