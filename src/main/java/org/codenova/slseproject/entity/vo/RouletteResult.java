package org.codenova.slseproject.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouletteResult {
    private String rewardType; // "slse", "coupon", "item"
    private int rewardValue;   // SLSE 금액 or itemId
    private String rewardName; // "적립금 쿠폰" or 아이템 이름

    private String rewardImage;
    private String rewardDescription;
}