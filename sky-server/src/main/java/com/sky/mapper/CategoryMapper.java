package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {


    Page<Category> page(CategoryPageQueryDTO categoryPageQueryDTO);


    @Update("update category set status = #{status} where id = #{id}")
    void startOrStop(@Param("status") Integer status, @Param("id") Long id);



    @AutoFill(OperationType.UPDATE)
    void update(Category category);


    @AutoFill(OperationType.INSERT)
    @Insert("insert into category(type, name, sort, status, create_time, update_time, create_user, update_user)" +
            "values (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}," +
            " #{createUser}, #{updateUser})")
    void save(Category category);


    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);


    @Select("select * from category where type = #{type} and status = 1 order by sort asc,create_time desc")
    List<Category> list(Integer type);
}
