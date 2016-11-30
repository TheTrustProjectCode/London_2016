package co.uk.trust.challenge.nlp;

import co.uk.trust.challenge.model.NamedEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by huber on 29.11.16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TxtWerkResult {
    private String text;
    private List<NamedEntity> entities;

    public TxtWerkResult() {

    }

    public String getText() {
        return text;
    }

    public List<NamedEntity> getEntities() {
        return entities;
    }
}
