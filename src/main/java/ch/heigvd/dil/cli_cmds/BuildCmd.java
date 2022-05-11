package ch.heigvd.dil.cli_cmds;

import ch.heigvd.dil.utils.FileHandler;
import ch.heigvd.dil.utils.parsers.MarkdownParser;
import ch.heigvd.dil.utils.parsers.PageContentSeparator;
import java.io.*;
import java.text.ParseException;
import java.util.concurrent.Callable;
import picocli.CommandLine;

/**
 * Cette classe reproduit la structure du site et convertit les fichiers Markdown en HTML et
 * retourne le résultat dans le dossier "build"
 */
@CommandLine.Command(name = "build", description = "Build the website")
public class BuildCmd implements Callable<Integer> {
  // paramètre indiquant le site le chemin du site à initialiser
  @CommandLine.Parameters(description = "The site path to build")
  String path;

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
    for (File f : allFiles) {
      if (f.getName().equals("index.md") || f.getName().equals("config.json")) ++initFiles;
    }

    if (initFiles != 2) {
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

    // Convertit les fichiers Markdown en HTML
    try {
      recursiveExploration("");
    } catch (Exception e) {
      System.err.println("Error: Could not build site : " + e.getMessage());
      FileHandler.delete(buildDir);
      return 1;
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
        // convertit le fichier Markdown en HTML
        String htmlFileName = f.getName().replace(".md", ".html");
        File htmlFile = new File(path, "build" + folderPath + "/" + htmlFileName);

        String content = FileHandler.read(f);

        PageContentSeparator pageContent = new PageContentSeparator(content);

        String mdContent = pageContent.getContent();

        // TODO: lire les configs et les utiliser

        String htmlContent = MarkdownParser.convertMarkdownToHTML(mdContent);

        FileHandler.write(htmlFile, htmlContent);
      }
    }
  }
}
