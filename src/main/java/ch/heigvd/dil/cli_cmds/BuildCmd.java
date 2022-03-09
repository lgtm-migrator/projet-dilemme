package ch.heigvd.dil.cli_cmds;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "build", description = "")
public class BuildCmd implements Callable<Integer> {
    @Override
    public Integer call() {
        System.out.println("BuildCmd.call has been called !");
        return 0;
    }
}
