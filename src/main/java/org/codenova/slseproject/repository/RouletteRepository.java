package org.codenova.slseproject.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.codenova.slseproject.entity.SLSELand.RewardItem;
import org.codenova.slseproject.entity.SLSELand.RouletteHistory;
import org.codenova.slseproject.entity.SLSELand.UserCoupon;
import org.codenova.slseproject.entity.SLSELand.UserItem;

@Mapper
public interface RouletteRepository {

    void increasePointPool(@Param("amount") int amount);

    void insertHistory(RouletteHistory history);

    void insertCoupon(UserCoupon coupon);

    void deductUserSLSE(@Param("userId") int userId, @Param("amount") int amount);

    void updateUserSLSE(@Param("userId") int userId, @Param("amount") int amount);

    RewardItem selectRewardItemById(@Param("id") int id);

    UserCoupon selectCouponByUserId(Integer userId);

    void insertUserItem(UserItem item);

    RewardItem selectRandomRewardItem();
}