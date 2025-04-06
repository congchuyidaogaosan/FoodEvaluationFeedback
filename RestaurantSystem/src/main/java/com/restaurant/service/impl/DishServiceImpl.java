package com.restaurant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restaurant.domain.Dish;
import com.restaurant.service.DishService;
import com.restaurant.mapper.DishMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
    implements DishService{

}




