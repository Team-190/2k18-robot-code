package team190.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;

/**
 * Created by Kevin O'Brien on 1/29/2018.
 */
public class TestVelocityControl extends Command {

    public TestVelocityControl() {
        requires(Robot.drivetrain);
    }

    @Override
    protected void execute() {
        Robot.drivetrain.drive(ControlMode.Velocity, 5000, 5000);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
