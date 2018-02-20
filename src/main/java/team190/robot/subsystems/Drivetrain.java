package team190.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team190.models.PairedTalonSRX;
import team190.robot.commands.drivetrain.ControllerDriveCommand;

/**
 *
 */
public class Drivetrain extends Subsystem {
    public static final double WHEELDIAMETER_FT = 4.0 / 12.0; // 4 inch diameter wheels
    public static final double WHEELCIRCUMFERENCE_FT = Math.PI * WHEELDIAMETER_FT;
    public static final double REV_PER_FT = 1.0 / WHEELCIRCUMFERENCE_FT;
    public static final double TICKS_PER_REV = 1024.0 * 3.0 * (50.0 / 34.0); // Encoder PPR: 256 (*4 for quadrature), Vex: "Encoder output spins at 3x the speed of the output shaft", "then a 50:34 reduction"
    public static final double TICKS_PER_FT = TICKS_PER_REV * REV_PER_FT;
    public static final double HUNDRED_MS_PER_SEC = 10.0;
    private static final int SHIFTER_PCM = 0,
            SHIFTER_FWD_PORT = 0,
            SHIFTER_REV_PORT = 1;
    private static final int DEFAULT_TIMEOUT_MS = 0;
    private static final int DEFAULT_PIDX = 0;
    private AHRS navx;
    private PairedTalonSRX leftPair;
    private PairedTalonSRX rightPair;
    private DoubleSolenoid shifter;

    public Drivetrain() {
        leftPair = new PairedTalonSRX(1, 2);
        leftPair.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, DEFAULT_PIDX, DEFAULT_TIMEOUT_MS);
        leftPair.setInverted(true);

        rightPair = new PairedTalonSRX(3, 4);
        rightPair.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, DEFAULT_PIDX, DEFAULT_TIMEOUT_MS);
        rightPair.setSensorPhase(true);

        shifter = new DoubleSolenoid(SHIFTER_PCM, SHIFTER_FWD_PORT, SHIFTER_REV_PORT);

        navx = new AHRS(SPI.Port.kMXP);

        addChild(leftPair);
        addChild(rightPair);
        addChild(shifter);

        setPositionZero();

    }

    private double feetPerSecToTicksPerHundredMs(double feetPerSec) {
        return feetPerSec * TICKS_PER_FT / HUNDRED_MS_PER_SEC;
    }

    private double feetToTicks(double feet) {
        return feet * TICKS_PER_FT;
    }

    private double TicksPerHundredMsToFeetPerSec(double ticksPerHundredMs) {
        return ticksPerHundredMs / (TICKS_PER_FT / HUNDRED_MS_PER_SEC);
    }

    private double TicksToFeet(double ticks) {
        return ticks / TICKS_PER_FT;
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

    public int getLeftPosition() {
        return leftPair.getSelectedSensorPosition(DEFAULT_PIDX);
    }

    public int getRightPosition() {
        return rightPair.getSelectedSensorPosition(DEFAULT_PIDX);
    }

    public int getLeftVelocity() {
        return leftPair.getSelectedSensorVelocity(DEFAULT_PIDX);
    }

    public int getRightVelocity() {
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
        SmartDashboard.putNumber("Left Encoder Pos", TicksToFeet(getLeftPosition()));
        SmartDashboard.putNumber("Right Encoder Pos", TicksToFeet(getRightPosition()));
        SmartDashboard.putNumber("Left Encoder Vel", TicksPerHundredMsToFeetPerSec(getLeftVelocity()));
        SmartDashboard.putNumber("Right Encoder Vel", TicksPerHundredMsToFeetPerSec(getRightVelocity()));
        SmartDashboard.putNumber("Gyro Heading", navx.getAngle());
    }

    public void resetNavx() {
        navx.reset();
    }

    public double getAngle() {
        return navx.getAngle();
    }

    public enum Gear {
        HIGH, LOW
    }

}

