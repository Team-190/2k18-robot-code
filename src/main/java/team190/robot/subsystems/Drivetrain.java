package team190.robot.subsystems;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
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
    private static final double TRAJECTORY_PERIOD_SEC = (double)TRAJECTORY_PERIOD_MS / 1000;
    public static final int DOWNLOAD_PERIOD_MS = TRAJECTORY_PERIOD_MS / 2; // Download points at twice the speed
    public static final double DOWNLOAD_PERIOD_SEC = (double)DOWNLOAD_PERIOD_MS / 1000;

    public static double WHEELDIAMETER_FT = 4.0 / 12.0; // 4 inch diameter wheels
    public static double WHEELCIRCUMFERENCE_FT = Math.PI * WHEELDIAMETER_FT;
    public static double REV_PER_FT = 1.0 / WHEELCIRCUMFERENCE_FT;
    public static double TICKS_PER_REV = 1024.0 * 3.0 * (50.0 / 34.0); // Encoder PPR: 256 (*4 for quadrature), Vex: "Encoder output spins at 3x the speed of the output shaft", "then a 50:34 reduction"
    public static double TICKS_PER_FT = TICKS_PER_REV * REV_PER_FT;
    public static double HUNDRED_MS_PER_SEC = 10.0;

    private final PairedTalonSRX leftPair = new PairedTalonSRX(0, 2);
    private final PairedTalonSRX rightPair = new PairedTalonSRX(6, 5);
    private DoubleSolenoid shifter = new DoubleSolenoid(SHIFTER_PCM, SHIFTER_FWD_PORT, SHIFTER_REV_PORT);

    public Drivetrain() {
        shift(Gear.LOW);
        leftPair.setInverted(true);
        leftPair.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, DEFAULT_PIDX, DEFAULT_TIMEOUT_MS);
        //leftPair.setSensorPhase(false);

        //rightPair.setInverted(false);
        rightPair.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, DEFAULT_PIDX, DEFAULT_TIMEOUT_MS);
        rightPair.setSensorPhase(true);
        //rightPair.setSensorPhase(true);

        setCoastMode();

        // TODO: put in the actual PIDF values
        // configPIDF(LOW_GEAR_PROFILE, 0, 0, 0, 0);
        configPIDF(HIGH_GEAR_PROFILE, 0, 0, 0, 0.1425);

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
        System.out.println("Loading the points into the top level buffer");
        for (int i = 0; i < points.length; i++) {
            TrajectoryPoint point = points[i];
            StringBuilder sb = new StringBuilder();
            sb.append("Loading point :" + i);
            sb.append(" Time duration: " + point.timeDur.name());
            sb.append(" PIDF Slot: " + point.profileSlotSelect0);
            sb.append(" Velocity Native: " + point.velocity);
            sb.append(" Velocity ft/s: " + TicksPerHundredMsToFeetPerSec(point.velocity));
            sb.append(" Position Native: " + point.position);
            sb.append(" Positiion ft: " + TicksToFeet(point.position));
            sb.append(" Zero: " + point.zeroPos);
            sb.append(" Last: " + point.isLastPoint);
            System.out.println(sb);
            pair.pushMotionProfileTrajectory(point);
        }
    }

    public SetValueMotionProfile getMotionProfileValue() {
        MotionProfileStatus rightStatus = new MotionProfileStatus();
        MotionProfileStatus leftStatus = new MotionProfileStatus();
        leftPair.getMotionProfileStatus(leftStatus);
        rightPair.getMotionProfileStatus(rightStatus);
        if (leftStatus.isUnderrun || rightStatus.isUnderrun) {
            return SetValueMotionProfile.Disable;
        }
        else if (leftStatus.btmBufferCnt > kMinPointsInTalon && rightStatus.btmBufferCnt > kMinPointsInTalon) {
            return SetValueMotionProfile.Enable;
        }

        else if (leftStatus.activePointValid && leftStatus.isLast && rightStatus.activePointValid && rightStatus.isLast) {
            return SetValueMotionProfile.Hold;
        }

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

    public static double feetPerSecToTicksPerHundredMs(double feetPerSec) {
        return feetPerSec * TICKS_PER_FT / HUNDRED_MS_PER_SEC;
    }

    public static double feetToTicks(double feet) {
        return feet * TICKS_PER_FT;
    }

    public static double TicksPerHundredMsToFeetPerSec(double ticksPerHundredMs) {
        return ticksPerHundredMs / (TICKS_PER_FT / HUNDRED_MS_PER_SEC);
    }

    public static double TicksToFeet(double ticks) {
        return ticks / TICKS_PER_FT;
    }

    public enum Gear {
        HIGH, LOW;
    }

}

