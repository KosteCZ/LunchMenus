package cz.koscak.jan.slack;

import java.io.IOException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SlackUtils {
	
    //private static String slackWebhookUrl = "https://hooks.slack.com/services/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";
    private static String slackWebhookUrl = "https://hooks.slack.com/services/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";
    
    public static void sendMessage(SlackMessage message) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(slackWebhookUrl);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(message);

            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json; charset=utf-8");

            client.execute(httpPost);
            client.close();
        } catch (IOException e) {
           e.printStackTrace();
        }
    }
    
}
