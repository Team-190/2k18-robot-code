package team190.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team190.robot.Robot;
import team190.robot.models.PairedTalonSRX;

/**
 *
 */
public class Elevator extends Subsystem {

    // All POS's in INCHES - Height of Intake
    // 0 is bottom of intake travel, POS_MAX is the inches of travel from
    // that 0.
    public final static double POS_INTAKE = 0;
    public final static double POS_CAR = 28;
    public final static double POS_SWITCH = 50;
    public final static double POS_MED = 78;
    public final static double POS_HI = 92; // MAX height
    private final static double POS_MAX = 90;

    private static double POT_BOTTOM = 271; // Pot Value
    private final static double POT_TOP_OFFSET = 470; // Pot Value

    private static final int DEFAULT_TIMEOUT_MS = 0;
    private static final int DEFAULT_PIDX = 0;

    private static final double ERROR_TOLERANCE = 10.0;

    // CAN Channels
    private static final int ELEVATOR_SRX_LEFT = 5,
            ELEVATOR_SRX_RIGHT = 6;

    private PairedTalonSRX motor;
    private double motorSetpoint;

    private final String POT_KEY = "ELEVATOR_BOTTOM";

    public Elevator() {
        POT_BOTTOM = Preferences.getInstance().getDouble(POT_KEY, POT_BOTTOM);
        DriverStation.reportWarning("Elevator Pot Bottom set to: " + POT_BOTTOM, false);

        motor = new PairedTalonSRX(ELEVATOR_SRX_LEFT, ELEVATOR_SRX_RIGHT);
        motor.setInverted(true);

        motor.configSelectedFeedbackSensor(FeedbackDevice.Analog, DEFAULT_PIDX, DEFAULT_TIMEOUT_MS);
        motor.setSensorPhase(true);

        motor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, DEFAULT_TIMEOUT_MS);
        motor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, DEFAULT_TIMEOUT_MS);

        motor.configPIDF(DEFAULT_PIDX, DEFAULT_TIMEOUT_MS, 35, 0, 0, 0);

        motor.configAllowableClosedloopError(0, DEFAULT_PIDX, DEFAULT_TIMEOUT_MS);

        motorSetpoint = POT_BOTTOM;
    }

    public void log() {
        double potValue = motor.getSelectedSensorPosition(DEFAULT_PIDX);
        SmartDashboard.putNumber("Elevator Pot Position", potValue);
        SmartDashboard.putNumber("Elevator Pot Speed", motor.getSelectedSensorVelocity(DEFAULT_PIDX));
        SmartDashboard.putNumber("Elevator Height", potToInches(potValue));
        SmartDashboard.putBoolean("Elevator Limit", getBottomLimitSwitch());
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
     * Move the elevator at a certain percent vbus.
     *
     * @param percent The percent vbus for the elevator motor.
     */
    public void manualMove(double percent) {
        motor.set(ControlMode.PercentOutput, percent);
    }

    public void stop() {
        motor.set(ControlMode.PercentOutput, 0);
    }

    public boolean inPosition() {
        double thisError = motorSetpoint - motor.getSelectedSensorPosition(DEFAULT_PIDX);
        boolean inPos = Math.abs(thisError) <= ERROR_TOLERANCE;
        SmartDashboard.putBoolean("Elevator in pos", inPos);
        return inPos;
    }

    public boolean getBottomLimitSwitch() {
        return motor.getSensorCollection().isRevLimitSwitchClosed();
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    /**
     * Zero the potentiometer if the difference between the old value and new value is greater than ERROR_TOLERANCE.
     * Currently only called from the ElevatorPositionIntake command when the limit switch is pressed.
     */
    public void zeroPot() {
        double position = motor.getSelectedSensorPosition(DEFAULT_PIDX);
        double difference = Math.abs(POT_BOTTOM - position);
        if (difference > ERROR_TOLERANCE) {
            POT_BOTTOM = position;
            Preferences.getInstance().putDouble(POT_KEY, POT_BOTTOM);
            DriverStation.reportWarning("New Elevator Pot Bottom: " + POT_BOTTOM, false);
        }
    }

    /**
     * Periodic method - checks if the POT_BOTTOM has been set over NetworkTables and updates accordingly.
     */
    public void periodic() {
        double prefValue = Preferences.getInstance().getDouble(POT_KEY, POT_BOTTOM);
        if (prefValue != POT_BOTTOM) {
            POT_BOTTOM = prefValue;
        }
    }
}
