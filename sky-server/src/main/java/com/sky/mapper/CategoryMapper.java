package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CategoryMapper {


    Page<Category> page(CategoryPageQueryDTO categoryPageQueryDTO);

    @Update("update category set status = #{status} where id = #{id}")
    void startOrStop(@Param("status") Integer status, @Param("id") Long id);



    void update(Category category);

    @Insert("insert into category(type, name, sort, status, create_time, update_time, create_user, update_user)" +
            "values (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}," +
            " #{createUser}, #{updateUser})")
    void save(Category category);


    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);
}
