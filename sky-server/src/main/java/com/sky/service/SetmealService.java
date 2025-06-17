package com.sky.service;


import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    void save(SetmealDTO setmealDTO);

    SetmealVO getById(Long id);

    void startOrStop(Integer status, Long id);

    void update(SetmealDTO setmealDTO);

    void deleteByIds(List<Integer> ids);
}
