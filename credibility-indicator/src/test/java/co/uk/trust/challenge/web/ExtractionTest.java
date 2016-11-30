package co.uk.trust.challenge.web;

import co.uk.trust.challenge.model.ProcessingResult;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExtractionTest {
    @Test
    public void extractContent() throws Exception {
        ProcessingResult result = Extraction.extractContent("http://www.zeit.de/digital/datenschutz/2016-11/mitfahrgelegenheit-datenschutz-hackerangriff");

        assertNotNull("Extracted object should not be null", result);
        assertNotNull("Title should not be null", result.getTitle());
        assertNotNull("Text should not be null", result.getText());
        assertNotNull("HTML content should not be null", result.getHtmlContent());
        assertNotNull("URL should not be null", result.getUrl());

        System.out.println(result);
    }

    @Test
    public void extractLinks() throws Exception {
        ProcessingResult result = Extraction.extractContent("http://www.zeit.de/digital/datenschutz/2016-11/mitfahrgelegenheit-datenschutz-hackerangriff");
        result.setLinks(Extraction.extractLinks(result.getHtmlContent()));

        assertNotNull("Links should not be null", result.getLinks());
        assertFalse("Links should not be empty", result.getLinks().isEmpty());

        for (String link : result.getLinks()) {
            System.out.println(link);
        }
    }
}