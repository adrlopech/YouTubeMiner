package aiss.YouTubeMiner.etl;

import aiss.YouTubeMiner.model.videominer.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Transformer {

    public static Channel toChannel(String channelId, Map snippet) {
        Channel channel = new Channel();
        channel.setId(channelId);
        channel.setName((String) snippet.get("title"));
        channel.setDescription((String) snippet.get("description"));
        channel.setCreatedTime((String) snippet.get("publishedAt"));
        return channel;
    }

    public static Video toVideo(String videoId, Map videoSnippet) {
        Video video = new Video();
        video.setId(videoId);
        video.setName((String) videoSnippet.get("title"));
        video.setDescription((String) videoSnippet.get("description"));
        video.setReleaseTime((String) videoSnippet.get("publishedAt"));
        return video;
    }

    public static User toUser(String channelId, Map videoSnippet) {
        User user = new User();
        user.setId((String) videoSnippet.get("channelId"));
        user.setName((String) videoSnippet.get("channelTitle"));
        user.setUserLink("https://www.youtube.com/channel/" + channelId);
        Map thumbnails = (Map) videoSnippet.get("thumbnails");
        if (thumbnails != null) {
            Map defaultThumb = (Map) thumbnails.get("default");
            if (defaultThumb != null) {
                user.setPictureLink((String) defaultThumb.get("url"));
            }
        }
        return user;
    }

    public static Comment toComment(Map commentItem) {
        Map commentSnippet = (Map) commentItem.get("snippet");
        Map topLevel = (Map) commentSnippet.get("topLevelComment");
        Map topSnippet = (Map) topLevel.get("snippet");

        Comment comment = new Comment();
        comment.setId((String) topLevel.get("id"));
        comment.setText((String) topSnippet.get("textDisplay"));
        comment.setCreatedOn((String) topSnippet.get("publishedAt"));
        return comment;
    }

    public static Caption toCaption(String videoId, Map captionItem) {
        Map captionSnippet = (Map) captionItem.get("snippet");

        Caption caption = new Caption();
        caption.setId((String) captionItem.get("id"));
        caption.setLanguage((String) captionSnippet.get("language"));
        caption.setLink("https://www.youtube.com/watch?v=" + videoId);
        return caption;
    }
}