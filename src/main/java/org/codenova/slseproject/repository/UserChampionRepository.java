package org.codenova.slseproject.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.codenova.slseproject.entity.UserChampion;

import java.util.List;

@Mapper
public interface UserChampionRepository {

    void insert (UserChampion userChampion);

    void saveAll(@Param("userId") int userId, @Param("champions") List<UserChampion> champions);

    List<UserChampion> findByUserId(@Param("userId") int userId);

    UserChampion alreadyOwned(@Param("userId") Integer userId, @Param("championId") String championId);

    void update(UserChampion userChampion);
}
