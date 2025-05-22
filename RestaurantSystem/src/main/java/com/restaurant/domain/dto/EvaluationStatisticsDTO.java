package com.restaurant.domain.dto;

import lombok.Data;

/**
 * 评价统计DTO
 */
@Data
public class EvaluationStatisticsDTO {
    
    /**
     * 评价总数
     */
    private Integer totalEvaluations;
    
    /**
     * 平均口味评分
     */
    private Float avgTasteScore;
    
    /**
     * 平均色泽评分
     */
    private Float avgColorScore;
    
    /**
     * 平均分量评分
     */
    private Float avgQuantityScore;
    
    /**
     * 平均性价比评分
     */
    private Float avgPricePerformanceScore;
} 