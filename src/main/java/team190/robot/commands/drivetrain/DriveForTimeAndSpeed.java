package team190.robot.commands.drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import team190.robot.Robot;
import team190.robot.subsystems.Drivetrain;

public class DriveForTimeAndSpeed extends Command {
    private double runTime;
    private double speed;

    public DriveForTimeAndSpeed(double timeToRun) {
        this(-0.5, timeToRun);
    }

    public DriveForTimeAndSpeed(double speed, double timeToRun) {
        requires(Robot.drivetrain);
        runTime = timeToRun;
        this.speed = speed;
    }

    @Override
    protected void initialize() {
        Robot.drivetrain.shift(Drivetrain.Gear.LOW);
        setTimeout(runTime);
    }

    @Override
    protected void execute() {
        Robot.drivetrain.drive(ControlMode.PercentOutput, speed, speed);
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
