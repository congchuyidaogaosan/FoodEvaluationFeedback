package com.restaurant.domain.dto;

import lombok.Data;

/**
 * 评价趋势DTO
 */
@Data
public class EvaluationTrendDTO {
    
    /**
     * 月份
     */
    private String month;
    
    /**
     * 评价数量
     */
    private Integer count;
} 