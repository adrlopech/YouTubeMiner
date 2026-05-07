package aiss.YouTubeMiner.model.youtube;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YouTubeChannelList {
    @JsonProperty("items")
    private List<YouTubeChannel> items;

    public List<YouTubeChannel> getItems() { return items; }
    public void setItems(List<YouTubeChannel> items) { this.items = items; }
}