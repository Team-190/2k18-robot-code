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
import team190.models.DeadbandJoystick;
import team190.robot.commands.CollectCube;
import team190.robot.commands.collector.CollectorExtakeFront;
import team190.robot.commands.collector.CollectorExtakeRear;
import team190.robot.commands.drivetrain.Shift;
import team190.robot.commands.elevator.*;
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
            PORT_OPERATOR_CONTROLLER_A = 2,
            PORT_OPERATOR_CONTROLLER_B = 3;

    // Buttons for the driver
    private static final int BUTTON_DRIVER_HIGH_GEAR = 3,
            BUTTON_DRIVER_LOW_GEAR = 4;

    // Buttons for the operator
    private static final int BUTTON_OPERATOR_ELEV_CARRIAGE_FT = 0,
            BUTTON_OPERATOR_ELEV_LOW = 0,
            BUTTON_OPERATOR_ELEV_MID = 0,
            BUTTON_OPERATOR_ELEV_HIGH = 0,
            BUTTON_OPERATOR_ELEV_CLIMB = 0,

            BUTTON_OPERATOR_INTAKE = 0,
            BUTTON_OPERATOR_EXTAKE_FRONT = 0,
            BUTTON_OPERATOR_EXTAKE_REAR = 0;

    /* Driver Controls */
    private Button highGearButton, lowGearButton;
    /* Operator Controls */
    private Joystick operatorControllerA, operatorControllerB;
    private Button elevatorPosCarriageButton, elevatorPosLowButton, elevatorPosMidButton, elevatorPosHighButton,
            elevatorPosClimbButton, intakeButton, extakeFrontButton, extakeRearButton;

    private Joystick leftStick;
    private Joystick rightStick;

    /**
     * Constructor
     */
    OI() {
        // Driver
        // TODO: Calibrate deadband
        leftStick = new DeadbandJoystick(PORT_DRIVER_JOYSTICK_1, 0.1);
        rightStick = new DeadbandJoystick(PORT_DRIVER_JOYSTICK_2, 0.1);

        highGearButton = new JoystickButton(rightStick, BUTTON_DRIVER_HIGH_GEAR);
        highGearButton.whenPressed(new Shift(Gear.HIGH));
        lowGearButton = new JoystickButton(rightStick, BUTTON_DRIVER_LOW_GEAR);
        lowGearButton.whenPressed(new Shift(Gear.LOW));

        /* Operator */
        operatorControllerA = new Joystick(PORT_OPERATOR_CONTROLLER_A);

        // Elevator Positions
        elevatorPosCarriageButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_ELEV_CARRIAGE_FT);
        elevatorPosCarriageButton.whenPressed(new ElevatorPositionCarriage());

        elevatorPosLowButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_ELEV_LOW);
        elevatorPosLowButton.whenPressed(new ElevatorPositionLow());

        elevatorPosMidButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_ELEV_MID);
        elevatorPosMidButton.whenPressed(new ElevatorPositionMed());

        elevatorPosHighButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_ELEV_HIGH);
        elevatorPosHighButton.whenPressed(new ElevatorPositionHigh());

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
