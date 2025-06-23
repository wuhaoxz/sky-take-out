package com.sky.mapper;

import com.sky.entity.Dish;
import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper {


    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    // 主键返回
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into user (openid, name,create_time) values (#{openid},#{name},#{createTime})")
    void save(User user1);

    @Select("select * from user where id = #{userId}")
    User getId(Long userId);


    Integer queryTotalUserNum(LocalDateTime end);
}
