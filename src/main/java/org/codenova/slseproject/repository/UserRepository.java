package org.codenova.slseproject.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.codenova.slseproject.entity.User;

@Mapper
public interface UserRepository {

    int create(User user);

    User selectByEmail(String email);

    User selectByProviderAndProviderId(@Param("provider") String provider,
                                       @Param("providerId") String providerId);
}
