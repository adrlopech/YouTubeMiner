package aiss.YouTubeMiner.controller;

import aiss.YouTubeMiner.exception.ChannelNotFoundException;
import aiss.YouTubeMiner.model.videominer.Channel;
import aiss.YouTubeMiner.etl.Transformer;
import aiss.YouTubeMiner.service.VideoMinerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/youtubeminer/channels")
public class YouTubeMinerController {

    @Autowired
    private Transformer transformer;

    @Autowired
    private VideoMinerService videoMinerService;

    @GetMapping("/{channelId}")
    public ResponseEntity<Channel> previewChannel(
            @PathVariable String channelId,
            @RequestParam(defaultValue = "10") int maxVideos,
            @RequestParam(defaultValue = "2") int maxComments) throws ChannelNotFoundException {

        Channel channel = transformer.buildChannel(channelId, maxVideos, maxComments);

        if (channel == null) {
            throw new ChannelNotFoundException(channelId);
        }

        return ResponseEntity.ok(channel);
    }

    @PostMapping("/{channelId}")
    public ResponseEntity<Channel> mineAndSend(
            @PathVariable String channelId,
            @RequestParam(defaultValue = "10") int maxVideos,
            @RequestParam(defaultValue = "2") int maxComments) throws ChannelNotFoundException {

        Channel channel = transformer.buildChannel(channelId, maxVideos, maxComments);

        if (channel == null) {
            throw new ChannelNotFoundException(channelId);
        }

        Channel createdChannel = videoMinerService.postChannel(channel);

        return ResponseEntity.ok(createdChannel);
    }
}
