package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    void save(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> list();

    void deleteOne(ShoppingCartDTO shoppingCartDTO);

    void clean();
}
