package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    @Insert("insert into shopping_cart(id, name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time)" +
            "values (#{id}, #{name}, #{image}, #{userId}, #{dishId}, " +
            "#{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{createTime}) ")
    void save(ShoppingCart shoppingCart);


    ShoppingCart getByCondition(ShoppingCart shoppingCart);

    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumber(ShoppingCart cart);


    @Select("select * from shopping_cart where user_id = #{userId}")
    List<ShoppingCart> list(Long userId);


    void deleteOne(ShoppingCart shoppingCart);

    @Delete("delete from shopping_cart where user_id = #{currentId}")
    void cleanAll(Long userId);

    void saveBatch(List<ShoppingCart> shoppingCartList);
}
