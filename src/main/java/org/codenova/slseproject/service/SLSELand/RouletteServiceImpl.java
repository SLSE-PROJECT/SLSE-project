package org.codenova.slseproject.service.SLSELand;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.codenova.slseproject.entity.SLSELand.RewardItem;
import org.codenova.slseproject.entity.SLSELand.RouletteHistory;
import org.codenova.slseproject.entity.SLSELand.UserCoupon;
import org.codenova.slseproject.entity.SLSELand.UserItem;
import org.codenova.slseproject.entity.User;
import org.codenova.slseproject.entity.vo.RouletteResult;
import org.codenova.slseproject.repository.RouletteRepository;
import org.codenova.slseproject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RouletteServiceImpl implements RouletteService {

    private final RouletteRepository rouletteRepository;
    private final UserRepository userRepository;
    private final Random random = new Random();

    @Override
    public RouletteResult spin(User user) {

        int cost = 10000;

        User dbUser = userRepository.selectById(user.getId());
        System.out.println(dbUser.toString());
        if (dbUser.getSLSE() < cost) {
            System.out.println("잔액부족");
            throw new IllegalStateException("잔액이 부족합니다.");
        }

        // 1. SLSE 차감 및 포인트 풀 적립
        rouletteRepository.deductUserSLSE(user.getId(), cost);
        rouletteRepository.increasePointPool(cost);

        // 2. 보상 뽑기
        RewardOption reward = drawReward();

        String rewardType = reward.getType();
        int rewardValue = reward.getValue();
        String rewardName = null;
        String rewardImage = null;
        String rewardDescription = null;

        switch (rewardType) {
            case "slse" -> {
                rouletteRepository.updateUserSLSE(user.getId(), rewardValue);
                rewardName = rewardValue + " SLSE";
            }
            case "coupon" -> {
                for (int i = 0; i < rewardValue; i++) {
                    UserCoupon coupon = new UserCoupon();
                    coupon.setUserId(user.getId());
                    rouletteRepository.insertCoupon(coupon);
                }
                rewardName = "적립금 응모권 " + rewardValue + "장";
            }
            case "item" -> {
                RewardItem item = rouletteRepository.selectRandomRewardItem();
                UserItem userItem = new UserItem();
                userItem.setUserId(user.getId());
                userItem.setItemId(item.getId());
                rouletteRepository.insertUserItem(userItem);

                rewardValue = item.getId();
                rewardName = item.getName();
                rewardImage = item.getImageUrl();
                rewardDescription = item.getDescription();
            }
        }

        // 3. 히스토리 저장
        RouletteHistory history = new RouletteHistory();
        history.setUserId(user.getId());
        history.setRewardType(rewardType);
        history.setRewardValue(rewardValue);
        rouletteRepository.insertHistory(history);

        return new RouletteResult(rewardType, rewardValue, rewardName, rewardImage, rewardDescription);
    }

    private RewardOption drawReward() {
        double rand = Math.random();
        double sum = 0;

        for (RewardOption r : RewardOption.values()) {
            sum += r.getProbability();
            if (rand < sum) return r;
        }
        return RewardOption.SLSE_5000; // fallback
    }

    @Getter
    @AllArgsConstructor
    private enum RewardOption {
        SLSE_5000("slse", 5000, 0.20),
        SLSE_10000("slse", 10000, 0.15),
        SLSE_20000("slse", 20000, 0.10),
        COUPON_1("coupon", 1, 0.10),
        COUPON_2("coupon", 2, 0.05),
        COUPON_3("coupon", 3, 0.05),
        COUPON_4("coupon", 4, 0.02),
        COUPON_5("coupon", 5, 0.01),
        RANDOM_ITEM("item", 0, 0.07);

        private final String type;
        private final int value;
        private final double probability;
    }
}