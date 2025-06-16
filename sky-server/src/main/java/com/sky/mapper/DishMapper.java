package com.sky.mapper;

import com.sky.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {


    @Select("select count(*) from dish where category_id = #{id}")
    Integer countByCategoryId(Long id);
}
