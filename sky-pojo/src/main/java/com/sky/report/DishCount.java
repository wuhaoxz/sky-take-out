package com.sky.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DishCount implements Serializable {
    private Long dishId;//菜品id
    private Integer dishCount;//菜品卖出的个数
}
