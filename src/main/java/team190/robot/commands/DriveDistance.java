package team190.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;
import team190.models.AutoSequence;
import team190.robot.Robot;
import team190.robot.subsystems.Drivetrain;

import java.io.File;

/**
 * Created by Kevin O'Brien on 2/10/2018.
 */
public class DriveDistance extends Command {
    private EncoderFollower leftFollower;
    private EncoderFollower rightFollower;

    public DriveDistance() {
        requires(Robot.drivetrain);
    }

    @Override
    protected void initialize() {
        Robot.drivetrain.setBrakeMode();

        Trajectory leftTraj = Pathfinder.readFromCSV(new File(AutoSequence.ForwardTenFeet.getLeftCSV()));
        leftFollower = new EncoderFollower(leftTraj);
        leftFollower.configureEncoder(0, (int) Drivetrain.TICKS_PER_REV, Drivetrain.WHEELDIAMETER_FT);
        leftFollower.configurePIDVA(0.9, 0, 0, (1.0/16.0), 0);

        Trajectory rightTraj = Pathfinder.readFromCSV(new File(AutoSequence.ForwardTenFeet.getRightCSV()));
        rightFollower = new EncoderFollower(rightTraj);
        rightFollower.configureEncoder(0, (int) Drivetrain.TICKS_PER_REV, Drivetrain.WHEELDIAMETER_FT);
        rightFollower.configurePIDVA(0.9, 0, 0, (1.0/16.0), 0);


        Robot.drivetrain.setPositionZero();
    }

    @Override
    protected void execute() {
        double leftSpeed = leftFollower.calculate((int) Robot.drivetrain.getLeftPosition());
        double rightSpeed = rightFollower.calculate((int) Robot.drivetrain.getRightPosition());
        leftFollower.getHeading();
        rightFollower.getHeading();
        Robot.drivetrain.drive(ControlMode.PercentOutput, leftSpeed, rightSpeed);

    }

    @Override
    protected boolean isFinished() {
        return leftFollower.isFinished() && rightFollower.isFinished();
    }

    @Override
    protected void  end() {
        Robot.drivetrain.drive(ControlMode.PercentOutput, 0, 0);
    }

    @Override
    protected void interrupted() {
        end();
    }


}
