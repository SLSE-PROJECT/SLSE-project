package org.codenova.slseproject.repository;

import org.apache.ibatis.annotations.Mapper;
import org.codenova.slseproject.entity.User;

@Mapper
public interface UserRepository {

    int create(User user);
}
