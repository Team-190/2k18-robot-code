/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package team190.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import team190.robot.commands.Shift;
import team190.robot.subsystems.Drivetrain.Gear;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 *
 * @author Jerry Brown
 */
public class OI {

    // Ports for controllers
    private static final int PORT_DRIVER_JOYSTICK_1 = 0,
            PORT_DRIVER_JOYSTICK_2 = 1,
            PORT_OPERATOR_CONTROLLER = 3;

    // Buttons for the operator
    private static final int BUTTON_EXAMPLE_ACTION = 0;

    Joystick leftStick;
    Joystick rightStick;
    Button highGear;
    Button lowGear;
    
    Joystick operatorController;


    /**
     * Constructor
     */
    public OI() {
        leftStick = new Joystick(PORT_DRIVER_JOYSTICK_1);
        rightStick = new Joystick(PORT_DRIVER_JOYSTICK_2);
        
        highGear = new JoystickButton(rightStick,3); 

        operatorController = new Joystick(PORT_OPERATOR_CONTROLLER);
        
        highGear.whenPressed(new Shift(Gear.HIGH));
        lowGear.whenPressed(new Shift(Gear.LOW));
    }

    /**
     * Get the value of the left Y axis
     *
     * @return Left Y axis (0.0 to 1.0)
     */
    public double getLeftY() {
        return leftStick.getY();
    }

    /**
     * Get the value of the right Y axis
     *
     * @return Right Y axis (0.0 to 1.0)
     */
    public double getRightY() {
        return rightStick.getY();
    }
}
