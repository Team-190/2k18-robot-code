/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package team190.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import team190.robot.commands.*;
import team190.robot.subsystems.Elevator;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
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

    Joystick operatorController;

    JoystickButton elevatorUp = new JoystickButton(operatorController, 4);
    JoystickButton elevatorDown = new JoystickButton(operatorController, 1);
    JoystickButton elevatorGround = new JoystickButton(operatorController, 5);
    JoystickButton elevatorRest = new JoystickButton(operatorController, 6);
    JoystickButton elevatorFiveFt = new JoystickButton(operatorController, 7);
    JoystickButton elevatorSixFt = new JoystickButton(operatorController, 8);
    JoystickButton elevatorSevenFt = new JoystickButton(operatorController, 9);
    JoystickButton elevatorEightFt = new JoystickButton(operatorController, 10);

    JoystickButton intakeCube = new JoystickButton(operatorController, 11);
    JoystickButton extakeCube = new JoystickButton(operatorController, 12);
    JoystickButton carriagePlace = new JoystickButton(operatorController, 13);

    /**
     * Constructor
     */
    public OI() {
        leftStick = new Joystick(PORT_DRIVER_JOYSTICK_1);
        rightStick = new Joystick(PORT_DRIVER_JOYSTICK_2);

        operatorController = new Joystick(PORT_OPERATOR_CONTROLLER);

        elevatorUp.whileHeld(new ElevatorManualMove(0.5));
        elevatorDown.whileHeld(new ElevatorManualMove(-0.5));
        elevatorGround.whenPressed(new ElevatorMove(Elevator.GROUND));
        elevatorRest.whenPressed(new ElevatorMove(Elevator.REST));
        elevatorFiveFt.whenPressed(new ElevatorMove(Elevator.FIVEFT));
        elevatorSixFt.whenPressed(new ElevatorMove(Elevator.SIXFT));
        elevatorSevenFt.whenPressed(new ElevatorMove(Elevator.SEVENFT));
        elevatorEightFt.whenPressed(new ElevatorMove(Elevator.EIGHTFT));

        intakeCube.whenPressed(new IntakeCube());
        extakeCube.whenPressed(new ExtakeCube());
        carriagePlace.whenPressed(new CarriagePlace());
    }

    /**
     * Get the value of the left Y axis
     * @return Left Y axis (0.0 to 1.0)
     */
    public double getLeftY() {
        return leftStick.getY();
    }

    /**
     * Get the value of the right Y axis
     * @return Right Y axis (0.0 to 1.0)
     */
    public double getRightY() {
        return rightStick.getY();
    }
}
