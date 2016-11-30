package co.uk.trust.challenge.nlp;

import co.uk.trust.challenge.model.NamedEntity;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by huber on 29.11.16.
 */
public class TXTWerkTest {
    TXTWerk txtWerk;

    @Before
    public void setup() {
        txtWerk = new TXTWerk();
    }

    @Test
    public void determineEntities() throws Exception {
        TxtWerkResult result = txtWerk.determineEntities("http://www.bbc.co.uk/news/world-latin-america-38140981");
        List<NamedEntity> entities = result.getEntities();
        String text = result.getText();

        assertNotNull("Entities should not be null", entities);
        assertNotNull("Text should not be null", text);
        assertFalse("Entities should not be empty", entities.isEmpty());

        System.out.println(text);
        for (NamedEntity entity : entities) {
            System.out.println(entity);
        }
    }
}