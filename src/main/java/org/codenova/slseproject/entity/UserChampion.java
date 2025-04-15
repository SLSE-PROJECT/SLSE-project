package org.codenova.slseproject.entity;

import lombok.Data;

@Data

public class UserChampion {
    private int id;
    private int userId;
    private String championId;
    private String name;
    private String title;
    private String imageUrl;
}