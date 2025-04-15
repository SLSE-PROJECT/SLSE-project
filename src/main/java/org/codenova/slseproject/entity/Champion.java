package org.codenova.slseproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Champion {
    private String id;
    private String name;
    private String title;
    private String blurb;

    private String imageUrl;

    private int price;
}
