package team190.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import edu.wpi.first.wpilibj.command.Subsystem;
import team190.models.PairedTalonSRX;

/**
 *
 */
public class Elevator extends Subsystem {

    // TODO: Calibrate
    private final static double POT_BOTTOM = 0; // Pot Value
    private final static double POT_TOP_OFFSET = 0; // Pot Value

    // TODO: Correct heights
    // All POS's in INCHES - Height of Intake
    // 0 is bottom of intake travel, POS_MAX is the inches of travel from
    // that 0.
    public final static double POS_INTAKE = 0;
    public final static double POS_CAR = 20;
    public final static double POS_LO = 30;
    public final static double POS_MED = 40;
    public final static double POS_HI = 50;
    public final static double POS_CLIMB = 60;
    public final static double POS_MAX = 60;

    private static final int DEFAULT_TIMEOUT_MS = 0;
    private static final int DEFAULT_PIDX = 0;

    // Moving Average
    private static int NUM_ROLLING_AVG = 25;
    private int[] errorValues;
    private int errorValuesIndex;

    private static final double SPEED_TOLERANCE = 0.0;
    private static final double ERROR_TOLERANCE = 0.0;

    // CAN Channels
    private static final int ELEVATOR_SRX_LEFT = 5,
            ELEVATOR_SRX_RIGHT = 6;

    /*
    int numKeepTrack = 10;
    int allowableError = 50;
    int[] lastTenErrors = new int[numKeepTrack];
    int errpos = 0;*/

    private PairedTalonSRX motor;

    public Elevator() {
        motor = new PairedTalonSRX(ELEVATOR_SRX_LEFT, ELEVATOR_SRX_RIGHT);
        //motor.setInverted(false);

        motor.configSelectedFeedbackSensor(FeedbackDevice.Analog, DEFAULT_PIDX, DEFAULT_TIMEOUT_MS);
        
        motor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, DEFAULT_TIMEOUT_MS);
        motor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, DEFAULT_TIMEOUT_MS);

        // TODO: Tune PID
        motor.configPIDF(DEFAULT_PIDX, DEFAULT_TIMEOUT_MS, 0.1, 0, 0, 0);

        motor.configAllowableClosedloopError(0, DEFAULT_PIDX, DEFAULT_TIMEOUT_MS);

        errorValuesIndex = 0;
        errorValues = new int[NUM_ROLLING_AVG];
    }

    /**
     * Move the elevator to a given position in inches.
     * @param inches The height of the intake.
     */
    public void moveElevator(double inches) {
        double heightScale = inches / POS_MAX;
        double potValue = (POT_TOP_OFFSET * heightScale) + POT_BOTTOM;
        motor.set(ControlMode.Position, potValue);
    }

    public void manualMove(double percent) {
        motor.set(ControlMode.PercentOutput, percent);
    }

    // TODO: confirm that it works
    public boolean inPosition() {
        int thisError = motor.getClosedLoopError(DEFAULT_PIDX);
        int lastError = errorValues[errorValuesIndex % NUM_ROLLING_AVG];
        errorValues[(++errorValuesIndex % NUM_ROLLING_AVG)] = thisError;

        if (errorValuesIndex <= NUM_ROLLING_AVG) return false;

        double sumError = 0;
        for (int e : errorValues) sumError += e;
        double averageError = sumError / errorValues.length;

        return (averageError < ERROR_TOLERANCE && Math.abs(thisError - lastError) < SPEED_TOLERANCE);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}
