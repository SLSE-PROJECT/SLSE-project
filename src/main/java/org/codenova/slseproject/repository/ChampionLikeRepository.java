package org.codenova.slseproject.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChampionLikeRepository {
    int insertLike(@Param("userId") int userId, @Param("championId") String championId);
    int deleteLike(@Param("userId") int userId, @Param("championId") String championId);
    boolean existsLike(@Param("userId") int userId, @Param("championId") String championId);
    List<String> selectLikedChampionIds(@Param("userId") int userId);
}