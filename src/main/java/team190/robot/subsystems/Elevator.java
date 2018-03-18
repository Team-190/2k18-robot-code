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

    // TODO: Correct heights
    // All POS's in INCHES - Height of Intake
    // 0 is bottom of intake travel, POS_MAX is the inches of travel from
    // that 0.
    public final static double POS_INTAKE = 5;
    public final static double POS_CAR = 15; // POT val 283
    public final static double POS_LO = 30;
    public final static double POS_MED = 50;
    public final static double POS_HI = 70;
    public final static double POS_CLIMB = 85;
    public final static double POS_MAX = 90;

    private final static double POT_BOTTOM = 116; // Pot Value
    private final static double POT_TOP_OFFSET = 560; // Pot Value

    private static final int DEFAULT_TIMEOUT_MS = 0;
    private static final int DEFAULT_PIDX = 0;

    private static final double SPEED_TOLERANCE = 0.5;
    private static final double ERROR_TOLERANCE = 10.0;

    // CAN Channels
    private static final int ELEVATOR_SRX_LEFT = 5,
            ELEVATOR_SRX_RIGHT = 6;

    // Moving Average
    private static int NUM_ROLLING_AVG = 25;
    private double[] errorValues;
    private int errorValuesIndex;
    private PairedTalonSRX motor;
    private int numLoops;

    public Elevator() {
        motor = new PairedTalonSRX(ELEVATOR_SRX_LEFT, ELEVATOR_SRX_RIGHT);
        motor.setInverted(true);

        motor.configSelectedFeedbackSensor(FeedbackDevice.Analog, DEFAULT_PIDX, DEFAULT_TIMEOUT_MS);
        motor.setSensorPhase(true);

        motor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, DEFAULT_TIMEOUT_MS);
        motor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, DEFAULT_TIMEOUT_MS);

        // TODO: Tune PID
        motor.configPIDF(DEFAULT_PIDX, DEFAULT_TIMEOUT_MS, 16.0, 0, 0, 0);

        motor.configAllowableClosedloopError(0, DEFAULT_PIDX, DEFAULT_TIMEOUT_MS);

        errorValuesIndex = 0;
        errorValues = new double[NUM_ROLLING_AVG];
    }

    public void log() {
        SmartDashboard.putNumber("Elevator Pot Position", motor.getSelectedSensorPosition(DEFAULT_PIDX));
        SmartDashboard.putNumber("Elevator Pot Speed", motor.getSelectedSensorVelocity(DEFAULT_PIDX));
    }

    /**
     * Move the elevator to a given position in inches.
     *
     * @param inches The height of the intake.
     */
    public void moveElevator(double inches) {
        double heightScale = inches / POS_MAX;
        double potValue = (POT_TOP_OFFSET * heightScale) + POT_BOTTOM;
        motor.set(ControlMode.Position, potValue);
        SmartDashboard.putNumber("Elev Setpoint", potValue);
        numLoops = 0;
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

    // TODO: confirm that it works
    public boolean inPosition() {
        boolean result = false;
        numLoops++;
        double thisError = motor.getClosedLoopError(DEFAULT_PIDX);
        System.out.println("thisError = " + thisError);
        boolean onTarget = Math.abs(thisError) <= ERROR_TOLERANCE;
        return numLoops >= 15 && onTarget;
        /*
        double lastError = errorValues[errorValuesIndex];

        errorValuesIndex = (errorValuesIndex  + 1) % NUM_ROLLING_AVG;
        errorValues[errorValuesIndex] = Math.abs(thisError);

        double sumError = 0;
        for (doublee : errorValues) sumError += e;

        double averageError = sumError / errorValues.length;
        double speedError = Math.abs(thisError - lastError);

        result = (averageError < ERROR_TOLERANCE);
        boolean speedBool = speedError < SPEED_TOLERANCE;

        SmartDashboard.putBoolean("Elevator in pos, p", result);
        SmartDashboard.putBoolean("Elevator in pos, s", speedBool);
        SmartDashboard.putNumber("Elevator Pos Error", averageError);
        SmartDashboard.putNumber("Elevator Spd Error", speedError);
        System.out.println("Elevator in pos, p = " + result);
        System.out.println("Elevator in pos, s = " + speedBool);
        System.out.println("Elevator Pos Error = " + averageError);
        System.out.println("Elevator Spd Error = " + speedError);
        return result && speedBool;*/
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}
