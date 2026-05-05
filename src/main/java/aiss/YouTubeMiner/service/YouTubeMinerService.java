package aiss.YouTubeMiner.service;

import aiss.YouTubeMiner.etl.Transformer;
import aiss.YouTubeMiner.model.videominer.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class YouTubeMinerService {

    @Value("${youtube.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String YT_BASE = "https://www.googleapis.com/youtube/v3";

    public Channel getChannel(String channelId, int maxVideos, int maxComments) {

        // 1. Obtener datos del canal
        String channelUrl = YT_BASE + "/channels?part=snippet&id=" + channelId + "&key=" + apiKey;
        Map response = restTemplate.getForObject(channelUrl, Map.class);

        List<Map> items = (List<Map>) response.get("items");
        if (items == null || items.isEmpty()) {
            return null;
        }

        Map channelItem = items.get(0);
        Map snippet = (Map) channelItem.get("snippet");

        // Mapear Channel usando Transformer
        Channel channel = Transformer.toChannel(channelId, snippet);

        // 2. Obtener videos del canal
        String videosUrl = YT_BASE + "/search?part=snippet&channelId=" + channelId
                + "&type=video&maxResults=" + maxVideos + "&key=" + apiKey;
        Map videosResponse = restTemplate.getForObject(videosUrl, Map.class);
        List<Map> videoItems = (List<Map>) videosResponse.get("items");

        List<Video> videos = new ArrayList<>();

        if (videoItems != null) {
            for (Map videoItem : videoItems) {
                Map idMap = (Map) videoItem.get("id");
                String videoId = (String) idMap.get("videoId");
                Map videoSnippet = (Map) videoItem.get("snippet");

                // Mapear Video y User usando Transformer
                Video video = Transformer.toVideo(videoId, videoSnippet);
                video.setUser(Transformer.toUser(channelId, videoSnippet));

                // 3. Obtener comentarios
                List<Comment> comments = new ArrayList<>();
                try {
                    String commentsUrl = YT_BASE + "/commentThreads?part=snippet&videoId=" + videoId
                            + "&maxResults=" + maxComments + "&key=" + apiKey;
                    Map commentsResponse = restTemplate.getForObject(commentsUrl, Map.class);
                    List<Map> commentItems = (List<Map>) commentsResponse.get("items");

                    if (commentItems != null) {
                        for (Map commentItem : commentItems) {
                            comments.add(Transformer.toComment(commentItem));
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Comments disabled for video: " + videoId);
                }
                video.setComments(comments);

                // 4. Obtener captions
                List<Caption> captions = new ArrayList<>();
                try {
                    String captionsUrl = YT_BASE + "/captions?part=snippet&videoId=" + videoId + "&key=" + apiKey;
                    Map captionsResponse = restTemplate.getForObject(captionsUrl, Map.class);
                    List<Map> captionItems = (List<Map>) captionsResponse.get("items");

                    if (captionItems != null) {
                        for (Map captionItem : captionItems) {
                            captions.add(Transformer.toCaption(videoId, captionItem));
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Captions not available for video: " + videoId);
                }
                video.setCaptions(captions);

                videos.add(video);
            }
        }

        channel.setVideos(videos);
        return channel;
    }
}