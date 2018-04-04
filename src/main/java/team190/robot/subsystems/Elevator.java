package team190.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team190.models.PairedTalonSRX;
import team190.robot.Robot;

/**
 *
 */
public class Elevator extends Subsystem {

    // TODO: re-do heights
    // All POS's in INCHES - Height of Intake
    // 0 is bottom of intake travel, POS_MAX is the inches of travel from
    // that 0.
    public final static double POS_INTAKE = 0;
    public final static double POS_CAR = 28;
    public final static double POS_LO = 0;
    public final static double POS_SWITCH = 50;
    public final static double POS_MED = 72;
    public final static double POS_HI = 95; // MAX height
    public final static double POS_CLIMB = 80;
    public final static double POS_MAX = 90;

    private final static double POT_BOTTOM = 266; // Pot Value
    private final static double POT_TOP_OFFSET = 572; // Pot Value

    private static final int DEFAULT_TIMEOUT_MS = 0;
    private static final int DEFAULT_PIDX = 0;

    private static final double SPEED_TOLERANCE = 0.5;
    private static final double ERROR_TOLERANCE = 8.0;

    // CAN Channels
    private static final int ELEVATOR_SRX_LEFT = 5,
            ELEVATOR_SRX_RIGHT = 6;

    private PairedTalonSRX motor;
    private double motorSetpoint;

    public Elevator() {
        motor = new PairedTalonSRX(ELEVATOR_SRX_LEFT, ELEVATOR_SRX_RIGHT);
        motor.setInverted(true);

        motor.configSelectedFeedbackSensor(FeedbackDevice.Analog, DEFAULT_PIDX, DEFAULT_TIMEOUT_MS);
        motor.setSensorPhase(true);

        motor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, DEFAULT_TIMEOUT_MS);
        motor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, DEFAULT_TIMEOUT_MS);

        // TODO: Tune PID
        motor.configPIDF(DEFAULT_PIDX, DEFAULT_TIMEOUT_MS, 20, 0, 0, 0);

        motor.configAllowableClosedloopError(0, DEFAULT_PIDX, DEFAULT_TIMEOUT_MS);

        motorSetpoint = POT_BOTTOM;
    }

    public void log() {
        double potValue = motor.getSelectedSensorPosition(DEFAULT_PIDX);
        SmartDashboard.putNumber("Elevator Pot Position", potValue);
        SmartDashboard.putNumber("Elevator Pot Speed", motor.getSelectedSensorVelocity(DEFAULT_PIDX));
        SmartDashboard.putNumber("Elevator Height", potToInches(potValue));
    }

    /**
     * Move the elevator to a given position in inches.
     *
     * @param inches The height of the intake.
     */
    public void moveElevator(double inches) {
        motorSetpoint = heightToPot(inches);
        SmartDashboard.putNumber("Elev Setpoint", motorSetpoint);

        motor.set(ControlMode.Position, motorSetpoint);
    }

    private double heightToPot(double inches) {
        double heightScale = inches / POS_MAX;
        return (POT_TOP_OFFSET * heightScale) + POT_BOTTOM;
    }

    private double potToInches(double potValue) {
        return ((potValue - POT_BOTTOM) / POT_TOP_OFFSET) * POS_MAX;
    }

    /**
     * Move the elevator at a certain percent vbus, only if in manual mode.
     *
     * @param percent The percent vbus for the elevator motor.
     */
    public void manualMove(double percent) {
        if (Robot.m_oi.isElevatorManual()) {
            motor.set(ControlMode.PercentOutput, percent);
        }
    }

    public void stop() {
        motor.set(ControlMode.PercentOutput, 0);
    }

    public boolean inPosition() {
        double thisError = motorSetpoint - motor.getSelectedSensorPosition(DEFAULT_PIDX);
        return Math.abs(thisError) <= ERROR_TOLERANCE;
    }

    public boolean getBottomLimitSwitch() {
        return motor.getSensorCollection().isFwdLimitSwitchClosed();
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}
