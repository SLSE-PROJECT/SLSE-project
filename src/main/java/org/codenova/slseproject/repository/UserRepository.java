package org.codenova.slseproject.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.codenova.slseproject.entity.User;

@Mapper
public interface UserRepository {

    void create(User user);

    User selectByEmail(String email);

    User selectById(Integer id);

    User selectByProviderAndProviderId(@Param("provider") String provider, @Param("providerId") String providerId);


    void updateSLSE(@Param("SLSE") Integer SLSE, @Param("userId") Integer userId);

    User selectByNickname(String nickname);

    void deductUserSLSE(@Param("userId") int userId, @Param("amount") int amount);


}
