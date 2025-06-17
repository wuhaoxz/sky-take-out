package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishMapper {




    @Select("select count(*) from dish where category_id = #{id}")
    Integer countByCategoryId(Long id);



    @Options(useGeneratedKeys = true,keyProperty = "id")
    @AutoFill(OperationType.INSERT)
    @Insert("insert into dish(name, category_id, price, image, description," +
            " status, create_time, update_time, create_user, update_user) " +
            "values (#{name}, #{categoryId}, #{price}, #{image}, #{description}," +
            "#{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void save(Dish dish);


    Page<DishVO> page(DishPageQueryDTO dto);


    void deleteBatch(List<Long> ids);


    Integer countStart(List<Long> ids);


    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    void update(DishDTO dishDTO);


    @Update("update dish set status = #{status} where id = #{id}")
    void startOrStop(@Param("status") Integer status,@Param("id") Long id);


    @Select("select * from dish where category_id = #{categoryId}")
    List<Dish> getByCategoryId(Integer categoryId);
}
