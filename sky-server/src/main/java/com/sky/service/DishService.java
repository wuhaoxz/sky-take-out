package com.sky.service;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import org.springframework.stereotype.Service;


public interface DishService {
    void save(DishDTO dishDTO);

    PageResult page(DishPageQueryDTO dto);
}
