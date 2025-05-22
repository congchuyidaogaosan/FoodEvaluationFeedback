package com.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.restaurant.domain.Dish;
import com.restaurant.domain.Evaluation;
import com.restaurant.domain.Feedback;
import com.restaurant.domain.dto.EvaluationStatisticsDTO;
import com.restaurant.domain.dto.EvaluationTrendDTO;
import com.restaurant.mapper.DishMapper;
import com.restaurant.mapper.EvaluationMapper;
import com.restaurant.mapper.FeedbackMapper;
import com.restaurant.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 统计服务实现类
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private EvaluationMapper evaluationMapper;
    
    @Autowired
    private DishMapper dishMapper;
    
    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public Map<String, Object> getEvaluationOverview() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取所有评价
        List<Evaluation> evaluations = evaluationMapper.selectList(null);
        
        // 计算总评价数
        result.put("totalEvaluations", evaluations.size());
        
        // 计算平均分
        double avgScore = evaluations.stream()
                .mapToDouble(e -> (e.getTasteScore() + e.getColorScore() + e.getQuantityScore() + e.getPricePerformanceScore()) / 4.0)
                .average()
                .orElse(0);
        result.put("avgScore", avgScore);
        
        // 计算本月评价数
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date monthStart = calendar.getTime();
        
        long monthlyCount = evaluations.stream()
                .filter(e -> e.getEvalTime().after(monthStart))
                .count();
        result.put("monthlyCount", monthlyCount);
        
        return result;
    }

    @Override
    public Map<String, Object> getEvaluationDimension() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取所有评价
        List<Evaluation> evaluations = evaluationMapper.selectList(null);
        
        if (evaluations.isEmpty()) {
            result.put("tasteScore", 0);
            result.put("colorScore", 0);
            result.put("quantityScore", 0);
            result.put("pricePerformanceScore", 0);
            return result;
        }
        
        // 计算各维度平均分
        double avgTasteScore = evaluations.stream()
                .mapToDouble(e -> e.getTasteScore())
                .average()
                .orElse(0);
        result.put("tasteScore", avgTasteScore);
        
        double avgColorScore = evaluations.stream()
                .mapToDouble(e -> e.getColorScore())
                .average()
                .orElse(0);
        result.put("colorScore", avgColorScore);
        
        double avgQuantityScore = evaluations.stream()
                .mapToDouble(e -> e.getQuantityScore())
                .average()
                .orElse(0);
        result.put("quantityScore", avgQuantityScore);
        
        double avgPricePerformanceScore = evaluations.stream()
                .mapToDouble(e -> e.getPricePerformanceScore())
                .average()
                .orElse(0);
        result.put("pricePerformanceScore", avgPricePerformanceScore);
        
        return result;
    }

    @Override
    public EvaluationStatisticsDTO getEvaluationStatisticsByDish(Integer dishId) {
        EvaluationStatisticsDTO dto = new EvaluationStatisticsDTO();
        
        // 查询指定菜品的所有评价
        LambdaQueryWrapper<Evaluation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Evaluation::getDishId, dishId);
        List<Evaluation> evaluations = evaluationMapper.selectList(queryWrapper);
        
        if (evaluations.isEmpty()) {
            dto.setTotalEvaluations(0);
            dto.setAvgTasteScore(0f);
            dto.setAvgColorScore(0f);
            dto.setAvgQuantityScore(0f);
            dto.setAvgPricePerformanceScore(0f);
            return dto;
        }
        
        dto.setTotalEvaluations(evaluations.size());
        
        // 计算各维度平均分
        float avgTasteScore = (float) evaluations.stream()
                .mapToDouble(e -> e.getTasteScore())
                .average()
                .orElse(0);
        dto.setAvgTasteScore(avgTasteScore);
        
        float avgColorScore = (float) evaluations.stream()
                .mapToDouble(e -> e.getColorScore())
                .average()
                .orElse(0);
        dto.setAvgColorScore(avgColorScore);
        
        float avgQuantityScore = (float) evaluations.stream()
                .mapToDouble(e -> e.getQuantityScore())
                .average()
                .orElse(0);
        dto.setAvgQuantityScore(avgQuantityScore);
        
        float avgPricePerformanceScore = (float) evaluations.stream()
                .mapToDouble(e -> e.getPricePerformanceScore())
                .average()
                .orElse(0);
        dto.setAvgPricePerformanceScore(avgPricePerformanceScore);
        
        return dto;
    }

    @Override
    public List<EvaluationTrendDTO> getEvaluationTrend() {
        // 获取所有评价
        List<Evaluation> evaluations = evaluationMapper.selectList(null);
        
        if (evaluations.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 按月份分组统计
        Map<String, Long> monthlyCount = evaluations.stream()
                .collect(Collectors.groupingBy(
                        e -> {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                            return sdf.format(e.getEvalTime());
                        },
                        Collectors.counting()
                ));
        
        // 转换为DTO列表
        List<EvaluationTrendDTO> result = monthlyCount.entrySet().stream()
                .map(entry -> {
                    EvaluationTrendDTO dto = new EvaluationTrendDTO();
                    dto.setMonth(entry.getKey());
                    dto.setCount(entry.getValue().intValue());
                    return dto;
                })
                .sorted(Comparator.comparing(EvaluationTrendDTO::getMonth))
                .collect(Collectors.toList());
        
        return result;
    }

    @Override
    public Map<String, Object> getFeedbackStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取所有反馈
        List<Feedback> feedbacks = feedbackMapper.selectList(null);
        
        // 计算总反馈数
        result.put("totalFeedbacks", feedbacks.size());
        
        // 计算已回复和未回复数量
        long repliedCount = feedbacks.stream()
                .filter(f -> f.getHandleResult() != null && !f.getHandleResult().isEmpty())
                .count();
        result.put("repliedCount", repliedCount);
        result.put("unrepliedCount", feedbacks.size() - repliedCount);
        
        // 计算本月反馈数
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date monthStart = calendar.getTime();
        
        long monthlyCount = feedbacks.stream()
                .filter(f -> f.getFeedbackTime().after(monthStart))
                .count();
        result.put("monthlyCount", monthlyCount);
        
        return result;
    }
} 