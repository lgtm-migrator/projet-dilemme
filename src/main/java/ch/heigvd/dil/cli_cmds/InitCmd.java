package ch.heigvd.dil.cli_cmds;

import ch.heigvd.dil.data_structures.Page;
import ch.heigvd.dil.data_structures.Site;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.concurrent.Callable;
import picocli.CommandLine;

/** Cette classe permet d'initialiser un site. */
@CommandLine.Command(name = "init", description = "")
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

    status = createFile(new File(siteFolder, "config.json"), siteConfig);

    if (!status) {
      System.err.println("Error: Could not create config.json.");
      return 1;
    }

    File contentFolder = new File(path + "/content");
    contentFolder.mkdirs();

    String pageConfig =
        new Page.Config("title", "author", LocalDate.now()).getJSON()
            + "\n"
            + "---"
            + "\n"
            + "# Your content here";

    status = createFile(new File(contentFolder, "page.md"), pageConfig);

    if (!status) {
      System.err.println("Error: Could not create page.md.");
      return 1;
    }

    System.out.println("Site initialized.");
    return 0;
  }

  /**
   * Crée un fichier à partir d'un contenu.
   *
   * @param file le fichier à créer
   * @param content le contenu du fichier
   * @return true si le fichier a été créé, false sinon
   */
  private boolean createFile(File file, String content) {
    try {
      OutputStreamWriter osw =
          new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
      osw.write(content);
      osw.close();
    } catch (IOException e) {
      return false;
    }
    return true;
  }
}
