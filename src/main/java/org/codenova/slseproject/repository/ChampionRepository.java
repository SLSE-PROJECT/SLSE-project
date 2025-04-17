package org.codenova.slseproject.repository;

import org.apache.ibatis.annotations.Mapper;
import org.codenova.slseproject.entity.Champion;

import java.util.List;

@Mapper
public interface ChampionRepository {

    List<Champion> findAll();
    List<Champion> selectNameAsc();
    List<Champion> selectNameDesc();
    List<Champion> selectPriceAsc();
    List<Champion> selectPriceDesc();

    Champion findByChampionId(String championId);
  
    Champion findByName(String name);

    List<Champion> selectByWord(String word);
}
