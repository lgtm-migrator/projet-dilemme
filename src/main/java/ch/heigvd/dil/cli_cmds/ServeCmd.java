package ch.heigvd.dil.cli_cmds;

import ch.heigvd.dil.Main;
import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(name = "serve", description = "Serve the website")
public class ServeCmd implements Callable<Integer> {
  @CommandLine.Parameters(description = "The site path to serve")
  String path;

  @Override
  public Integer call() {
    int exitCode = new CommandLine(new Main()).execute("build", path);
    if (exitCode != 0) {
      return exitCode;
    }

    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
      try {
        Desktop.getDesktop()
            .browse(new URI("file://" + new File(path).getAbsolutePath() + "/build/index.html"));
      } catch (Exception e) {
        System.err.println("Could not open the browser");
        return 1;
      }
    }
    return 0;
  }
}
