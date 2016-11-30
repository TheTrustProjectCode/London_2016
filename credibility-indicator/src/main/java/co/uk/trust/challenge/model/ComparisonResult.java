package co.uk.trust.challenge.model;

import java.util.Set;

public class ComparisonResult {
    private ProcessingResult processingResult;
    private Set<NamedEntity> overlappingEntities;
    private float score;

    public ComparisonResult() {
    }

    public ProcessingResult getProcessingResult() {
        return processingResult;
    }

    public void setProcessingResult(ProcessingResult processingResult) {
        this.processingResult = processingResult;
    }

    public Set<NamedEntity> getOverlappingEntities() {
        return overlappingEntities;
    }

    public void setOverlappingEntities(Set<NamedEntity> overlappingEntities) {
        this.overlappingEntities = overlappingEntities;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
