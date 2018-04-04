/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package team190.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
import team190.models.DeadbandJoystick;
import team190.robot.commands.CollectCube;
import team190.robot.commands.CollectorCarriageManualMove;
import team190.robot.commands.VaultExtake;
import team190.robot.commands.carriage.CarriageIntakeSequence;
import team190.robot.commands.carriage.CarriageManualMove;
import team190.robot.commands.collector.AntiJerk;
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
    private static final int BUTTON_DRIVER_HIGH_GEAR = 2,
            BUTTON_DRIVER_LOW_GEAR = 3;

    // Buttons for the operator a
    private static final int BUTTON_OPERATOR_A_ELEV_CARRIAGE = 2,
            BUTTON_OPERATOR_A_ELEV_LOW = 1,
            BUTTON_OPERATOR_A_ELEV_MID = 3,
            BUTTON_OPERATOR_A_ELEV_HIGH = 4,
            BUTTON_OPERATOR_A_PREP_CLIMB = 5,
            BUTTON_OPERATOR_A_CLIMB = 6, // NOW TRANSFER
            BUTTON_OPERATOR_A_EXTAKE_FRONT = 7,
            BUTTON_OPERATOR_A_EXTAKE_REAR = 8;

    // Buttons for the operator b
    /*
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
*/
    private static final int BUTTON_OPERATOR_B_INTAKE = 9,
            BUTTON_OPERATOR_B_CARR_INTAKE = 10,
    //BUTTON_OPERATOR_B_TURBO = 11,
    BUTTON_OPERATOR_B_CARR_MAN_F = 12,
            BUTTON_OPERATOR_B_CARR_MAN_R = 13,
            BUTTON_OPERATOR_B_INT_MAN_F = 14,
            BUTTON_OPERATOR_B_INT_MAN_R = 15,
            BUTTON_OPERATOR_B_ELEV_MAN_D = 16,
            BUTTON_OPERATOR_B_ELEV_MAN_U = 11;
    //BUTTON_OPERATOR_B_MAN_OVERRIDE = 18;

    /* Driver Controls */
    private Button highGearButton, lowGearButton;
    /* Operator Controls */
    private Joystick operatorControllerA, operatorControllerB;
    private Button elevatorPosCarriageButton, elevatorPosLowButton, elevatorPosMidButton, elevatorPosHighButton,
            elevatorPosClimbButton, intakeButton, carriageIntakeButton, extakeFrontButton, extakeRearButton, prepClimb,
            turboButton, carriageFrontManualButton, carriageRearManualButton, intakeFrontManualButton, intakeRearManualButton,
            elevatorManualUpButton, elevatorManualDownButton, manualOverrideButton;

    //private XboxController xboxController;

    private Joystick leftStick;
    private Joystick rightStick;

    private XboxController operatorGamepad;

    private Joystick operatorStick;

    /**
     * Constructor
     */
    OI() {
        // Driver Sticks
        leftStick = new DeadbandJoystick(PORT_DRIVER_JOYSTICK_1, 0.1);
        rightStick = new DeadbandJoystick(PORT_DRIVER_JOYSTICK_2, 0.1);

        highGearButton = new JoystickButton(rightStick, BUTTON_DRIVER_HIGH_GEAR);
        highGearButton.whenPressed(new Shift(Gear.HIGH));
        lowGearButton = new JoystickButton(rightStick, BUTTON_DRIVER_LOW_GEAR);
        lowGearButton.whenPressed(new Shift(Gear.LOW));

        operatorGamepad = new XboxController(4);
        useXboxController();

        operatorStick = new Joystick(5);

        // Collector Carriage Jogs
        final int CarriageButton = 6, CollectorButton = 4;
        Trigger bothJogIn = new Trigger() {
            @Override
            public boolean get() {
                return operatorStick.getY() < -.75 && !operatorStick.getRawButton(CarriageButton) && !operatorStick.getRawButton(CollectorButton);
            }
        };
        bothJogIn.whileActive(new CollectorCarriageManualMove(Collector.IntakeMode.Intake, Carriage.CarriageMode.Extake));
        Trigger bothJogOut = new Trigger() {

            @Override
            public boolean get() {
                return operatorStick.getY() > .75 && !operatorStick.getRawButton(CarriageButton) && !operatorStick.getRawButton(CollectorButton);
            }
        };
        bothJogOut.whileActive(new CollectorCarriageManualMove(Collector.IntakeMode.Extake, Carriage.CarriageMode.Intake));

        // Collector Jogs
        Trigger collectorJogIn = new Trigger() {
            @Override
            public boolean get() {
                return operatorStick.getY() < -.75 && operatorStick.getRawButton(CollectorButton);
            }
        };
        collectorJogIn.whileActive(new CollectorManualMove(Collector.IntakeMode.Intake));
        Trigger collectorJogOut = new Trigger() {
            @Override
            public boolean get() {
                return operatorStick.getY() > .75 && operatorStick.getRawButton(CollectorButton);
            }
        };
        collectorJogOut.whileActive(new CollectorManualMove(Collector.IntakeMode.Extake));

        // Carriage Jogs
        Trigger carriageJogIn = new Trigger() {
            @Override
            public boolean get() {
                return operatorStick.getY() < -.75 && operatorStick.getRawButton(CarriageButton);
            }
        };
        carriageJogIn.whileActive(new CarriageManualMove(Carriage.CarriageMode.Extake));
        Trigger carriageJogOut = new Trigger() {
            @Override
            public boolean get() {
                return operatorStick.getY() > .75 && operatorStick.getRawButton(CarriageButton);
            }
        };
        carriageJogOut.whileActive(new CarriageManualMove(Carriage.CarriageMode.Intake));

        // Intake Button
        new JoystickButton(operatorStick, 1).whenPressed(new CollectCube());

        // Anti-Jerk
        new JoystickButton(operatorStick, 2).whenPressed(new AntiJerk());


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
        prepClimb.whenPressed(new VaultExtake());
        // TODO: Add command to prepare for climbing

        elevatorPosClimbButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_A_CLIMB);
        //elevatorPosClimbButton.whenPressed(new ElevatorPositionClimb());
        elevatorPosClimbButton.whenPressed(new CarriageIntakeSequence());

        extakeFrontButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_A_EXTAKE_FRONT);
        extakeFrontButton.whenPressed(new CollectorExtakeFront());

        extakeRearButton = new JoystickButton(operatorControllerA, BUTTON_OPERATOR_A_EXTAKE_REAR);
        extakeRearButton.whenPressed(new CollectorExtakeRear());

        // B CHANNEL OPERATOR
        //operatorControllerB = new Joystick(PORT_OPERATOR_CONTROLLER_B);
        operatorControllerB = operatorControllerA;

        // Intake & Extake
        intakeButton = new JoystickButton(operatorControllerB, BUTTON_OPERATOR_B_INTAKE);
        intakeButton.whenPressed(new CollectCube());

        carriageIntakeButton = new JoystickButton(operatorControllerB, BUTTON_OPERATOR_B_CARR_INTAKE);
        carriageIntakeButton.whenPressed(new AntiJerk());

        //turboButton = new JoystickButton(operatorControllerB, BUTTON_OPERATOR_B_TURBO);

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

        //manualOverrideButton = new JoystickButton(operatorControllerB, BUTTON_OPERATOR_B_MAN_OVERRIDE);

    }

    private void useXboxController() {
        // Elevator Presets:
        new JoystickButton(operatorGamepad, 1).whenPressed(new ElevatorPositionSwitch()); // A
        new JoystickButton(operatorGamepad, 2).whenPressed(new ElevatorPositionMed()); // B
        new JoystickButton(operatorGamepad, 3).whenPressed(new ElevatorPositionCarriage()); // X
        new JoystickButton(operatorGamepad, 4).whenPressed(new ElevatorPositionHigh()); // Y


        // Elevator Manual jog
        // Right Analog Stick Up and Down
        new AxisTrigger(GenericHID.Hand.kRight, AxisDirection.Y, PosNegDirection.UP).whileActive(new ElevatorManualMove(0.5));
        new AxisTrigger(GenericHID.Hand.kRight, AxisDirection.Y, PosNegDirection.DOWN).whileActive(new ElevatorManualMove(-0.5));

        // Carriage and Collector Manual job
        // Left Analog Stick Up and Down
        new AxisTrigger(GenericHID.Hand.kLeft, AxisDirection.Y, PosNegDirection.DOWN)
                .whileActive(new CollectorCarriageManualMove(Collector.IntakeMode.Intake, Carriage.CarriageMode.Extake));
        new AxisTrigger(GenericHID.Hand.kLeft, AxisDirection.Y, PosNegDirection.UP)
                .whileActive(new CollectorCarriageManualMove(Collector.IntakeMode.Extake, Carriage.CarriageMode.Intake));

        // Intake Sequence
        new TriggerTrigger(GenericHID.Hand.kLeft).whenActive(new CollectCube()); // Left trigger
        new JoystickButton(operatorGamepad, 5).whenPressed(new AntiJerk()); // Left bumper
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
    public boolean isTurboActivated() {
        //return turboButton.get();
        return true;
    }

    /**
     * Check if the elevator manual switch is on
     *
     * @return True if in manual mode
     */
    public boolean isElevatorManual() {
        //return manualOverrideButton.get();
        return true;
    }

    private enum AxisDirection {
        X, Y
    }

    private enum PosNegDirection {
        UP, DOWN
    }

    private class AxisTrigger extends Trigger {
        GenericHID.Hand hand;
        AxisDirection axisDirection;
        PosNegDirection posNegDirection;

        public AxisTrigger(GenericHID.Hand hand, AxisDirection axisDirection, PosNegDirection posNegDirection) {
            this.hand = hand;
            this.axisDirection = axisDirection;
            this.posNegDirection = posNegDirection;
        }

        @Override
        public boolean get() {
            if (axisDirection == AxisDirection.X) {
                if (posNegDirection == PosNegDirection.UP) {
                    return operatorGamepad.getX(hand) < -0.75;
                } else {
                    return operatorGamepad.getX(hand) > 0.75;
                }
            } else {
                if (posNegDirection == PosNegDirection.UP) {
                    return operatorGamepad.getY(hand) < -0.75;
                } else {
                    return operatorGamepad.getY(hand) > 0.75;
                }
            }
        }
    }

    private class TriggerTrigger extends Trigger {

        private final GenericHID.Hand hand;

        public TriggerTrigger(GenericHID.Hand hand) {
            this.hand = hand;
        }

        public boolean get() {
            return operatorGamepad.getTriggerAxis(hand) > 0.75;
        }
    }
}
