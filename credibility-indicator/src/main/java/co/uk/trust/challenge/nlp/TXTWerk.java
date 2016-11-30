package co.uk.trust.challenge.nlp;

import co.uk.trust.challenge.model.NamedEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huber on 29.11.16.
 */
public class TXTWerk {
    private CloseableHttpClient httpClient;
    private ObjectMapper mapper;
    private Map<String, TxtWerkResult> cache;

    public TXTWerk() {
        httpClient = HttpClients.createDefault();
        mapper = new ObjectMapper();
        cache = new HashMap<String, TxtWerkResult>();
    }

    public TxtWerkResult determineEntities(String url) throws IOException {
        if (cache.containsKey(url)) return cache.get(url);
        HttpPost httpPost = new HttpPost("https://api.neofonie.de/rest/txt/analyzer");
        httpPost.addHeader("X-Api-Key", "d9360ee5-7182-e7b0-8fbc-e2ab8ea45791");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("services", "entities"));
        nvps.add(new BasicNameValuePair("url", url));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse response = httpClient.execute(httpPost);

        try {
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            String json = EntityUtils.toString(entity);

            TxtWerkResult result = mapper.readValue(json, TxtWerkResult.class);

            for (NamedEntity ne : result.getEntities()) {
                if (ne.getUri() != null) {
                    ne.setId(ne.getUri().replaceAll(".*/", ""));
                }
            }

            cache.put(url, result);
            return result;
        } finally {
            response.close();
        }
    }
}
