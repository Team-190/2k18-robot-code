package team190.robot.subsystems;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team190.models.PairedTalonSRX;
import team190.robot.commands.ControllerDriveCommand;

/**
 *
 */
public class Drivetrain extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    // Shifting
    public static final int LOW_GEAR_PROFILE = 0;
    public static final int HIGH_GEAR_PROFILE = 1;
    public static final int SHIFTER_PCM = 0,
            SHIFTER_FWD_PORT = 0,
    SHIFTER_REV_PORT = 1;
    private static final int DEFAULT_TIMEOUT_MS = 0;
    private static final int DEFAULT_PIDX = 0;
    // Motion Profiling
    private static final int kMinPointsInTalon = 5;

    private static final int TRAJECTORY_PERIOD_MS = 10;
    private static final int TRAJECTORY_PERIOD_SEC = TRAJECTORY_PERIOD_MS / 1000;
    private static final int DOWNLOAD_PERIOD_MS = TRAJECTORY_PERIOD_MS / 2; // Download points at twice the speed
    public static final double DOWNLOAD_PERIOD_SEC = DOWNLOAD_PERIOD_MS / 1000;

    private final PairedTalonSRX leftPair = new PairedTalonSRX(3, 1);
    private final PairedTalonSRX rightPair = new PairedTalonSRX(2, 0);
    private DoubleSolenoid shifter = new DoubleSolenoid(SHIFTER_PCM, SHIFTER_FWD_PORT, SHIFTER_REV_PORT);

    public Drivetrain() {
        shift(Gear.LOW);
        //leftPair.setInverted(false);
        leftPair.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, DEFAULT_PIDX, DEFAULT_TIMEOUT_MS);
        //leftPair.setSensorPhase(false);

        rightPair.setInverted(true);
        rightPair.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, DEFAULT_PIDX, DEFAULT_TIMEOUT_MS);
        //rightPair.setSensorPhase(true);

        setCoastMode();

        // TODO: put in the actual PIDF values
        // configPIDF(LOW_GEAR_PROFILE, 0, 0, 0, 0);
        // configPIDF(HIGH_GEAR_PROFILE, 0, 0, 0, 0);

        setPositionZero();
    }

    public void drive(ControlMode controlMode, double left, double right) {
        leftPair.set(controlMode, left);
        rightPair.set(controlMode, right);
        updateSmartDashboard();
    }

    public void setBrakeMode() {
        leftPair.setNeutralMode(NeutralMode.Brake);
        rightPair.setNeutralMode(NeutralMode.Brake);
    }

    public void setCoastMode() {
        leftPair.setNeutralMode(NeutralMode.Coast);
        rightPair.setNeutralMode(NeutralMode.Coast);
    }

    public void initDefaultCommand() {
        setDefaultCommand(new ControllerDriveCommand());
    }

    public void configPIDF(int profile, double P, double I, double D, double F) {
        leftPair.configPIDF(profile, DEFAULT_TIMEOUT_MS, P, I, D, F);
        rightPair.configPIDF(profile, DEFAULT_TIMEOUT_MS, P, I, D, F);
    }

    public double getLeftPosition() {
        return leftPair.getSelectedSensorPosition(DEFAULT_PIDX);
    }

    public double getRightPosition() {
        return rightPair.getSelectedSensorPosition(DEFAULT_PIDX);
    }

    public double getLeftVelocity() {
        return leftPair.getSelectedSensorVelocity(DEFAULT_PIDX);
    }

    public double getRightVelocity() {
        return rightPair.getSelectedSensorVelocity(DEFAULT_PIDX);
    }

    public void setPositionZero() {
        leftPair.setSelectedSensorPosition(0, DEFAULT_PIDX, DEFAULT_TIMEOUT_MS);
        rightPair.setSelectedSensorPosition(0, DEFAULT_PIDX, DEFAULT_TIMEOUT_MS);
        updateSmartDashboard();
    }

    //Shifts gear
    public void shift(Gear gear) {
        if (gear.equals(Gear.HIGH)) {
            shifter.set(DoubleSolenoid.Value.kForward);
        } else if (gear.equals(Gear.LOW)) {
            shifter.set(DoubleSolenoid.Value.kReverse);
        }
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Left Encoder Pos", getLeftPosition());
        SmartDashboard.putNumber("Right Encoder Pos", getRightPosition());
        SmartDashboard.putNumber("Left Encoder Vel", getLeftVelocity());
        SmartDashboard.putNumber("Right Encoder Vel", getRightVelocity());
    }

    // MOTION PROFILING METHODS


    // called before entering motion profiling
    public void prepareMotionProfiling() {
        shift(Gear.HIGH);
        setBrakeMode();
        leftPair.clearMotionProfileTrajectories();
        rightPair.clearMotionProfileTrajectories();

        configUpdateRate();

        drive(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value, SetValueMotionProfile.Disable.value);
    }

    public void configUpdateRate() {
        leftPair.configMotionProfileTrajectoryPeriod(TRAJECTORY_PERIOD_MS, DEFAULT_TIMEOUT_MS);
        rightPair.configMotionProfileTrajectoryPeriod(TRAJECTORY_PERIOD_MS, DEFAULT_TIMEOUT_MS);
        leftPair.changeMotionControlFramePeriod(DOWNLOAD_PERIOD_MS);
        rightPair.changeMotionControlFramePeriod(DOWNLOAD_PERIOD_MS);
    }

    // take your trajectory and stream it the srx's
    public void fillMotionProfilingBuffer(TrajectoryPoint[] left, TrajectoryPoint[] right) {
        processTrajectory(leftPair, left);
        processTrajectory(rightPair, right);
    }

    private void processTrajectory(PairedTalonSRX pair, TrajectoryPoint[] points) {
        for (TrajectoryPoint point : points) {
            pair.pushMotionProfileTrajectory(point);
        }
    }

    public SetValueMotionProfile getMotionProfileValue() {
        MotionProfileStatus rightStatus = new MotionProfileStatus();
        MotionProfileStatus leftStatus = new MotionProfileStatus();
        leftPair.getMotionProfileStatus(leftStatus);
        rightPair.getMotionProfileStatus(rightStatus);
        if (leftStatus.isUnderrun || rightStatus.isUnderrun)
            return SetValueMotionProfile.Disable;
        else if (leftStatus.btmBufferCnt > kMinPointsInTalon && rightStatus.btmBufferCnt > kMinPointsInTalon)
            return SetValueMotionProfile.Enable;
        else if (leftStatus.activePointValid && leftStatus.isLast && rightStatus.activePointValid && rightStatus.isLast)
            return SetValueMotionProfile.Hold;
        else
            return SetValueMotionProfile.Disable;
    }

    public void processMotionProfilingBuffer() {
        leftPair.processMotionProfileBuffer();
        rightPair.processMotionProfileBuffer();
    }

    // clean up all the motion profiling things
    public void endMotionProfiling() {
        leftPair.clearMotionProfileTrajectories();
        rightPair.clearMotionProfileTrajectories();
        drive(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value, SetValueMotionProfile.Disable.value);
        drive(ControlMode.PercentOutput, 0, 0);
        setCoastMode();
        shift(Gear.LOW);
    }

    public enum Gear {
        HIGH, LOW;
    }

}

