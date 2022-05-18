package ch.heigvd.dil.cli_cmds;

import ch.heigvd.dil.data_structures.Page;
import ch.heigvd.dil.data_structures.Site;
import ch.heigvd.dil.utils.FileHandler;
import ch.heigvd.dil.utils.parsers.MarkdownParser;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.concurrent.Callable;
import org.everit.json.schema.ValidationException;
import picocli.CommandLine;

/**
 * Cette classe reproduit la structure du site et convertit les fichiers Markdown en HTML et
 * retourne le résultat dans le dossier "build"
 */
@CommandLine.Command(name = "build", description = "Build the website")
public class BuildCmd implements Callable<Integer> {
  // paramètre indiquant le site le chemin du site à initialiser
  @CommandLine.Parameters(description = "The site path to clean")
  String path;

  private Site site;

  @Override
  public Integer call() {
    System.out.println("Building site...");
    boolean status;

    File[] allFiles = new File(path).listFiles();

    if (allFiles == null) {
      System.err.println("Error: Site folder '" + path + "' does not exist");
      return 1;
    }

    // Vérifie si le site est initialisé
    int initFiles = 0;
    File configFile = null;
    for (File f : allFiles) {
      if (f.getName().equals("index.md")) {
        ++initFiles;
      } else if (f.getName().equals("config.json")) {
        ++initFiles;
        configFile = f;
      }
    }

    if (initFiles != 2 || configFile == null) {
      System.err.println("Error: The site has to be initialized first.");
      return 1;
    }

    // Crée le dossier build
    File buildDir = new File(path, "build");
    if (buildDir.exists()) {
      status = FileHandler.delete(buildDir);
      if (!status) {
        System.err.println("Error: Could not delete the build folder.");
        return 1;
      }
    }

    status = buildDir.mkdir();
    if (!status) {
      System.err.println("Error: Could not create build folder.");
      return 1;
    }

    // Création de la classe Site
    try{
      site = new Site(FileHandler.read(configFile), Paths.get(buildDir.getPath()));
    } catch (IOException e) {
      System.err.println("Cannot read config file. Aborting...");
      return 1;
    } catch (ValidationException e) {
      System.err.println(e.getViolatedSchema());
      return 1;
    }

    // Convertit les fichiers Markdown en HTML
    try {
      recursiveExploration("");
    } catch (Exception e) {
      System.err.println("Error: Could not build site : " + e.getMessage());
      FileHandler.delete(buildDir);
      return 1;
    }

    for (Page p : site.retrievePages()) {
      // convertit le fichier Markdown en HTML
      File htmlFile = new File(path + "/" + p.getPath().toString());

      String htmlContent = MarkdownParser.convertMarkdownToHTML(p.getMarkdown());

      try {
        FileHandler.write(htmlFile, htmlContent);
      } catch (IOException e) {
        System.err.println("Error while writing file " + p.getPath().toString());
      }
    }

    System.out.println("Site built.");
    return 0;
  }

  /**
   * Explore les fichiers Markdown du dossier courant et les convertit en HTML dans le dossier build
   *
   * @param folderPath le chemin du dossier courant
   */
  private void recursiveExploration(String folderPath) throws IOException, ParseException {
    boolean status;

    File[] files = new File(path, folderPath).listFiles();
    if (files == null) {
      throw new IOException("Could not list files.");
    }

    for (File f : files) {
      if (f.isDirectory()) {
        if (f.getName().equals("build")) continue;
        // crée un sous-dossier dans build
        String subFolder = folderPath + "/" + f.getName();
        status = new File(path, "build" + subFolder).mkdir();
        if (!status) {
          throw new IOException("Error: Could not create folder '" + subFolder + "'");
        }

        // explore le sous-dossier
        recursiveExploration(folderPath + "/" + f.getName());
      } else if (f.getName().endsWith(".md")) {
        String htmlFileName = f.getName().replace(".md", ".html");
        Path path = Paths.get("build" + folderPath + "/" + htmlFileName);

        try {
          Page page = new Page(FileHandler.read(f), path);
          site.addPage(page);
        } catch (ParseException e) {
          System.err.println(
              "Warning : Bad page format. " + f.getName() + " not generated. Continuing...");
        } catch (ValidationException e) {
          System.err.println(
              "Warning : Bad configuration in page "
                  + f.getName()
                  + ". Page not generated. Continuing...");
        } catch (IOException e) {
          System.err.println("Error while reading file " + f.getName() + ". Page not generated. Continuing...");
        }
      }
    }
  }
}
