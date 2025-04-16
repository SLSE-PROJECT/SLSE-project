package org.codenova.slseproject.repository;

import org.apache.ibatis.annotations.Mapper;
import org.codenova.slseproject.entity.ChampionPost;

@Mapper
public interface ChampionPostRepository {

    void insert(ChampionPost championPost);

    void updatePost();

}
