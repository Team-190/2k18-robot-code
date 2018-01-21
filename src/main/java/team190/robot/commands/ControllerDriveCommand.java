package team190.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;

public class ControllerDriveCommand extends Command {

    public ControllerDriveCommand() {
        requires(Robot.drivetrain);
    }

    @Override
    protected void execute() {
        double left = Robot.m_oi.getLeftY();
        double right = Robot.m_oi.getRightY();

        Robot.drivetrain.drive(ControlMode.PercentOutput, left, right);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
