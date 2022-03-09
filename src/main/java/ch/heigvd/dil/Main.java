package ch.heigvd.dil;

import ch.heigvd.dil.cli_cmds.CleanCmd;
import ch.heigvd.dil.cli_cmds.NewCmd;
import picocli.CommandLine;

/**
 * DILemme !
 */
@CommandLine.Command(name = "ISOCodeResolver",
        subcommands = { NewCmd.class, CleanCmd.class /*, CommandLine.HelpCommand.class */ },
        description = "")
public class Main
{
    public static void main(String[] args)
    {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}