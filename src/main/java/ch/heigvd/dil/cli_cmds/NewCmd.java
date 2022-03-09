package ch.heigvd.dil.cli_cmds;

import picocli.CommandLine;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "new", description = "")
public class NewCmd implements Callable<Integer> {

    @Override
    public Integer call() {
        System.out.println("NewCmd.call has been called !");
        return 0;
    }
}
