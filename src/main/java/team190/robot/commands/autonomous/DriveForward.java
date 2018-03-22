package team190.robot.commands.autonomous;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;
import team190.robot.subsystems.Drivetrain;

public class DriveForward extends Command {
    private double runTime;

    public DriveForward(double timeToRun) {
        requires(Robot.drivetrain);
        runTime = timeToRun;
    }

    @Override
    protected void initialize() {
        Robot.drivetrain.shift(Drivetrain.Gear.LOW);
        setTimeout(runTime);
    }

    @Override
    protected void execute() {
        Robot.drivetrain.drive(ControlMode.PercentOutput, -0.5, -0.5);
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }

    @Override
    protected void end() {
        Robot.drivetrain.drive(ControlMode.PercentOutput, 0, 0);
    }
}
