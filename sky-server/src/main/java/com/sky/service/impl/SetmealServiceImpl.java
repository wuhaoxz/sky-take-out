package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Consumer;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {

        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());

         Page<SetmealVO> page=setmealMapper.page(setmealPageQueryDTO);

        PageResult pageResult = new PageResult();
        pageResult.setRecords(page.getResult());
        pageResult.setTotal(pageResult.getTotal());

        return pageResult;
    }



    @Transactional
    @Override
    public void save(SetmealDTO setmealDTO) {

        //1.向setmeal表插入套数据
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmeal.setStatus(0);
        setmealMapper.save(setmeal);

        //主键返回
        Long setmealId = setmeal.getId();


        //2.向selmeal_dish表插入数据
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(new Consumer<SetmealDish>() {
            @Override
            public void accept(SetmealDish setmealDish) {
                setmealDish.setSetmealId(setmealId);
            }
        });

        setmealDishMapper.saveBatch(setmealDishes);

    }




    @Override
    public SetmealVO getById(Long id) {

        //1.查询setmeal表，获得基本信息
        Setmeal setmeal = setmealMapper.getById(id);

        //2.查询setmeal_dish表，获得套餐关联的菜品
        List<SetmealDish> setmealDishes =setmealDishMapper.getBySetMealId(id);

        SetmealVO setmealVO = new SetmealVO();

        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        setmealMapper.startOrStop(status,id);
    }




    @Transactional
    @Override
    public void update(SetmealDTO setmealDTO) {
        //1.修改setmeal表
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);

        setmealMapper.update(setmeal);

        //2.删除setmel_dish表中相关的数据
        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());

        //3.向setmel_dish中新增数据
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(new Consumer<SetmealDish>() {
            @Override
            public void accept(SetmealDish setmealDish) {
                setmealDish.setSetmealId(setmealDTO.getId());
            }
        });
        setmealDishMapper.saveBatch(setmealDishes);
    }
}
