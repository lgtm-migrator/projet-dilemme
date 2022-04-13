package ch.heigvd.dil.cli_cmds;

import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(name = "serve", description = "")
public class ServeCmd implements Callable<Integer> {
  @Override
  public Integer call() {
    System.out.println("ServeCmd.call has been called !");
    return 0;
  }
}
