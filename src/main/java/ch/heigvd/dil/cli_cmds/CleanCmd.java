package ch.heigvd.dil.cli_cmds;

import java.io.*;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(name = "clean", description = "")
public class CleanCmd implements Callable<Integer> {
  // paramètre indiquant le site à nettoyer
  @CommandLine.Parameters(description = "The site path to clean")
  String path;

  @Override
  public Integer call() {
    System.out.println("Cleaning site...");

    File file = new File(path + "/build");

    if (!(file.exists() && file.isDirectory())) {
      System.err.println("Error: The build directory does not exist in '" + path + "'");
      return 1;
    }

    boolean status = file.delete();
    if (!status) {
      System.err.println("Error: Could not delete the build directory");
      return 1;
    }

    System.out.println("Done.");
    return 0;
  }
}
