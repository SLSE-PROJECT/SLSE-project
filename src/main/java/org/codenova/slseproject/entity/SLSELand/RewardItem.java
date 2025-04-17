package org.codenova.slseproject.entity.SLSELand;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardItem {
    private int id;
    private String name;
    private String imageUrl;
    private String description;
}