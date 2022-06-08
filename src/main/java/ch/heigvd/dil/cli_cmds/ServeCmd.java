package ch.heigvd.dil.cli_cmds;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(name = "serve", description = "Serve the site on a local http server.")
public class ServeCmd implements Callable<Integer> {

  @CommandLine.Parameters(description = "The site path to serve")
  String path;

  @CommandLine.Option(
      names = "--watch",
      description = "Automatically rebuild " + "and reload the site when a file is changed")
  boolean watchOption;

  @Override
  public Integer call() {
    new CommandLine(new BuildCmd()).execute(path);

    // Démarre le serveur
    Javalin.create(config -> config.addStaticFiles(path + "/build", Location.EXTERNAL)).start(8080);

    if (watchOption)
      // à chaque changement de fichier, le site est reconstruit
      // et le serveur est rechargé automatiquement
      new CommandLine(new BuildCmd()).execute(path, "--watch");

    return 0;
  }
}
