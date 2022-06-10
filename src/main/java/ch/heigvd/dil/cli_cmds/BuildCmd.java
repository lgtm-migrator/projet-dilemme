package ch.heigvd.dil.cli_cmds;

import static java.nio.file.StandardWatchEventKinds.*;

import ch.heigvd.dil.data_structures.Page;
import ch.heigvd.dil.data_structures.Site;
import ch.heigvd.dil.utils.FileHandler;
import ch.heigvd.dil.utils.TemplateInjector;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import org.everit.json.schema.ValidationException;
import org.json.JSONException;
import picocli.CommandLine;

/**
 * Cette classe reproduit la structure du site et convertit les fichiers Markdown en HTML et
 * retourne le résultat dans le dossier "build"
 */
@CommandLine.Command(name = "build", description = "Build the website")
public class BuildCmd implements Callable<Integer> {
  // paramètre indiquant le site le chemin du site à initialiser

  @CommandLine.Option(
      names = "--watch",
      description = "Automatically rebuild the " + "site when a file is changed")
  boolean watchOption;

  @CommandLine.Parameters(description = "The site path to build")
  String siteRoot;

  private Site site;

  @Override
  public Integer call() {
    if (!watchOption) {
      return buildWebsite();
    } else {
      return buildWithWatcher();
    }
  }

  /**
   * Construit le site générant les pages HTML à partir des fichiers Markdown (en utilisant des
   * templates). Le site est construit dans le dossier "build".
   *
   * @return 0 si tout s'est bien passé, 1 sinon.
   */
  int buildWebsite() {
    System.out.println("Building site...");

    File[] allFiles = new File(siteRoot).listFiles();

    if (allFiles == null) {
      System.err.println("Error: Site folder '" + siteRoot + "' does not exist");
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
    File buildDir = new File(siteRoot, "build");
    if (buildDir.exists()) {
      if (!FileHandler.delete(buildDir)) {
        System.err.println("Error: Could not delete the build folder.");
        return 1;
      }
    }

    if (!buildDir.mkdir()) {
      System.err.println("Error: Could not create build folder.");
      return 1;
    }

    // Création de la classe Site
    try {
      site = new Site(FileHandler.read(configFile), Paths.get(siteRoot));
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

    String layout;
    try {
      File templateDir = new File(siteRoot, "template/layout.html");
      layout = FileHandler.read(templateDir);
    } catch (Exception ignored) {
      // a try-catch inside a catch is a bit weird...
      try {
        layout = TemplateInjector.getDefaultLayout();
      } catch (IOException e) {
        System.err.println("Error: Could not read layout template.");
        FileHandler.delete(buildDir);
        return 1;
      }
    }

    //
    TemplateInjector ti = new TemplateInjector(site);
    for (Page p : site.retrievePages()) {
      try {
        p.generate(layout, ti, siteRoot); // Génère la page
      } catch (IOException e) {
        System.err.println("Error while writing file " + p.getPath().toString());
        return 1;
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
  private void recursiveExploration(String folderPath) throws IOException {
    boolean status;

    File[] files = new File(siteRoot, folderPath).listFiles();
    if (files == null) {
      throw new IOException("Could not list files.");
    }

    for (File f : files) {
      if (f.isDirectory()) {
        ArrayList<String> ignoredDirs = new ArrayList<>(Arrays.asList("build", "template"));
        if (ignoredDirs.contains(f.getName())) continue;
        // crée un sous-dossier dans build
        String subFolder = folderPath + "/" + f.getName();
        status = new File(siteRoot, "build" + subFolder).mkdir();
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
        } catch (ParseException | JSONException e) {
          System.out.println(
              "File "
                  + f.getName()
                  + " only copied because it is not a valid "
                  + "page. Continuing...");
          Files.copy(f.toPath(), Paths.get(siteRoot, "build" + folderPath + "/" + f.getName()));
        } catch (ValidationException e) {
          System.err.println(
              "Warning : Bad configuration in page "
                  + f.getName()
                  + ". Page not generated. Continuing...");
        } catch (IOException e) {
          System.err.println(
              "Error while reading file " + f.getName() + ". Page not generated. Continuing...");
        }
      } else if (!f.getName().equals("config.json")) { // les autres fichiers
        Files.copy(f.toPath(), Paths.get(siteRoot, "build" + folderPath + "/" + f.getName()));
      }
    }
  }

  /**
   * Construit le site avec un watcher (à chaque changement de fichier, le site est reconstruit)
   *
   * @return le code de retour de la commande
   */
  private int buildWithWatcher() {

    // Build le site une fois avant de commencer
    int buildingStatus = buildWebsite();
    if (buildingStatus != 0) {
      return buildingStatus;
    }

    Path dir;
    WatchService watchService;
    try {
      watchService = FileSystems.getDefault().newWatchService();

      dir = Paths.get(siteRoot);
      registerSubDirs(dir, watchService);
    } catch (IOException e) {
      System.err.println("Invalid path: " + siteRoot);
      return 1;
    }

    while (true) {
      WatchKey key;
      try {
        key = watchService.take();
      } catch (InterruptedException e) {
        System.out.println("Watcher interrupted");
        return 0;
      }

      String fileChanged = "";
      for (WatchEvent<?> event : key.pollEvents()) {
        fileChanged = event.context().toString();
      }

      // Ne reconstruit pas le site quand build est changé
      // (Sinon reconstruction en boucle)
      if (!fileChanged.equals("build")) {
        System.out.println("Change detected");
        buildingStatus = buildWebsite();
        if (buildingStatus != 0) {
          return buildingStatus;
        }
      }

      if (!key.reset()) return 0;
    }
  }

  /**
   * Enregistre tous les sous-dossiers du site dans le watcher
   *
   * @param startPath le dossier racine du site
   * @param watchService le watcher
   * @throws IOException si le dossier n'est pas trouvé
   */
  private void registerSubDirs(Path startPath, WatchService watchService) throws IOException {
    Files.walkFileTree(
        startPath,
        new FileVisitor<>() {
          @Override
          public FileVisitResult preVisitDirectory(
              Path path, BasicFileAttributes basicFileAttributes) throws IOException {

            // ignore inside build folder
            if (path.toString().equals(startPath + "/build")) return FileVisitResult.SKIP_SUBTREE;

            path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
            return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult visitFile(Path path, BasicFileAttributes attr) {
            return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult visitFileFailed(Path path, IOException e) {
            return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult postVisitDirectory(Path path, IOException e) {
            return FileVisitResult.CONTINUE;
          }
        });
  }
}
