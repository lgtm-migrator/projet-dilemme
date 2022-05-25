package ch.heigvd.dil.cli_cmds;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(name = "serve", description = "Serve the site on a local http server.")
public class ServeCmd implements Callable<Integer> {

  @CommandLine.Parameters(description = "The site path to serve")
  String path;

  @Override
  public Integer call() {
    // int exitCode = new CommandLine(new Main()).execute("build", path);
    // Build the site
    new CommandLine(new BuildCmd()).execute(path);

    // Serve the site
    Javalin.create(
            config -> {
              config.addStaticFiles(path + "/build", Location.EXTERNAL);
            })
        .start(8080);

    return 0;
  }
}
