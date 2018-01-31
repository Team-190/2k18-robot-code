package team190.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;
import team190.robot.subsystems.Drivetrain;

/**
 * Created by Kevin O'Brien on 1/29/2018.
 */
public class TestVelocityControl extends Command {

    public TestVelocityControl() {
        requires(Robot.drivetrain);
    }

    protected void initialize() {
        Robot.drivetrain.setPositionZero();
    }

    @Override
    protected void execute() {
        double speed = Drivetrain.feetPerSecToTicksPerHundredMs(5.0);
        System.out.println("speed = " + speed);
        Robot.drivetrain.drive(ControlMode.Velocity, speed, speed);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
