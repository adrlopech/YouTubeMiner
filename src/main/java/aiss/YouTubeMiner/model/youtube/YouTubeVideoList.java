package aiss.YouTubeMiner.model.youtube;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YouTubeVideoList {
    @JsonProperty("items")
    private List<YouTubeVideo> items;

    public List<YouTubeVideo> getItems() { return items; }
    public void setItems(List<YouTubeVideo> items) { this.items = items; }
}