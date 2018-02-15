package team190.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import team190.models.PairedTalonSRX;

/**
 *
 */
public class Elevator extends Subsystem {
    private static final int DEFAULT_TIMEOUT_MS = 0;
    private static final int DEFAULT_PIDX = 0;

    // CAN Channels
    private static final int ELEVATOR_SRX_LEFT = 5,
            ELEVATOR_SRX_RIGHT = 6;

    // TODO: change exact values
    public final static double GROUND = 0 * (1023 / 96);
    public final static double REST = 20 * (1023 / 96);
    public final static double FIVEFT = 60 * (1023 / 96);
    public final static double SIXFT = 72 * (1023 / 96);
    public final static double SEVENFT = 84 * (1023 / 96);
    public final static double MAX = 96 * (1023 / 96);

    // TODO: change channels
    private PairedTalonSRX motor;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public Elevator() {
        motor = new PairedTalonSRX(ELEVATOR_SRX_LEFT, ELEVATOR_SRX_RIGHT);
        motor.setInverted(false);

        // TODO more srx setup here?
        motor.configSelectedFeedbackSensor(FeedbackDevice.Analog, DEFAULT_PIDX, DEFAULT_TIMEOUT_MS);

        motor.configNominalOutputForward(0, DEFAULT_TIMEOUT_MS);
        motor.configNominalOutputReverse(0, DEFAULT_TIMEOUT_MS);
        motor.configPeakOutputForward(1, DEFAULT_TIMEOUT_MS);
        motor.configPeakOutputReverse(-1, DEFAULT_TIMEOUT_MS);

        motor.configPIDF(DEFAULT_PIDX, DEFAULT_TIMEOUT_MS, 0.1, 0, 0, 0);

        motor.configAllowableClosedloopError(0, DEFAULT_PIDX, DEFAULT_TIMEOUT_MS);
    }

    public void moveElevator(double height) {
        motor.set(ControlMode.Position, height);
    }

    public void manualMove(double percent) {
        motor.set(ControlMode.PercentOutput, percent);
    }

    int numKeepTrack = 10;
    int allowableError = 50;
    int[] lastTenErrors = new int[numKeepTrack];
    int errpos = 0;

    public boolean inPosition() {
        int err = motor.getClosedLoopError(DEFAULT_PIDX);
        lastTenErrors[errpos % numKeepTrack] = err;
        errpos++;

        if (errpos < numKeepTrack) return false;

        double average = 0;
        for (int e : lastTenErrors) {
            average += e;
            average += e;
        }
        average /= numKeepTrack;

        if (average <= allowableError) return true;
        else return false;
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}
