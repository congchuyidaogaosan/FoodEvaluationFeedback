package com.restaurant.Controller.admin;

import com.restaurant.admin.Result;
import com.restaurant.domain.dto.EvaluationStatisticsDTO;
import com.restaurant.domain.dto.EvaluationTrendDTO;
import com.restaurant.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 统计数据控制器
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 获取评价统计概览
     */
    @GetMapping("/evaluation/overview")
    public Result getEvaluationOverview() {
        Map<String, Object> data = statisticsService.getEvaluationOverview();
        return Result.ok(data);
    }

    /**
     * 获取评价维度分析
     */
    @GetMapping("/evaluation/dimension")
    public Result getEvaluationDimension() {
        Map<String, Object> data = statisticsService.getEvaluationDimension();
        return Result.ok(data);
    }

    /**
     * 获取菜品评价统计数据
     */
    @GetMapping("/evaluation/dish/{dishId}")
    public Result getEvaluationStatistics(@PathVariable Integer dishId) {
        EvaluationStatisticsDTO statistics = statisticsService.getEvaluationStatisticsByDish(dishId);
        return Result.ok(statistics);
    }

    /**
     * 获取评价趋势数据
     */
    @GetMapping("/evaluation/trend")
    public Result getEvaluationTrend() {
        List<EvaluationTrendDTO> data = statisticsService.getEvaluationTrend();
        return Result.ok(data);
    }

    /**
     * 获取反馈统计数据
     */
    @GetMapping("/feedback")
    public Result getFeedbackStatistics() {
        Map<String, Object> data = statisticsService.getFeedbackStatistics();
        return Result.ok(data);
    }
} 