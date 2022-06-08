package ch.heigvd.dil;

import ch.heigvd.dil.cli_cmds.*;
import picocli.CommandLine;

/** DILemme ! */
@CommandLine.Command(
    name = "dilemme",
    subcommands = {
      BuildCmd.class,
      CleanCmd.class,
      InitCmd.class,
      ServeCmd.class,
      VersionCmd.class,
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
