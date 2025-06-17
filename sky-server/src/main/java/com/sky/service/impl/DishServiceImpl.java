package com.sky.service.impl;


import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import net.sf.jsqlparser.statement.alter.EnableConstraint;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.wiring.BeanWiringInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Consumer;

@Service
public class DishServiceImpl implements DishService {


    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;


    @Transactional
    @Override
    public void save(DishDTO dishDTO) {

        //1.保存到dish表

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dish.setStatus(StatusConstant.DISABLE);//默认停售

        dishMapper.save(dish);

        //主键返回：获取insert语句生成的主键值
        Long dishId = dish.getId();


        //2.保存到dish_flavor表
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null && flavors.size()>0){

            flavors.forEach(new Consumer<DishFlavor>() {
                @Override
                public void accept(DishFlavor dishFlavor) {
                    dishFlavor.setDishId(dishId);
                }
            });

            dishFlavorMapper.saveBatch(flavors);
        }




    }



}
