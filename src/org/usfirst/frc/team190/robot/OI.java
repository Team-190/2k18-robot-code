/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team190.robot;

import org.usfirst.frc.team190.robot.commands.CarriagePlace;
import org.usfirst.frc.team190.robot.commands.ElevatorManualMove;
import org.usfirst.frc.team190.robot.commands.ElevatorMove;
import org.usfirst.frc.team190.robot.commands.ExtakeCube;
import org.usfirst.frc.team190.robot.commands.IntakeCube;
import org.usfirst.frc.team190.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());
	
	Joystick stick = new Joystick(1);
	JoystickButton elevatorUp = new JoystickButton(stick, 4);
	JoystickButton elevatorDown = new JoystickButton(stick, 1);
	JoystickButton elevatorGround = new JoystickButton(stick, 5);
	JoystickButton elevatorRest = new JoystickButton(stick, 6);
	JoystickButton elevatorFiveFt = new JoystickButton(stick, 7);
	JoystickButton elevatorSixFt = new JoystickButton(stick, 8);
	JoystickButton elevatorSevenFt = new JoystickButton(stick, 9);
	JoystickButton elevatorEightFt = new JoystickButton(stick, 10);
	
	JoystickButton intakeCube = new JoystickButton(stick, 11);
	JoystickButton extakeCube = new JoystickButton(stick, 12);
	JoystickButton carriagePlace = new JoystickButton(stick, 13);
	
	public OI() {
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
}
