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

    // Buttons for the driver
    private static final int BUTTON_DRIVER_HIGH_GEAR = 3,
                             BUTTON_DRIVER_LOW_GEAR = 4;

    // Buttons for the operator
    private static final int BUTTON_OPERATOR_ELEV_CARRIAGE_FT = 0,
                             BUTTON_OPERATOR_ELEV_FIVE_FT = 0,
                             BUTTON_OPERATOR_ELEV_SIX_FT = 0,
                             BUTTON_OPERATOR_ELEV_SEVEN_FT = 0,
                             BUTTON_OPERATOR_ELEV_CLIMB = 0,

                             BUTTON_OPERATOR_INTAKE = 0,
                             BUTTON_OPERATOR_EXTAKE_FRONT = 0,
                             BUTTON_OPERATOR_EXTAKE_REAR = 0;

    /* Driver Controls */
    Joystick leftStick;
    Joystick rightStick;
    Button highGearButton, lowGearButton;

    /* Operator Controls */
    Joystick operatorControllerA, getOperatorControllerB;
    Button elevatorPosCarriageButton, elevatorPosFiveFtButton, elevatorPosSixFtButton, elevatorPosSevenFtButton,
           elevatorPosClimbButton, intakeButton, extakeFrontButton, extakeRearButton;

    /**
     * Constructor
     */
    public OI() {
        // Driver
        leftStick = new Joystick(PORT_DRIVER_JOYSTICK_1);
        rightStick = new Joystick(PORT_DRIVER_JOYSTICK_2);

        highGearButton = new JoystickButton(rightStick, BUTTON_DRIVER_HIGH_GEAR);
        highGearButton.whenPressed(new Shift(Gear.HIGH));

        lowGearButton = new JoystickButton(rightStick, BUTTON_DRIVER_LOW_GEAR);
        lowGearButton.whenPressed(new Shift(Gear.LOW));

        /* Operator */
        operatorControllerA = new Joystick(PORT_OPERATOR_CONTROLLER);

        // Elevator Positions
        elevatorPosCarriageButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_ELEV_CARRIAGE_FT);
        elevatorPosFiveFtButton  = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_ELEV_FIVE_FT);
        elevatorPosSixFtButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_ELEV_SIX_FT);
        elevatorPosSevenFtButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_ELEV_SEVEN_FT);
        elevatorPosClimbButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_ELEV_CLIMB);

        // Intake & Extake
        intakeButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_INTAKE);
        extakeFrontButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_EXTAKE_FRONT);
        extakeRearButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_EXTAKE_REAR);
    }

    /**
     * Get the value of the left Y axis
     *
     * @return Left Y axis (-1.0 to 1.0)
     */
    public double getLeftY() {
        return leftStick.getY();
    }

    /**
     * Get the value of the right Y axis
     *
     * @return Right Y axis (-1.0 to 1.0)
     */
    public double getRightY() {
        return rightStick.getY();
    }
}
