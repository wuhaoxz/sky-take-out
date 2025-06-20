package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public void save(ShoppingCartDTO shoppingCartDTO) {

        //当前用户id
        Long userId = BaseContext.getCurrentId();

        ShoppingCart shoppingCart = new ShoppingCart();


        Long dishId = shoppingCartDTO.getDishId();
        Long setmealId = shoppingCartDTO.getSetmealId();

        if(dishId != null){//加购的菜品
            //查询购物车是否已经存在
            shoppingCart.setUserId(userId);
            shoppingCart.setDishFlavor(shoppingCartDTO.getDishFlavor());
            shoppingCart.setDishId(shoppingCartDTO.getDishId());
            ShoppingCart cart = shoppingCartMapper.getByCondition(shoppingCart);

            //如果购物车中已经存在这个菜品，就直接增加数量即可
            if(cart!=null){
                cart.setNumber(cart.getNumber()+1);
                shoppingCartMapper.updateNumber(cart);
                return;
            }
            //如果不存在
            Dish dish = dishMapper.getById(dishId);
            shoppingCart.setId(null);
            shoppingCart.setName(dish.getName());
            shoppingCart.setImage(dish.getImage());
            shoppingCart.setUserId(BaseContext.getCurrentId());
            shoppingCart.setDishId(dishId);
            shoppingCart.setSetmealId(null);
            shoppingCart.setDishFlavor(shoppingCartDTO.getDishFlavor());
            shoppingCart.setNumber(1);
            shoppingCart.setAmount(dish.getPrice());
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.save(shoppingCart);
            return;
        }
        else if(setmealId != null){//如果加购的是套餐


            //查询购物车是否已经存在
            shoppingCart.setUserId(userId);
            shoppingCart.setSetmealId(shoppingCartDTO.getSetmealId());
            ShoppingCart cart = shoppingCartMapper.getByCondition(shoppingCart);

            //如果购物车中已经存在这个套餐，就直接增加数量即可
            if(cart!=null){
                cart.setNumber(cart.getNumber()+1);
                shoppingCartMapper.updateNumber(cart);
                return;
            }

            //如果不存在

            Setmeal setmeal = setmealMapper.getById(setmealId);
            shoppingCart.setId(null);
            shoppingCart.setName(setmeal.getName());
            shoppingCart.setImage(setmeal.getImage());
            shoppingCart.setUserId(BaseContext.getCurrentId());
            shoppingCart.setDishId(null);
            shoppingCart.setSetmealId(setmealId);
            shoppingCart.setDishFlavor(null);
            shoppingCart.setNumber(1);
            shoppingCart.setAmount(setmeal.getPrice());
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.save(shoppingCart);
            return;
        }


    }

    @Override
    public List<ShoppingCart> list() {

        //获得当前请求的id
        Long userId = BaseContext.getCurrentId();

        List<ShoppingCart> list = shoppingCartMapper.list(userId);
        return list;


    }

    @Override
    public void deleteOne(ShoppingCartDTO shoppingCartDTO) {


        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        ShoppingCart targetCart = shoppingCartMapper.getByCondition(shoppingCart);
        //如果number ==1，直接删除
        if(targetCart.getNumber()==1){
            shoppingCartMapper.deleteOne(shoppingCart);
            return;
        }else if(targetCart.getNumber()>1){
            //如果number>1，update
            targetCart.setNumber(targetCart.getNumber()-1);
            shoppingCartMapper.updateNumber(targetCart);
        }




    }

    /**
     * 清空该用户的购物车
     */
    @Override
    public void clean() {
        shoppingCartMapper.cleanAll(BaseContext.getCurrentId());
    }
}
