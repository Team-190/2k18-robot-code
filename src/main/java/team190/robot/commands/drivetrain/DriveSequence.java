package team190.robot.commands.drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.DriverStation;
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
public class DriveSequence extends Command {
    private EncoderFollower leftFollower;
    private EncoderFollower rightFollower;

    private AutoSequence sequence;

    private boolean resetSensors;

    private boolean isFinished;

    public DriveSequence(AutoSequence sequence) {
        this(sequence, true);
    }

    public DriveSequence(AutoSequence sequence, boolean resetSensors) {
        requires(Robot.drivetrain);
        this.sequence = sequence;
        this.resetSensors = resetSensors;
    }

    @Override
    protected void initialize() {
        int leftPos = Robot.drivetrain.getLeftPosition();
        int rightPos = Robot.drivetrain.getRightPosition();
        if (resetSensors) {
            Robot.navx.reset();
            Robot.drivetrain.setPositionZero();
            leftPos = 0;
            rightPos = 0;
        }


        isFinished = false;

        File lFile = new File(sequence.getLeftCSV());
        File rFile = new File(sequence.getRightCSV());
        if (!lFile.exists()) {
            DriverStation.reportWarning(sequence.getLeftCSV() + " not found.", false);
            isFinished = true;
            return;
        }
        if (!rFile.exists()) {
            DriverStation.reportWarning(sequence.getRightCSV() + " not found.", false);
            isFinished = true;
            return;
        }

        Trajectory leftTraj = Pathfinder.readFromCSV(lFile);

        leftFollower = new EncoderFollower(leftTraj);
        leftFollower.configureEncoder(leftPos, (int) Drivetrain.TICKS_PER_REV, Drivetrain.WHEELDIAMETER_FT);
        leftFollower.configurePIDVA(0.9, 0, 0, (1.0 / 16.0), 0);

        Trajectory rightTraj = Pathfinder.readFromCSV(rFile);

        rightFollower = new EncoderFollower(rightTraj);
        rightFollower.configureEncoder(rightPos, (int) Drivetrain.TICKS_PER_REV, Drivetrain.WHEELDIAMETER_FT);
        rightFollower.configurePIDVA(0.9, 0, 0, (1.0 / 16.0), 0);


    }

    @Override
    protected void execute() {
        if (!isFinished) {
            double leftSpeed = leftFollower.calculate(Robot.drivetrain.getLeftPosition());
            double rightSpeed = rightFollower.calculate(Robot.drivetrain.getRightPosition());

            double gyroHeading = -Robot.navx.getAngle();
            double desiredHeading = Pathfinder.r2d(leftFollower.getHeading());

            double angleDifference = Pathfinder.boundHalfDegrees(desiredHeading - gyroHeading);
            double turn = 0.8 * (-1.0 / 80.0) * angleDifference;

            SmartDashboard.putNumber("Desired Heading: ", desiredHeading);
            SmartDashboard.putNumber("Actual Heading: ", gyroHeading);
            SmartDashboard.putNumber("Angle Difference: ", angleDifference);
            SmartDashboard.putNumber("Turn Value: ", turn);

            Robot.drivetrain.drive(ControlMode.PercentOutput, leftSpeed + turn, rightSpeed - turn);
        }
    }

    @Override
    protected boolean isFinished() {
        return isFinished || (leftFollower.isFinished() && rightFollower.isFinished());
    }

    @Override
    protected void end() {
        Robot.drivetrain.drive(ControlMode.PercentOutput, 0, 0);
    }

    @Override
    protected void interrupted() {
        end();
    }

}
