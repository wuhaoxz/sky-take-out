package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;

public interface CategoryService {
        PageResult page(CategoryPageQueryDTO categoryPageQueryDTO);

        void startOrStop(Integer status, Long id);

        void update(CategoryDTO categoryDTO);

        void save(CategoryDTO categoryDTO);

        void deleteById(Long id);
    }