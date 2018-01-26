package team190.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import team190.models.PairedTalonSRX;
import team190.robot.commands.ControllerDriveCommand;

/**
 *
 */
public class Drivetrain extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public static final int LOW_GEAR_PROFILE = 0;
    public static final int HIGH_GEAR_PROFILE = 1;
    public static final int SHIFTER_PDM = 0,
            SHIFTER_PORT = 0;
    private static final int DEFAULT_TIMEOUT_MS = 10;
    private PairedTalonSRX leftPair;
    private PairedTalonSRX rightPair;
    private Solenoid shifter;

    public Drivetrain() {
        leftPair = new PairedTalonSRX(0, 1);
        rightPair = new PairedTalonSRX(2, 3);

        shifter = new Solenoid(SHIFTER_PDM, SHIFTER_PORT);

        this.leftPair.setInverted(false);
        this.leftPair.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, DEFAULT_TIMEOUT_MS);
        this.leftPair.setSensorPhase(false);

        this.rightPair.setInverted(true);
        this.rightPair.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, DEFAULT_TIMEOUT_MS);
        this.rightPair.setSensorPhase(true);
    }

    public void initDefaultCommand() {
        setDefaultCommand(new ControllerDriveCommand());
    }

    //Shifts gear
    public void shift(Gear gear) {
        if (gear.equals(Gear.HIGH)) {
            shifter.set(true);
        } else if (gear.equals(Gear.LOW)) {
            shifter.set(false);
        }
    }

    public void drive(ControlMode controlMode, double left, double right) {
        this.leftPair.set(controlMode, left);
        this.rightPair.set(controlMode, right);
    }

    public enum Gear {
        HIGH, LOW;
    }

}

