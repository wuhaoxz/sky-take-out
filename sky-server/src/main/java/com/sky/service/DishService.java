package com.sky.service;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface DishService {
    void save(DishDTO dishDTO);

    PageResult page(DishPageQueryDTO dto);

    void deleteBatch(List<Long> ids);

    DishVO getById(Long id);

    void update(DishDTO dishDTO);

    void startOrStop(Integer status, Long id);

    List<Dish> getByCategoryId(Integer categoryId);
}
