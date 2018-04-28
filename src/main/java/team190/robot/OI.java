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
import team190.robot.models.DeadbandJoystick;
import team190.robot.commands.CollectCube;
import team190.robot.commands.collector.AntiJam;
import team190.robot.commands.collector.CarriageManualMove;
import team190.robot.commands.collector.CollectorCarriageManualMove;
import team190.robot.commands.collector.CollectorManualMove;
import team190.robot.commands.drivetrain.Shift;
import team190.robot.commands.elevator.*;
import team190.robot.subsystems.Carriage;
import team190.robot.subsystems.Collector;
import team190.robot.subsystems.Drivetrain.Gear;

/**
 * Sorry this file is real gross at this point. Too bad. The rest of the code's ok.
 *
 * @author Jerry Brown
 */
public class OI {

    // Ports for controllers
    private static final int PORT_DRIVER_JOYSTICK_1 = 0,
            PORT_DRIVER_JOYSTICK_2 = 1,
            PORT_OPERATOR_CONTROLLER = 2;

    // Buttons for the driver
    private static final int BUTTON_DRIVER_HIGH_GEAR = 3,
            BUTTON_DRIVER_LOW_GEAR = 2;

    /* Driver Controls */
    private Button highGearButton, lowGearButton;
    /* Operator Controls */
    private Joystick operatorController;

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

        new JoystickButton(leftStick, 1).whenPressed(new ElevatorPositionCarriage()); // Trigger
        new JoystickButton(rightStick, 1).whenPressed(new CollectCube()); // Trigger

        //operatorGamepad = new XboxController(5);
        //useXboxController();

        operatorStick = new Joystick(4);

        // Manual Jogs
        final int CarriageButton = 4, CollectorButton = 6, ElevatorButton = 5;

        // Jog Carriage and Collector
        Trigger jogUp = new Trigger() {
            @Override
            public boolean get() {
                boolean carr = operatorStick.getRawButton(CarriageButton);
                boolean coll = operatorStick.getRawButton(CollectorButton);
                boolean elev = operatorStick.getRawButton(ElevatorButton);
                return operatorStick.getY() < -0.5 && !carr && !coll && !elev;
            }
        };
        jogUp.whileActive(new CollectorCarriageManualMove(Collector.IntakeMode.Extake, Carriage.CarriageMode.Intake));

        // Jog Carriage and Collector
        Trigger jogDown = new Trigger() {
            @Override
            public boolean get() {
                boolean carr = operatorStick.getRawButton(CarriageButton);
                boolean coll = operatorStick.getRawButton(CollectorButton);
                boolean elev = operatorStick.getRawButton(ElevatorButton);
                return operatorStick.getY() > 0.5 && !carr && !coll && !elev;
            }
        };
        jogDown.whileActive(new CollectorCarriageManualMove(Collector.IntakeMode.Intake, Carriage.CarriageMode.Extake));

        Trigger groundExtake = new Trigger() {
            @Override
            public boolean get() {
                return operatorStick.getPOV() == 0; // 0 degrees is top
            }
        };
        groundExtake.whileActive(new CollectorManualMove(Collector.IntakeMode.ExtakeGround));


        new ButtonAxisTrigger(operatorStick, CarriageButton, true)
                .whileActive(new CarriageManualMove(Carriage.CarriageMode.Intake));
        new ButtonAxisTrigger(operatorStick, CarriageButton, false)
                .whileActive(new CarriageManualMove(Carriage.CarriageMode.Extake));

        new ButtonAxisTrigger(operatorStick, CollectorButton, true)
                .whileActive(new CollectorManualMove(Collector.IntakeMode.Extake));
        new ButtonAxisTrigger(operatorStick, CollectorButton, false)
                .whileActive(new CollectorManualMove(Collector.IntakeMode.Intake));

        new ButtonAxisTrigger(operatorStick, ElevatorButton, true)
                .whileActive(new ElevatorManualMove(0.5));
        new ButtonAxisTrigger(operatorStick, ElevatorButton, false)
                .whileActive(new ElevatorManualMove(-0.5));

        // Intake Button
        new JoystickButton(operatorStick, 1).whenPressed(new CollectCube());

        // Anti-Jam
        new JoystickButton(operatorStick, 2).whenPressed(new AntiJam());

        // A CHANNEL OPERATOR
        operatorController = new Joystick(PORT_OPERATOR_CONTROLLER);

        new JoystickButton(operatorController, 1).whenPressed(new ElevatorPositionHigh());
        new JoystickButton(operatorController, 2).whenPressed(new ElevatorPositionMed());
        new JoystickButton(operatorController, 3).whenPressed(new ElevatorPositionSwitch());
        new JoystickButton(operatorController, 4).whenPressed(new ElevatorPositionCarriage());
        new JoystickButton(operatorController, 5).whenPressed(new ElevatorPositionIntake());
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
        new JoystickButton(operatorGamepad, 5).whenPressed(new AntiJam()); // Left bumper
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


    private enum AxisDirection {
        X, Y
    }

    private enum PosNegDirection {
        UP, DOWN
    }

    private class ButtonAxisTrigger extends Trigger {
        private GenericHID joystick;
        private int buttonNumber;
        private boolean up;

        ButtonAxisTrigger(GenericHID joystick, int buttonNumber, boolean up) {

            this.joystick = joystick;
            this.buttonNumber = buttonNumber;
            this.up = up;
        }

        @Override
        public boolean get() {
            boolean buttonStatus = joystick.getRawButton(buttonNumber);
            if (up) {
                return joystick.getY() < -0.5 && buttonStatus;
            } else {
                return joystick.getY() > 0.5 && buttonStatus;
            }
        }
    }

    private class AxisTrigger extends Trigger {
        GenericHID.Hand hand;
        AxisDirection axisDirection;
        PosNegDirection posNegDirection;

        AxisTrigger(GenericHID.Hand hand, AxisDirection axisDirection, PosNegDirection posNegDirection) {
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

        TriggerTrigger(GenericHID.Hand hand) {
            this.hand = hand;
        }

        public boolean get() {
            return operatorGamepad.getTriggerAxis(hand) > 0.75;
        }
    }
}
