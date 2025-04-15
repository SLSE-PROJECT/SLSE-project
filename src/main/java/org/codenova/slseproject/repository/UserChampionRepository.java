package org.codenova.slseproject.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.codenova.slseproject.entity.UserChampion;

import java.util.List;

@Mapper
public interface UserChampionRepository {
    void saveAll(@Param("userId") int userId, @Param("champions") List<UserChampion> champions);
    List<UserChampion> findByUserId(@Param("userId") int userId);
}
