package team190.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Collector extends Subsystem {

    // CAN Channels
    private static final int SRX_LEFT = 7, SRX_RIGHT = 8;
    private static final boolean INVERT_LEFT = true, INVERT_RIGHT = false;
    // DIO inputs
    private static final int INTAKE_CUBE_SENSOR_PORT = 1;

    private static final int INTAKE_JAM_SENSOR_PORT = 3;

    private TalonSRX left, right;

    // Anti-jam limit switch
    private DigitalInput jamSensor;

    // Banner sensor
    private DigitalInput cubeSensor;

    public Collector() {
        left = new TalonSRX(SRX_LEFT);
        left.setInverted(INVERT_LEFT);

        right = new TalonSRX(SRX_RIGHT);
        right.setInverted(INVERT_RIGHT);
        right.follow(left);

        cubeSensor = new DigitalInput(INTAKE_CUBE_SENSOR_PORT);
        jamSensor = new DigitalInput(INTAKE_JAM_SENSOR_PORT);
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void move(IntakeMode mode) {
        double percent = 0;
        if (mode == IntakeMode.Stop) {
            percent = 0.0;
            right.follow(left);
        } else if (mode == IntakeMode.Intake) {
            percent = 0.50;
            right.follow(left);
        } else if (mode == IntakeMode.Extake) {
            percent = -0.3;
        } else if (mode == IntakeMode.Transfer) {
            percent = 0.6;
        } else if (mode == IntakeMode.ExtakeGround) {
            percent = -1.0;
        }

        left.set(ControlMode.PercentOutput, percent);
    }

    public void antiJam() {
        left.set(ControlMode.PercentOutput, 0.4);
        right.set(ControlMode.PercentOutput, -0.4);
    }

    public boolean hasCube() {
        return !cubeSensor.get();
    }

    public boolean isJammed() {
        return jamSensor.get();
    }

    public void log() {
        SmartDashboard.putNumber("Intake Left Speed", left.getMotorOutputPercent());
        SmartDashboard.putNumber("Intake Right Speed", right.getMotorOutputPercent());
        SmartDashboard.putBoolean("Intake Has Cube", hasCube());
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    public enum IntakeMode {
        Stop, Intake, Extake, ExtakeGround, Transfer
    }
}
