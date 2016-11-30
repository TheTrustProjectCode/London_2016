package co.uk.trust.challenge.web;

import co.uk.trust.challenge.model.ProcessingResult;
import de.l3s.boilerpipe.BoilerpipeExtractor;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.extractors.CommonExtractors;
import de.l3s.boilerpipe.sax.BoilerpipeSAXInput;
import de.l3s.boilerpipe.sax.HTMLDocument;
import de.l3s.boilerpipe.sax.HTMLFetcher;
import de.l3s.boilerpipe.sax.HTMLHighlighter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.swing.text.Document;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huber on 29.11.16.
 */
public class Extraction {
    public static void extract(String url) throws IOException, SAXException, BoilerpipeProcessingException {
        HTMLDocument htmlDoc = HTMLFetcher.fetch(new URL(url));
        TextDocument doc = new BoilerpipeSAXInput(htmlDoc.toInputSource()).getTextDocument();
    }

    public static ProcessingResult extractContent(String urlString) throws URISyntaxException, IOException, SAXException, BoilerpipeProcessingException {
        URL url = new URL(urlString);

        final HTMLDocument htmlDoc = HTMLFetcher.fetch(url);
        final BoilerpipeExtractor extractor = CommonExtractors.DEFAULT_EXTRACTOR;

        final HTMLHighlighter hh = HTMLHighlighter.newExtractingInstance();
        hh.setOutputHighlightOnly(true);

        TextDocument doc;

        String text = "";

        doc = new BoilerpipeSAXInput(htmlDoc.toInputSource()).getTextDocument();
        extractor.process(doc);
        final InputSource is = htmlDoc.toInputSource();
        text = hh.process(doc, is);

        ProcessingResult result = new ProcessingResult(urlString);
        result.setTitle(doc.getTitle());
        result.setText(doc.getContent());
        result.setHtmlContent(text);

        return result;
    }

    public static List<String> extractLinks(String html) {
        org.jsoup.nodes.Document doc = Jsoup.parse(html);

        List<String> result = new ArrayList<String>();
        Elements links = doc.select("a");
        for (Element linkElement : links) {
            result.add(linkElement.absUrl("href"));
        }

        return result;
    }
}
