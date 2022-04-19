package ch.heigvd.dil.utils.parsers;

import ch.heigvd.dil.data_structures.Page;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

import static org.junit.Assert.*;

public class PageContentSeparatorTest {

    private JSONObject validConfig = new JSONObject();
    private String validPageStr;
    private Page validPage;


    @Before
    public void genJSONs () {
        validConfig.put("title", "titre d'exemple");
        validConfig.put("author", "titre d'exemple");
        validConfig.put("date", "2022-10-12");
    }

    @Before
    public void readTestFiles () {

        try (BufferedReader br = new BufferedReader(
                new FileReader("testFiles/test-page/test-page-valid.md",
                        StandardCharsets.UTF_8))) {
            StringBuilder buffer = new StringBuilder();
            while (br.ready()) {
                buffer.append(br.readLine());
            }
            validPageStr = buffer.toString();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    @Test
    public void parseValidFileShouldNotThrowException() {
        boolean thrown = false;
        try{
            PageContentSeparator separator = new PageContentSeparator(validPageStr);
            separator.getContent();
            separator.getConfig();
        } catch (ParseException e) {
            thrown = true;
        }
        assertFalse(thrown);
    }

}