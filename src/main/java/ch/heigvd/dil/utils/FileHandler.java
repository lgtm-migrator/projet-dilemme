package ch.heigvd.dil.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Cette classe offre des méthodes pour écrire, lire et supprimer des fichiers.
 */
public class FileHandler {
    /**
     * Ecrit du contenu dans un fichier
     * (Crée le fichier s'il n'existe pas)
     *
     * @param file le fichier dans lequel écrire
     * @param content le contenu du fichier
     * @return true si l'écriture s'est bien passée, false sinon
     */
    static public boolean write(File file, String content) {
        try {
            BufferedWriter bw =
                    new BufferedWriter(
                            new FileWriter(file,StandardCharsets.UTF_8));
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Lit le contenu d'un fichier
     *
     * @param file le fichier à lire
     * @return le contenu du fichier
     * ou null si le fichier n'existe pas
     */
    static public String read(File file) {
        StringBuilder content = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(file, StandardCharsets.UTF_8));

            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            return null;
        }

        return content.toString();
    }

    /**
     * Supprime un fichier ou un dossier (ainsi que ses sous-dossiers)
     *
     * @param fileOrFolder le fichier ou dossier à supprimer
     */
    public static boolean delete(File fileOrFolder) {
        boolean status;
        if (fileOrFolder.isDirectory()) {
            File[] files = fileOrFolder.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    delete(file);
                }
            }
        }
        status = fileOrFolder.delete();
        return status;
    }

}
