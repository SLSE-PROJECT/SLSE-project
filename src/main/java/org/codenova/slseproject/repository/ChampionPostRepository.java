package org.codenova.slseproject.repository;

import org.apache.ibatis.annotations.Mapper;
import org.codenova.slseproject.entity.ChampionPost;

import java.util.List;

@Mapper
public interface ChampionPostRepository {

    void insert(ChampionPost championPost);

    List<ChampionPost> selectByChampionId(String championId);

    ChampionPost selectById (Integer id);

    void updatePost(String content, Integer id);

    void deletePost(Integer id);

}
