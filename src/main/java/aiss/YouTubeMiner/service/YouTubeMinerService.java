package aiss.YouTubeMiner.service;

import aiss.YouTubeMiner.model.youtube.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class YouTubeMinerService {

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${youtube.api.key}")
    private String apiKey;

    private static final String YT_BASE = "https://www.googleapis.com/youtube/v3";

    public YouTubeChannelList getChannel(String channelId) {
        String url = YT_BASE + "/channels?part=snippet&id=" + channelId + "&key=" + apiKey;
        return restTemplate.getForObject(url, YouTubeChannelList.class);
    }

    public YouTubeVideoList getVideosFromChannel(String channelId, int maxVideos) {
        String url = YT_BASE + "/search?part=snippet&channelId=" + channelId + "&type=video&maxResults=" + maxVideos + "&key=" + apiKey;
        return restTemplate.getForObject(url, YouTubeVideoList.class);
    }

    public YouTubeCommentList getComments(String videoId, int maxComments) {
        String url = YT_BASE + "/commentThreads?part=snippet&videoId=" + videoId + "&maxResults=" + maxComments + "&key=" + apiKey;
        return restTemplate.getForObject(url, YouTubeCommentList.class);
    }

    public YouTubeCaptionList getCaptions(String videoId) {
        String url = YT_BASE + "/captions?part=snippet&videoId=" + videoId + "&key=" + apiKey;
        return restTemplate.getForObject(url, YouTubeCaptionList.class);
    }
}