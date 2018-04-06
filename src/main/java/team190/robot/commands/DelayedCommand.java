package team190.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.PrintCommand;
import edu.wpi.first.wpilibj.command.TimedCommand;

/**
 * Created by Kevin O'Brien on 3/14/2018.
 */
public class DelayedCommand extends CommandGroup {
    public DelayedCommand(double seconds, Command command) {
        addSequential(new TimedCommand(seconds));
        addSequential(command);
    }
}
