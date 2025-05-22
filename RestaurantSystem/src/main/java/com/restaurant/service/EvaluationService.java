package com.restaurant.service;

import com.restaurant.domain.Evaluation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.restaurant.domain.dto.EvaluationDTO;

import java.util.List;

/**
 *
 */
public interface EvaluationService extends IService<Evaluation> {
    /**
     * 获取用户的评价和对应菜品信息
     * @param studentId 学生ID
     * @return 评价和菜品信息列表
     */
    List<EvaluationDTO> getMyEvaluationsWithDish(Integer studentId);
    
    /**
     * 将评价实体列表转换为DTO列表
     * @param evaluations 评价实体列表
     * @return 评价DTO列表
     */
    List<EvaluationDTO> convertToDtoList(List<Evaluation> evaluations);
}
