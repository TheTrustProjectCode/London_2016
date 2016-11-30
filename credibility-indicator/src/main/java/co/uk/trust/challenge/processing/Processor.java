package co.uk.trust.challenge.processing;

import co.uk.trust.challenge.model.ComparisonResult;
import co.uk.trust.challenge.model.CredibilityResult;
import co.uk.trust.challenge.model.NamedEntity;
import co.uk.trust.challenge.model.ProcessingResult;
import co.uk.trust.challenge.nlp.TxtWerkResult;
import co.uk.trust.challenge.nlp.TXTWerk;
import co.uk.trust.challenge.web.Extraction;

import java.util.*;
import java.util.regex.Pattern;

public class Processor {
    private static final float IMPORTANCE_THRESHOLD = .5f;
    private static final int MAX_ARTICLES = 3;
    private static final int MIN_ENTITIES = 5;
    private static final Set<String> EXCLUDED_TYPES = new HashSet<String>(Arrays.asList("NONE"));
    private TXTWerk txtWerk;

    public Processor() {
        txtWerk = new TXTWerk();
    }

    public CredibilityResult determineCredibility(String url) {
        CredibilityResult result = new CredibilityResult();
        result.setUrl(url);

        ProcessingResult processingResult = process(url);
        result.setProcessingResult(processingResult);

        Map<NamedEntity, Float> entitiesOfInterest = determineEntitiesOfInterest(processingResult);
        result.setEntitiesOfInterest(entitiesOfInterest);

        List<String> links = filterLinks(processingResult.getUrl(), processingResult.getLinks());
        result.setLinks(links);

        Map<String, ComparisonResult> relatedArticles = new TreeMap<String, ComparisonResult>();
        for (String link : links) {
            try {
                ProcessingResult linkResult = process(link);
                ComparisonResult comparisonResult = compareArticles(result, linkResult);

                relatedArticles.put(link, comparisonResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        result.setRelatedArticles(relatedArticles);

        calculateCredibility(result);

        return result;
    }

    private static ComparisonResult compareArticles(CredibilityResult result, ProcessingResult processingResult) {
        float sum = 0;
        float matchedSum = 0;

        Set<NamedEntity> matchedEntities = new HashSet<NamedEntity>();
        for (NamedEntity entity : result.getEntitiesOfInterest().keySet()) {
            float importance = result.getEntitiesOfInterest().get(entity);

            sum += importance;
            if (processingResult.getNamedEntities().contains(entity)) {
                matchedEntities.add(entity);
                matchedSum += importance;
            }
        }

        float overlap = sum > 0 ? matchedSum / sum : 0;

        ComparisonResult comparisonResult = new ComparisonResult();
        comparisonResult.setProcessingResult(processingResult);
        comparisonResult.setOverlappingEntities(matchedEntities);
        comparisonResult.setScore(overlap);

        return comparisonResult;
    }

    private static CredibilityResult calculateCredibility(CredibilityResult credibilityResult) {
        List<ComparisonResult> comparisonResults = new ArrayList<ComparisonResult>(credibilityResult.getRelatedArticles().values());
        Collections.sort(comparisonResults, new Comparator<ComparisonResult>() {
            public int compare(ComparisonResult o1, ComparisonResult o2) {
                return (int)Math.signum(o2.getScore() - o1.getScore());
            }
        });

        int relatedArticlesCounted = Math.min(comparisonResults.size(), MAX_ARTICLES);
        float sum = 0;
        for (int i = 0; i < relatedArticlesCounted; i++) {
            ComparisonResult comparisonResult = comparisonResults.get(i);
            sum += comparisonResult.getScore();
        }

        float average = sum > 0 ? sum / relatedArticlesCounted : 0;
        float score = Math.min(2 * average, 1);
        credibilityResult.setCredibility(score);

        return credibilityResult;
    }

    public ProcessingResult process(String url) {
        try {
            ProcessingResult result = Extraction.extractContent(url);
            result.setLinks(Extraction.extractLinks(result.getHtmlContent()));

            TxtWerkResult txtWerkResult = txtWerk.determineEntities(result.getUrl());
            //TxtWerkResult txtWerkResult = txtWerk.determineEntities(url);
            result.setNamedEntities(txtWerkResult.getEntities());
            result = postProcess(result);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ProcessingResult postProcess(ProcessingResult result) {
        result.setNamedEntities(filterEntities(result.getNamedEntities()));
        applyPopularityHeuristic(result.getNamedEntities());
        result.setHtmlContent(tagEntities(result.getHtmlContent(), result.getNamedEntities()));

        return result;
    }

    private static List<NamedEntity> filterEntities(List<NamedEntity> namedEntities) {
        List<NamedEntity> result = new ArrayList<NamedEntity>();

        Set<String> knownIds = new HashSet<String>();
        for (NamedEntity entity : namedEntities) {
            if (entity.getUri() != null && entity.getLabel() != null && entity.getId() != null
                    && !knownIds.contains(entity.getId())
                    && !EXCLUDED_TYPES.contains(entity.getType())) {
                result.add(entity);
                knownIds.add(entity.getId());
            }
        }

        return result;
    }

    private static List<NamedEntity> applyPopularityHeuristic(List<NamedEntity> namedEntities) {
        List<NamedEntity> result = new ArrayList<NamedEntity>();

        for (NamedEntity entity : namedEntities) {
            if (entity.getId().matches("Q\\d+")) {
                int id = Integer.parseInt(entity.getId().replace("Q", ""));
                float popularity = (float)Math.log(id) / (float)Math.log(10);
                entity.setPopularity(popularity);
            }
        }

        return result;
    }

    private static Map<NamedEntity, Float> determineEntitiesOfInterest(ProcessingResult processingResult) {
        Map<NamedEntity, Float> result = new TreeMap<NamedEntity, Float>();

        int textLength = processingResult.getText().length();
        for (NamedEntity entity : processingResult.getNamedEntities()) {
            if (processingResult.getTitle().contains(entity.getSurface())) {
                result.put(entity, 1f);
            } else {
                float importance = 1 - (float)entity.getStart() / (float)Math.max(textLength, 400);
                if (importance >= IMPORTANCE_THRESHOLD) {
                    result.put(entity, importance);
                }
            }
        }

        return result;
    }

    private static String extractDomain(String url) {
        return url.replaceAll("(\\.co)?\\.[^\\.]+/.*", "").replaceAll(".*://(www\\.)?", "");
    }

    public static List<String> filterLinks(String url, List<String> links) {
        List<String> result = new ArrayList<String>();
        String domain = extractDomain(url);
        for (String link : links) {
            String linkDomain = extractDomain(link);

            if (linkDomain.contains(domain) && link.contains("://") && !link.contains("author")) {
                result.add(link);
            }
        }

        return result;
    }

    public static String tagEntities(String html, List<NamedEntity> entities) {
        String result = html;
        for (NamedEntity entity : entities) {
            String replacement = String.format("<span class=\"%s\">%s</span>", entity.getType().toLowerCase(), entity.getSurface());
            result = result.replaceAll(String.format("(?<=\\b)%s(?=\\b)", Pattern.quote(entity.getSurface())), replacement);
        }

        return result;
    }
}
