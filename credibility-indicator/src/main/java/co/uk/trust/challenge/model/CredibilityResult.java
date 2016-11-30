package co.uk.trust.challenge.model;

import java.util.List;
import java.util.Map;

/**
 * Created by huber on 29.11.16.
 */
public class CredibilityResult {
    private String url;
    private float credibility;
    private ProcessingResult processingResult;
    private List<String> links;
    private Map<NamedEntity, Float> entitiesOfInterest;
    private Map<String, ComparisonResult> relatedArticles;

    public CredibilityResult() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getCredibility() {
        return credibility;
    }

    public void setCredibility(float credibility) {
        this.credibility = credibility;
    }

    public ProcessingResult getProcessingResult() {
        return processingResult;
    }

    public void setProcessingResult(ProcessingResult processingResult) {
        this.processingResult = processingResult;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public Map<NamedEntity, Float> getEntitiesOfInterest() {
        return entitiesOfInterest;
    }

    public void setEntitiesOfInterest(Map<NamedEntity, Float> entitiesOfInterest) {
        this.entitiesOfInterest = entitiesOfInterest;
    }

    public Map<String, ComparisonResult> getRelatedArticles() {
        return relatedArticles;
    }

    public void setRelatedArticles(Map<String, ComparisonResult> relatedArticles) {
        this.relatedArticles = relatedArticles;
    }
}
