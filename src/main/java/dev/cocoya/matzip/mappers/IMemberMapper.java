package dev.cocoya.matzip.mappers;

import dev.cocoya.matzip.entities.member.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IMemberMapper {
    int insertUser(UserEntity user);

    UserEntity selectUserById(@Param(value = "id") String id);
}