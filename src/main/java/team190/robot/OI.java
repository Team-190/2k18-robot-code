/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package team190.robot;

import edu.wpi.first.wpilibj.Joystick;
import team190.models.DeadbandJoystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import team190.robot.commands.CollectCube;
import team190.robot.commands.Shift;
import team190.robot.commands.collector.CollectorExtakeFront;
import team190.robot.commands.collector.CollectorExtakeRear;
import team190.robot.commands.elevator.*;
import team190.robot.subsystems.Drivetrain.Gear;

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
    Button highGearButton, lowGearButton;
    private Joystick leftStick;
    private Joystick rightStick;
    //Button highGear;
    //Button lowGear;

    Joystick operatorController;

    /* Operator Controls */
    Joystick operatorControllerA, getOperatorControllerB;
    Button elevatorPosCarriageButton, elevatorPosFiveFtButton, elevatorPosSixFtButton, elevatorPosSevenFtButton,
           elevatorPosClimbButton, intakeButton, extakeFrontButton, extakeRearButton;

    /**
     * Constructor
     */
    public OI() {
        // Driver
        highGearButton = new JoystickButton(rightStick, BUTTON_DRIVER_HIGH_GEAR);
        highGearButton.whenPressed(new Shift(Gear.HIGH));
        leftStick = new DeadbandJoystick(PORT_DRIVER_JOYSTICK_1, 0.1);
        rightStick = new DeadbandJoystick(PORT_DRIVER_JOYSTICK_2, 0.1);

        //highGear = new JoystickButton(rightStick,3);
        //lowGear = new JoystickButton(rightStick, 4);

        lowGearButton = new JoystickButton(rightStick, BUTTON_DRIVER_LOW_GEAR);
        lowGearButton.whenPressed(new Shift(Gear.LOW));

        /* Operator */
        operatorControllerA = new Joystick(PORT_OPERATOR_CONTROLLER);

        // Elevator Positions
        elevatorPosCarriageButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_ELEV_CARRIAGE_FT);
        elevatorPosCarriageButton.whenPressed(new ElevatorPositionCarriage());

        elevatorPosFiveFtButton  = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_ELEV_FIVE_FT);
        elevatorPosFiveFtButton.whenPressed(new ElevatorPositionFiveFeet());

        elevatorPosSixFtButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_ELEV_SIX_FT);
        elevatorPosSixFtButton.whenPressed(new ElevatorPositionSixFeet());

        elevatorPosSevenFtButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_ELEV_SEVEN_FT);
        elevatorPosSevenFtButton.whenPressed(new ElevatorPositionSevenFeet());

        elevatorPosClimbButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_ELEV_CLIMB);
        elevatorPosClimbButton.whenPressed(new ElevatorPositionClimb());

        // Intake & Extake
        intakeButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_INTAKE);
        intakeButton.whenPressed(new CollectCube());

        extakeFrontButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_EXTAKE_FRONT);
        extakeFrontButton.whenPressed(new CollectorExtakeFront());

        extakeRearButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_EXTAKE_REAR);
        extakeRearButton.whenPressed(new CollectorExtakeRear());
    }

    /**
     * Get the value of the left Y axis
     *
     * @return Left Y axis (-1.0 to 1.0)
     */
    public double getLeftY() {
        return leftStick.getY() * -1.0;
    }

    /**
     * Get the value of the right Y axis
     *
     * @return Right Y axis (-1.0 to 1.0)
     */
    public double getRightY() {
        return rightStick.getY() * -1.0;
    }
}
