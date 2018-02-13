package team190.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

    private AutoSequence sequence;

    private boolean resetNavx;

    public DriveDistance(AutoSequence sequence) {
        this(sequence, true);
    }

    public DriveDistance(AutoSequence sequence, boolean resetNavx) {
        requires(Robot.drivetrain);
        this.sequence = sequence;
        this.resetNavx = resetNavx;
    }

    @Override
    protected void initialize() {
        Robot.drivetrain.setBrakeMode();
        if (resetNavx) Robot.navx.reset();

        Trajectory leftTraj = Pathfinder.readFromCSV(new File(sequence.getLeftCSV()));
        leftFollower = new EncoderFollower(leftTraj);
        leftFollower.configureEncoder(0, (int) Drivetrain.TICKS_PER_REV, Drivetrain.WHEELDIAMETER_FT);
        leftFollower.configurePIDVA(0.9, 0, 0, (1.0/16.0), 0);

        Trajectory rightTraj = Pathfinder.readFromCSV(new File(sequence.getRightCSV()));
        rightFollower = new EncoderFollower(rightTraj);
        rightFollower.configureEncoder(0, (int) Drivetrain.TICKS_PER_REV, Drivetrain.WHEELDIAMETER_FT);
        rightFollower.configurePIDVA(0.9, 0, 0, (1.0/16.0), 0);

        Robot.drivetrain.setPositionZero();
    }

    @Override
    protected void execute() {
        double leftSpeed = leftFollower.calculate((int) Robot.drivetrain.getLeftPosition());
        double rightSpeed = rightFollower.calculate((int) Robot.drivetrain.getRightPosition());

        double kG = 0.8 * (1.0/80.0);

        double gyroHeading = Robot.navx.getAngle();
        double desiredHeading = Pathfinder.r2d(-leftFollower.getHeading());
        double angle_delta = Pathfinder.boundHalfDegrees(desiredHeading - gyroHeading);
        double turn = kG * angle_delta;


        SmartDashboard.putNumber("Desired Heading: ", Pathfinder.r2d(leftFollower.getHeading()));
        SmartDashboard.putNumber("Actual Heading: ", Robot.navx.getAngle());
        SmartDashboard.putNumber("Turn Value: ", turn);

        Robot.drivetrain.drive(ControlMode.PercentOutput, leftSpeed + turn, rightSpeed - turn);
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
