package team190.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;

public class ElevatorPositionIntake extends Command {

    public ElevatorPositionIntake() {
        // requires...
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {

    }

    /**
     * isFinished should return true in two cases
     * 1) when the elevator is in the intake position
     * 2) if the elevator has a cube loaded
     *
     * this makes it so the operator cannot move the elevator
     * to the intake position if a cube is loaded
     * @return
     */
    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    public synchronized void cancel() {

    }

    @Override
    protected void end() {

    }
}
