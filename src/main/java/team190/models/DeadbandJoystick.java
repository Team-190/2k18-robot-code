package team190.models;

import edu.wpi.first.wpilibj.Joystick;

public class DeadbandJoystick extends Joystick {
    double deadband;

    /**
     * Construct an instance of a joystick. The joystick index is the USB port on the drivers
     * station.
     *
     * @param port The port on the Driver Station that the joystick is plugged into.
     */
    public DeadbandJoystick(int port, double deadband) {
        super(port);

        this.deadband = deadband;
    }

    @Override
    public double getRawAxis(int axis) {
        double value = super.getRawAxis(axis);

        if (axis != getYChannel()) return value;

        boolean isPositive = value > 0;

        value = Math.abs(value);
        value = Math.max(0, value - deadband);
        value = value / (1 - deadband);

        return isPositive ? value : -value;
    }
}
