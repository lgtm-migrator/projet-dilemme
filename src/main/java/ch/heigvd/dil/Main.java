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
      InitCmd.class,
    },
    description = "")
public class Main {
  public static void main(String[] args) {
    int exitCode = new CommandLine(new Main()).execute(args);
    // Vérification nécessaire pour que la commande serve fonctionne
    if (exitCode != 0) {
      System.exit(exitCode);
    }
  }
}
