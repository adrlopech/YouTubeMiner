package aiss.YouTubeMiner.etl;

import aiss.YouTubeMiner.model.videominer.*;
import aiss.YouTubeMiner.model.youtube.*;
import aiss.YouTubeMiner.service.YouTubeMinerService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Transformer {

    private final YouTubeMinerService youtubeService;

    public Transformer(YouTubeMinerService youtubeService) {
        this.youtubeService = youtubeService;
    }

    public Channel buildChannel(String channelId, Integer maxVideos, Integer maxComments) {
        YouTubeChannelList channelResponse = youtubeService.getChannel(channelId);

        if (channelResponse == null || channelResponse.getItems() == null || channelResponse.getItems().isEmpty()) {
            return null; // Canal no encontrado
        }

        YouTubeChannel ytChannel = channelResponse.getItems().get(0);
        YouTubeChannel.YouTubeChannelSnippet snippet = ytChannel.getSnippet();

        Channel channel = new Channel();
        channel.setId(channelId);
        channel.setName(snippet.getTitle());
        channel.setDescription(snippet.getDescription());
        channel.setCreatedTime(snippet.getPublishedAt() != null ? snippet.getPublishedAt() : "Unknown");

        YouTubeVideoList videosResponse = youtubeService.getVideosFromChannel(channelId, maxVideos);
        List<Video> videos = new ArrayList<>();

        if (videosResponse != null && videosResponse.getItems() != null) {
            for (YouTubeVideo ytVideo : videosResponse.getItems()) {
                String videoId = ytVideo.getId().getVideoId();
                YouTubeVideo.YouTubeVideoSnippet vSnippet = ytVideo.getSnippet();

                Video video = new Video();
                video.setId(videoId);
                video.setName(vSnippet.getTitle());
                video.setDescription(vSnippet.getDescription());
                video.setReleaseTime(vSnippet.getPublishedAt() != null ? vSnippet.getPublishedAt() : "Unknown");

                // Mapear Usuario
                User user = new User();
                user.setName(vSnippet.getChannelTitle());
                user.setUser_link("https://www.youtube.com/channel/" + vSnippet.getChannelId());
                if (vSnippet.getThumbnails() != null && vSnippet.getThumbnails().getDefaultThumbnail() != null) {
                    user.setPicture_link(vSnippet.getThumbnails().getDefaultThumbnail().getUrl());
                }
                video.setAuthor(user);

                // Mapear Comentarios
                List<Comment> comments = new ArrayList<>();
                try {
                    YouTubeCommentList commentsResponse = youtubeService.getComments(videoId, maxComments);
                    if (commentsResponse != null && commentsResponse.getItems() != null) {
                        for (YouTubeComment ytComment : commentsResponse.getItems()) {
                            YouTubeComment.TopLevelCommentSnippet cSnippet = ytComment.getSnippet().getTopLevelComment().getSnippet();
                            Comment comment = new Comment();
                            comment.setId(ytComment.getId());
                            comment.setText(cSnippet.getTextDisplay());
                            comment.setCreatedOn(cSnippet.getPublishedAt());
                            comments.add(comment);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Comentarios desactivados para el video: " + videoId);
                }
                video.setComments(comments);

                // Mapear Subtítulos
                List<Caption> captions = new ArrayList<>();
                try {
                    YouTubeCaptionList captionsResponse = youtubeService.getCaptions(videoId);
                    if (captionsResponse != null && captionsResponse.getItems() != null) {
                        for (YouTubeCaption ytCaption : captionsResponse.getItems()) {
                            Caption caption = new Caption();
                            caption.setId(ytCaption.getId());
                            caption.setName("https://www.youtube.com/watch?v=" + videoId);
                            caption.setLanguage(ytCaption.getSnippet().getLanguage());
                            captions.add(caption);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Subtítulos no disponibles para el video: " + videoId);
                }
                video.setCaptions(captions);

                videos.add(video);
            }
        }
        channel.setVideos(videos);
        return channel;
    }
}