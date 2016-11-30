package co.uk.trust.challenge.model;

import java.util.List;

/**
 * Created by huber on 29.11.16.
 */
public class ProcessingResult {
    private String url;
    private String title;
    private String text;
    private String htmlContent;
    private List<NamedEntity> namedEntities;
    private List<String> links;

    public ProcessingResult() {

    }

    public ProcessingResult(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public List<NamedEntity> getNamedEntities() {
        return namedEntities;
    }

    public void setNamedEntities(List<NamedEntity> namedEntities) {
        this.namedEntities = namedEntities;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "ProcessingResult{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", htmlContent='" + htmlContent + '\'' +
                ", namedEntities=" + namedEntities +
                ", links=" + links +
                '}';
    }
}
