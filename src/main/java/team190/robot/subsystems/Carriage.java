package team190.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Carriage extends Subsystem {

    //TODO: channels
    private static final int CARRIAGE_SRX = 9;
    private static final int CARRIAGE_CUBE_SENSOR_PORT = 2;
    private DigitalInput cubeSensor = new DigitalInput(CARRIAGE_CUBE_SENSOR_PORT);
    private TalonSRX mainMotor;

    public Carriage() {
        mainMotor = new TalonSRX(CARRIAGE_SRX);
        mainMotor.setInverted(false);
    }

    public void move(CarriageMode mode) {
        double speed = 0;
        if (mode == CarriageMode.Extake) {
            speed = 1;
        } else if (mode == CarriageMode.Stop) {
            speed = 0;
        } else if (mode == CarriageMode.Transfer) { // TODO the transfer mode might be more complicated
            speed = 0.5;
        }

        mainMotor.set(ControlMode.PercentOutput, speed);
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public boolean hasCube() {
        return cubeSensor.get();
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    public enum CarriageMode {
        Extake, Stop, Transfer
    }
}
