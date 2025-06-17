package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.BaseException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
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

    @Override
    public PageResult page(DishPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());

        Page<DishVO> page = dishMapper.page(dto);

        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;
    }

    @Transactional
    @Override
    public void deleteBatch(List<Long> ids) {

        //如果有的菜品处于起售，不能删除
        //获得起售中的菜品的数量
        Integer count = dishMapper.countStart(ids);
        if(count>0){
            throw new BaseException("删除失败，存在起售中的菜品");
        }



        //TODO 如果菜品关联了套餐，则不允许删除





        //删除菜品
        dishMapper.deleteBatch(ids);

        //删除对应的口味
        dishFlavorMapper.deleteBatch(ids);



    }

    @Override
    public DishVO getById(Long id) {


        //查询dish表，获得菜品信息
        Dish dish = dishMapper.getById(id);

        //查询dish_flavor表，获得该菜品的口味信息
        List<DishFlavor> list = dishFlavorMapper.getByDishId(id);


        //封装成dishVO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(list);

        return dishVO;
    }

    @Override
    public void update(DishDTO dishDTO) {

        //更新dish表
        dishMapper.update(dishDTO);

        //删除dish_flavor表中该菜品id对应的全部口味
        dishFlavorMapper.deleteByDishId(dishDTO.getId());


        //将新的口味插入dish_flavor表
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.forEach(new Consumer<DishFlavor>() {
            @Override
            public void accept(DishFlavor dishFlavor) {
                dishFlavor.setDishId(dishDTO.getId());
            }
        });
        dishFlavorMapper.saveBatch(flavors);

    }

    @Override
    public void startOrStop(Integer status, Long id) {
        dishMapper.startOrStop(status,id);
    }


}
