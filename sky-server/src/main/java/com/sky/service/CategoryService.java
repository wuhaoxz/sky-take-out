package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {
        PageResult page(CategoryPageQueryDTO categoryPageQueryDTO);

        void startOrStop(Integer status, Long id);

        void update(CategoryDTO categoryDTO);

        void save(CategoryDTO categoryDTO);

        void deleteById(Long id);

        List<Category> list(Integer type);
}