package org.codenova.slseproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChampion {
    private int id;
    private int userId;
    private String championId;
    private String name;
    private String title;
    private String imageUrl;
    private String blurb;
    private int quantity;
    private int grade;
}