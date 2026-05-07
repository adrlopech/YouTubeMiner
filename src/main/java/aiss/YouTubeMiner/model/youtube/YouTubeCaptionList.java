package aiss.YouTubeMiner.model.youtube;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YouTubeCaptionList {
    @JsonProperty("items")
    private List<YouTubeCaption> items;

    public List<YouTubeCaption> getItems() { return items; }
    public void setItems(List<YouTubeCaption> items) { this.items = items; }
}