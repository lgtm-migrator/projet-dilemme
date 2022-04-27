package ch.heigvd.dil.cli_cmds;

import ch.heigvd.dil.data_structures.Page;
import ch.heigvd.dil.data_structures.Site;
import ch.heigvd.dil.utils.FileHandler;
import java.io.*;
import java.time.LocalDate;
import java.util.concurrent.Callable;
import picocli.CommandLine;

/** Cette classe permet d'initialiser un site. */
@CommandLine.Command(name = "init", description = "Initialize a site")
public class InitCmd implements Callable<Integer> {
  // paramètre indiquant le site le chemin du site à initialiser
  @CommandLine.Parameters(description = "The site path to clean")
  String path;

  @Override
  public Integer call() {
    System.out.println("Initializing site...");

    File siteFolder = new File(path);
    if (siteFolder.exists()) {
      System.err.println("Error: The site has already been initialised.");
      return 1;
    }

    boolean status = siteFolder.mkdirs();
    if (!status) {
      System.err.println("Error: Could not initialize the site.");
      return 1;
    }

    String siteConfig = new Site.Config("title", "owner", "domain").getJSON();

    status = FileHandler.write(new File(siteFolder, "config.json"), siteConfig);

    if (!status) {
      System.err.println("Error: Could not create config.json.");
      return 1;
    }

    String pageConfig =
        new Page.Config("title", "author", LocalDate.now()).getJSON()
            + "\n"
            + "---"
            + "\n"
            + "# Your content here";

    status = FileHandler.write(new File(siteFolder, "index.md"), pageConfig);

    if (!status) {
      System.err.println("Error: Could not create index.md.");
      return 1;
    }

    System.out.println("Site initialized.");
    return 0;
  }
}
