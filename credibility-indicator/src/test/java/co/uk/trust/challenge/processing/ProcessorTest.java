package co.uk.trust.challenge.processing;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by huber on 29.11.16.
 */
public class ProcessorTest {
    @Test
    public void filterLinks() throws Exception {
        String url = "http://www.zeit.de/digital/datenschutz/2016-11/mitfahrgelegenheit-datenschutz-hackerangriff";
        List<String> links = Arrays.asList("http://www.zeit.de/digital/internet/2016-03/mitfahrgelegenheit-31-maerz-blablacar",
        "https://www.mitfahrgelegenheit.de/Datenauskunft/",
        "http://www.spiegel.de/netzwelt/web/mitfahrgelegenheit-de-daten-von-hunderttausenden-mitgliedern-erbeutet-a-1123518.html",
        "https://t.co/W9wRPrZF11",
        "https://twitter.com/hashtag/mfgdaten?src=hash",
        "https://t.co/lpEcsoD4zE)",
        "http://mobile.zeit.de/test");

        List<String> filteredLinks = Processor.filterLinks(url, links);

        assertArrayEquals("Filtered Links should be correct",
                Arrays.asList("http://www.zeit.de/digital/internet/2016-03/mitfahrgelegenheit-31-maerz-blablacar", "http://mobile.zeit.de/test").toArray(),
                filteredLinks.toArray());
    }
}