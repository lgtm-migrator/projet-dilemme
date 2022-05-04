package ch.heigvd.dil.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/** Cette classe offre des méthodes pour écrire, lire et supprimer des fichiers. */
public class FileHandler {
  /**
   * Ecrit du contenu dans un fichier (Crée le fichier s'il n'existe pas)
   *
   * @param file le fichier dans lequel écrire
   * @param content le contenu du fichier
   */
  public static void write(File file, String content) throws IOException {
    BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8));
    bw.write(content);
    bw.close();
  }

  /**
   * Lit le contenu d'un fichier
   *
   * @param file le fichier à lire
   * @return le contenu du fichier
   */
  public static String read(File file) throws IOException {
    StringBuilder content = new StringBuilder();

    BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));

    String line;
    while ((line = br.readLine()) != null) {
      content.append(line).append("\n");
    }

    return content.toString();
  }

  /**
   * Supprime un fichier ou un dossier (ainsi que ses sous-dossiers)
   *
   * @param fileOrFolder le fichier ou dossier à supprimer
   * @return true si la suppression s'est bien passée, false sinon
   */
  public static boolean delete(File fileOrFolder) {
    if (fileOrFolder.isDirectory()) {
      File[] files = fileOrFolder.listFiles();
      if (files != null && files.length > 0) {
        for (File file : files) {
          delete(file);
        }
      }
    }
    return fileOrFolder.delete();
  }
}
