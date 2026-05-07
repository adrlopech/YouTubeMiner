package aiss.YouTubeMiner.model.youtube;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YouTubeCommentList {
    @JsonProperty("items")
    private List<YouTubeComment> items;

    public List<YouTubeComment> getItems() { return items; }
    public void setItems(List<YouTubeComment> items) { this.items = items; }
}