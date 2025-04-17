package org.codenova.slseproject.entity.SLSELand;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codenova.slseproject.entity.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouletteHistory {
    private int id;
    private int userId;
    private String rewardType; // "slse", "coupon", "item"
    private int rewardValue;
    private LocalDateTime createdAt;
}

