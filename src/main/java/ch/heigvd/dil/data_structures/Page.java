package ch.heigvd.dil.data_structures;

import ch.heigvd.dil.utils.FileHandler;
import ch.heigvd.dil.utils.TemplateInjector;
import ch.heigvd.dil.utils.parsers.PageContentSeparator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import org.everit.json.schema.ValidationException;

/** Représente une page. */
public class Page {
  private final Page.Config pageConfig;
  private final String markdown;
  private final Path path;

  /**
   * Construit une page depuis le contenu Markdown et une configuration de page.
   *
   * @param pageConfig Chemin source de la page
   * @param markdown Contenu markdown dans le corps de la page
   */
  public Page(Config pageConfig, String markdown) {
    this.pageConfig = pageConfig;
    this.markdown = markdown;
    path = Paths.get("");
  }

  /**
   * Construit une page depuis une chaine de caractères et le chemin source.
   *
   * @param fileContent Contenu de la page (le markdown et le header JSON)
   * @param path Chemin source de la page
   */
  public Page(String fileContent, Path path) throws ParseException, ValidationException {
    PageContentSeparator sep = new PageContentSeparator(fileContent);
    pageConfig = sep.getConfig();
    markdown = sep.getContent();
    this.path = path;
  }

  /**
   * @return La configuration de la page
   */
  public Page.Config getPageConfig() {
    return pageConfig;
  }

  /**
   * @return Le chemin de la page
   */
  public Path getPath() {
    return path;
  }

  /**
   * @return Le contenu markdown de la page
   */
  public String getMarkdown() {
    return markdown;
  }

  public void generate(String layout, TemplateInjector ti, String sitePath) throws IOException {
    // convertit le fichier Markdown en HTML
    File htmlFile = new File(sitePath + "/" + getPath().toString());
    String htmlContent = ti.resolveProperties(layout, this);
    FileHandler.write(htmlFile, htmlContent);
  }

  /** Représente la configuration d'une page. */
  public static class Config {
    private String title;
    private String author;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    /**
     * Construit une configuration de page.
     *
     * @param title Le titre de la page
     * @param author L'auteur de la page
     * @param date La date de création de la page
     */
    public Config(String title, String author, LocalDate date) {
      this.title = title;
      this.author = author;
      this.date = date;
    }

    public Config() {}

    /**
     * @return Le titre de la page
     */
    public String getTitle() {
      return title;
    }

    /**
     * @return L'auteur de la page
     */
    public String getAuthor() {
      return author;
    }

    /**
     * @return La date de création de la page
     */
    public LocalDate getDate() {
      return date;
    }

    /**
     * @return Un string représentant la configuration de la page au format JSON
     */
    public String getJSON() {
      return "{\n  \"title\": \""
          + title
          + "\",\n  \"author\": \""
          + author
          + "\",\n  "
          + "\"date\": \""
          + date.toString() // au format yyyy-MM-dd
          + "\"\n}";
    }
  }
}
