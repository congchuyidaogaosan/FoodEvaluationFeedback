package com.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restaurant.domain.Dish;
import com.restaurant.domain.Evaluation;
import com.restaurant.domain.dto.EvaluationDTO;
import com.restaurant.mapper.DishMapper;
import com.restaurant.service.EvaluationService;
import com.restaurant.mapper.EvaluationMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class EvaluationServiceImpl extends ServiceImpl<EvaluationMapper, Evaluation>
    implements EvaluationService{

    @Autowired
    private DishMapper dishMapper;

    @Override
    public List<EvaluationDTO> getMyEvaluationsWithDish(Integer studentId) {
        // 1. 查询用户的所有评价
        LambdaQueryWrapper<Evaluation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Evaluation::getStudentId, studentId);
        queryWrapper.orderByDesc(Evaluation::getEvalTime);
        List<Evaluation> evaluations = this.list(queryWrapper);
        
        if (evaluations == null || evaluations.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 2. 获取所有涉及到的菜品ID
        List<Integer> dishIds = evaluations.stream()
                .map(Evaluation::getDishId)
                .collect(Collectors.toList());
        
        // 3. 查询这些菜品信息
        List<Dish> dishes = dishMapper.selectBatchIds(dishIds);
        Map<Integer, Dish> dishMap = dishes.stream()
                .collect(Collectors.toMap(Dish::getDishId, dish -> dish));
        
        // 4. 组装DTO返回
        List<EvaluationDTO> resultList = evaluations.stream().map(evaluation -> {
            EvaluationDTO dto = new EvaluationDTO();
            BeanUtils.copyProperties(evaluation, dto);
            
            // 获取对应的菜品信息
            Dish dish = dishMap.get(evaluation.getDishId());
            if (dish != null) {
                dto.setDishName(dish.getDishName());
                dto.setDishImage(dish.getUrl());
            }
            
            return dto;
        }).collect(Collectors.toList());
        
        return resultList;
    }

    @Override
    public List<EvaluationDTO> convertToDtoList(List<Evaluation> evaluations) {
        if (evaluations == null || evaluations.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取所有涉及到的菜品ID
        List<Integer> dishIds = evaluations.stream()
                .map(Evaluation::getDishId)
                .collect(Collectors.toList());
        
        // 查询这些菜品信息
        List<Dish> dishes = dishMapper.selectBatchIds(dishIds);
        Map<Integer, Dish> dishMap = dishes.stream()
                .collect(Collectors.toMap(Dish::getDishId, dish -> dish, (k1, k2) -> k1));
        
        // 组装DTO返回
        return evaluations.stream().map(evaluation -> {
            EvaluationDTO dto = new EvaluationDTO();
            BeanUtils.copyProperties(evaluation, dto);
            
            // 获取对应的菜品信息
            Dish dish = dishMap.get(evaluation.getDishId());
            if (dish != null) {
                dto.setDishName(dish.getDishName());
                dto.setDishImage(dish.getUrl());
            }
            
            return dto;
        }).collect(Collectors.toList());
    }
}




