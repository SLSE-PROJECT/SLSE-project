package org.codenova.slseproject.entity.vo;

import lombok.Data;

@Data
public class UserChampionAndPrice {
    private int id;
    private int userId;
    private String championId;
    private String name;
    private String title;
    private String imageUrl;
    private String blurb;
    private int price;
}
