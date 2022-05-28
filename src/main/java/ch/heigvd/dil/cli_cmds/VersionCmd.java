package ch.heigvd.dil.cli_cmds;

import ch.heigvd.dil.Version;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(name = "-version", description = "Display the version")
public class VersionCmd implements Callable<Integer> {
  @Override
  public Integer call() {

    System.out.println("DIL Site Generator Version : " + Version.CURRENT);
    return 0;
  }
}
