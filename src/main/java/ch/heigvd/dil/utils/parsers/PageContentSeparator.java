package ch.heigvd.dil.utils.parsers;
import java.text.ParseException;

public class PageContentSeparator {
    private final static String SEP = "---";
    private final String content;
    private final String config;
    public PageContentSeparator(String file) throws ParseException{
        int index = file.indexOf(SEP);
        if (index == -1) {
            throw new ParseException("Invalid page format", 0);
        }
        config = file.substring(0, index);
        content = file.substring(index + SEP.length());
    }

    public String getContent () {
        return content;
    }

    public String getConfig () {
        return config;
    }

}
