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
import team190.robot.commands.carriage.CarriageIntake;
import team190.robot.commands.carriage.CarriageManualMove;
import team190.robot.commands.collector.CollectorExtakeFront;
import team190.robot.commands.collector.CollectorExtakeRear;
import team190.robot.commands.collector.CollectorManualMove;
import team190.robot.commands.drivetrain.Shift;
import team190.robot.commands.elevator.*;
import team190.robot.subsystems.Carriage;
import team190.robot.subsystems.Collector;
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
            BUTTON_DRIVER_LOW_GEAR = 2;

    // Buttons for the operator a
    private static final int BUTTON_OPERATOR_A_ELEV_CARRIAGE = 1,
            BUTTON_OPERATOR_A_ELEV_LOW = 2,
            BUTTON_OPERATOR_A_ELEV_MID = 3,
            BUTTON_OPERATOR_A_ELEV_HIGH = 4,
            BUTTON_OPERATOR_A_PREP_CLIMB = 5,
            BUTTON_OPERATOR_A_CLIMB = 6,
            BUTTON_OPERATOR_A_EXTAKE_FRONT = 7,
            BUTTON_OPERATOR_A_EXTAKE_REAR = 8;

    // Buttons for the operator b
    private static final int BUTTON_OPERATOR_B_INTAKE = 1,
            BUTTON_OPERATOR_B_CARR_INTAKE = 2,
            BUTTON_OPERATOR_B_TURBO = 3,
            BUTTON_OPERATOR_B_CARR_MAN_F = 4,
            BUTTON_OPERATOR_B_CARR_MAN_R = 5,
            BUTTON_OPERATOR_B_INT_MAN_F = 6,
            BUTTON_OPERATOR_B_INT_MAN_R = 7,
            BUTTON_OPERATOR_B_ELEV_MAN_D = 8,
            BUTTON_OPERATOR_B_ELEV_MAN_U = 9,
            BUTTON_OPERATOR_B_MAN_OVERRIDE = 10;


    /* Driver Controls */
    private Button highGearButton, lowGearButton;
    /* Operator Controls */
    private Joystick operatorControllerA, operatorControllerB;
    private Button elevatorPosCarriageButton, elevatorPosLowButton, elevatorPosMidButton, elevatorPosHighButton,
            elevatorPosClimbButton, intakeButton, carriageIntakeButton, extakeFrontButton, extakeRearButton, prepClimb,
            turboButton, carriageFrontManualButton, carriageRearManualButton, intakeFrontManualButton, intakeRearManualButton,
            elevatorManualUpButton, elevatorManualDownButton, manualOverrideButton;

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

        // A CHANNEL OPERATOR
        operatorControllerA = new Joystick(PORT_OPERATOR_CONTROLLER_A);

        // Elevator Positions
        elevatorPosCarriageButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_A_ELEV_CARRIAGE);
        elevatorPosCarriageButton.whenPressed(new ElevatorPositionCarriage());

        elevatorPosLowButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_A_ELEV_LOW);
        elevatorPosLowButton.whenPressed(new ElevatorPositionLow());

        elevatorPosMidButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_A_ELEV_MID);
        elevatorPosMidButton.whenPressed(new ElevatorPositionMed());

        elevatorPosHighButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_A_ELEV_HIGH);
        elevatorPosHighButton.whenPressed(new ElevatorPositionHigh());

        prepClimb = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_A_PREP_CLIMB);
        // TODO: Add command to prepare for climbing

        elevatorPosClimbButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_A_CLIMB);
        elevatorPosClimbButton.whenPressed(new ElevatorPositionClimb());

        extakeFrontButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_A_EXTAKE_FRONT);
        extakeFrontButton.whenPressed(new CollectorExtakeFront());

        extakeRearButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_A_EXTAKE_REAR);
        extakeRearButton.whenPressed(new CollectorExtakeRear());

        // B CHANNEL OPERATOR
        operatorControllerB = new Joystick(PORT_OPERATOR_CONTROLLER_B);
        // Intake & Extake
        intakeButton = new JoystickButton(operatorControllerB, BUTTON_OPERATOR_B_INTAKE);
        intakeButton.whenPressed(new CollectCube());

        carriageIntakeButton = new JoystickButton(operatorControllerB, BUTTON_OPERATOR_B_CARR_INTAKE);
        carriageIntakeButton.whenPressed(new CarriageIntake());

        turboButton = new JoystickButton(operatorControllerB, BUTTON_OPERATOR_B_TURBO);
        // TODO: add command for turbo

        carriageFrontManualButton = new JoystickButton(operatorControllerB, BUTTON_OPERATOR_B_CARR_MAN_F);
        carriageFrontManualButton.whileHeld(new CarriageManualMove(Carriage.CarriageMode.Intake));

        carriageRearManualButton = new JoystickButton(operatorControllerB, BUTTON_OPERATOR_B_CARR_MAN_R);
        carriageRearManualButton.whileHeld(new CarriageManualMove(Carriage.CarriageMode.Extake));

        intakeFrontManualButton = new JoystickButton(operatorControllerB, BUTTON_OPERATOR_B_INT_MAN_F);
        intakeFrontManualButton.whileHeld(new CollectorManualMove(Collector.IntakeMode.Extake));

        intakeRearManualButton = new JoystickButton(operatorControllerB, BUTTON_OPERATOR_B_INT_MAN_R);
        intakeRearManualButton.whileHeld(new CollectorManualMove(Collector.IntakeMode.Intake));

        elevatorManualDownButton = new JoystickButton(operatorControllerB, BUTTON_OPERATOR_B_ELEV_MAN_D);
        elevatorManualDownButton.whileHeld(new ElevatorManualMove(-0.5));

        elevatorManualUpButton = new JoystickButton(operatorControllerB, BUTTON_OPERATOR_B_ELEV_MAN_U);
        elevatorManualUpButton.whileHeld(new ElevatorManualMove(0.5));

        manualOverrideButton = new JoystickButton(operatorControllerB, BUTTON_OPERATOR_B_MAN_OVERRIDE);
        // TODO: add manual override to elevator
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

    /**
     * Check if the turbo mode is switched on
     *
     * @return True if in turbo mode
     */
    public boolean getTurbo() { return turboButton.get(); }
}
