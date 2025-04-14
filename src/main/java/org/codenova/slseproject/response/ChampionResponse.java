package org.codenova.slseproject.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.codenova.slseproject.entity.Champion;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChampionResponse {
    private String type;
    private Map<String, Champion> data;
}
