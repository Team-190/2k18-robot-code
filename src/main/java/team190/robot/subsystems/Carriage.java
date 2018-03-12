package team190.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import team190.robot.Robot;

public class Carriage extends Subsystem {

    private static final int CARRIAGE_SRX = 9;
    private static final int CARRIAGE_CUBE_SENSOR_PORT = 2;

    // Banner sensor
    private DigitalInput cubeSensor;

    private TalonSRX mainMotor;

    public Carriage() {
        mainMotor = new TalonSRX(CARRIAGE_SRX);
        cubeSensor = new DigitalInput(CARRIAGE_CUBE_SENSOR_PORT);
        //mainMotor.setInverted(false);
    }

    public void move(CarriageMode mode) {
        double speed = 0;
        // TODO: Calibrate speeds
        if (mode == CarriageMode.Extake) {
            // Modify extake speed if in turbo mode
            speed = Robot.m_oi.getTurbo() ? 1 : 2;
        } else if (mode == CarriageMode.Stop) {
            speed = 0;
        } else if (mode == CarriageMode.Transfer) {
            speed = 0.5;
        } else if (mode == CarriageMode.Intake) {
            speed = -.75;
        }

        mainMotor.set(ControlMode.PercentOutput, speed);
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    /**
     * Check if the cube banner sensor is indicating a cube in the passthru
     *
     * @return true if there is a cube in the passthru
     */
    public boolean hasCube() {
        return !cubeSensor.get();
    }

    /**
     * no default command for Carriage
     */
    public void initDefaultCommand() {
    }

    public enum CarriageMode {
        Extake, Stop, Transfer, Intake
    }
}
