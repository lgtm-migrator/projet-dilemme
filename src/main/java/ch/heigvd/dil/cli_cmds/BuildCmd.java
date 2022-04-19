package ch.heigvd.dil.cli_cmds;

import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(name = "build", description = "")
public class BuildCmd implements Callable<Integer> {
  @Override
  public Integer call() {
    System.out.println("BuildCmd.call has been called !");
    return 0;
  }
}
