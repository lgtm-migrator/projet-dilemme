package ch.heigvd.dil.cli_cmds;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "clean", description = "")
public class CleanCmd implements Callable<Integer> {
  @Override
  public Integer call() {
    System.out.println("CleanCmd.call has been called !");
    return 0;
  }
}
