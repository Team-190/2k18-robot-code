package team190.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import team190.robot.commands.collector.CollectorExtakeFront;
import team190.robot.commands.collector.CollectorExtakeGround;
import team190.robot.commands.elevator.ElevatorPositionIntake;

/**
 * Created by Kevin O'Brien on 3/23/2018.
 */
public class VaultExtake extends CommandGroup {
    public VaultExtake() {
        addSequential(new ElevatorPositionIntake());
        addSequential(new CollectorExtakeGround());
    }
}
