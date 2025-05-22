package com.restaurant.service;

import com.restaurant.domain.dto.EvaluationStatisticsDTO;
import com.restaurant.domain.dto.EvaluationTrendDTO;

import java.util.List;
import java.util.Map;

/**
 * 统计服务接口
 */
public interface StatisticsService {
    
    /**
     * 获取评价统计概览
     * @return 统计数据
     */
    Map<String, Object> getEvaluationOverview();
    
    /**
     * 获取评价维度分析
     * @return 维度分析数据
     */
    Map<String, Object> getEvaluationDimension();
    
    /**
     * 获取菜品评价统计数据
     * @param dishId 菜品ID
     * @return 评价统计数据
     */
    EvaluationStatisticsDTO getEvaluationStatisticsByDish(Integer dishId);
    
    /**
     * 获取评价趋势数据
     * @return 评价趋势数据
     */
    List<EvaluationTrendDTO> getEvaluationTrend();
    
    /**
     * 获取反馈统计数据
     * @return 反馈统计数据
     */
    Map<String, Object> getFeedbackStatistics();
} 