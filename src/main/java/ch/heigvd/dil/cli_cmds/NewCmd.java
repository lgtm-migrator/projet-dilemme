package ch.heigvd.dil.cli_cmds;

import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(name = "new", description = "")
public class NewCmd implements Callable<Integer> {

  @Override
  public Integer call() {
    System.out.println("NewCmd.call has been called !");
    return 0;
  }
}
