package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SetmealMapper {


    @Select("select count(*) from setmeal where category_id = #{id}")
    Integer countByCategoryId(Long id);



    Page<SetmealVO> page(SetmealPageQueryDTO setmealPageQueryDTO);



    @Options(useGeneratedKeys = true,keyProperty = "id")
    @AutoFill(OperationType.INSERT)
    @Insert("insert into setmeal (category_id, name, price, description, image, " +
            "create_time, update_time, create_user, update_user,status) " +
            "values (#{categoryId}, #{name}, #{price}, #{description}, #{image}," +
            "#{createTime}, #{updateTime}, #{createUser}, #{updateUser},#{status});")
    void save(Setmeal setmeal);



    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);


    @Update("update setmeal set status = #{status} where id = #{id}")
    void startOrStop(@Param("status") Integer status,@Param("id") Long id);


    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);


    void deleteByIds(List<Integer> ids);


    @Select("select * from setmeal where category_id = #{categoryId}")
    List<Setmeal> getByCategoryId(Long categoryId);
}
