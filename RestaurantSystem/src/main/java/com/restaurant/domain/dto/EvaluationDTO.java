package com.restaurant.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评价信息DTO，包含菜品信息
 */
@Data
public class EvaluationDTO implements Serializable {
    /**
     * 评价ID
     */
    private Integer evalId;

    /**
     * 学生ID
     */
    private Integer studentId;

    /**
     * 菜品ID
     */
    private Integer dishId;

    /**
     * 口味评分
     */
    private Byte tasteScore;

    /**
     * 色泽评分
     */
    private Byte colorScore;

    /**
     * 分量评分
     */
    private Byte quantityScore;

    /**
     * 性价比评分
     */
    private Byte pricePerformanceScore;

    /**
     * 评论内容
     */
    private String comment;

    /**
     * 评价时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date evalTime;

    /**
     * 菜品名称
     */
    private String dishName;

    /**
     * 菜品图片
     */
    private String dishImage;
    
    private static final long serialVersionUID = 1L;
} 