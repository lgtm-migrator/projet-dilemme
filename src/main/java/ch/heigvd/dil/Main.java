package ch.heigvd.dil;

import ch.heigvd.dil.cli_cmds.*;
import picocli.CommandLine;

/** DILemme ! */
@CommandLine.Command(
    name = "DILemme",
    subcommands = {
      NewCmd.class,
      CleanCmd.class,
      BuildCmd.class,
      ServeCmd.class, /*, CommandLine.HelpCommand.class */
      VersionCmd.class,
    },
    description = "")
public class Main {
  public static void main(String[] args) {
    int exitCode = new CommandLine(new Main()).execute(args);
    System.exit(exitCode);
  }
}
