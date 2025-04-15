package org.codenova.slseproject.repository;

import org.apache.ibatis.annotations.Mapper;
import org.codenova.slseproject.entity.Champion;

import java.util.List;

@Mapper
public interface ChampionRepository {
    void insert (Champion champion);

    List<Champion> findAll();
}
