package com.restaurant.Controller.wx;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.restaurant.admin.Result;
import com.restaurant.domain.Dish;
import com.restaurant.domain.Feedback;
import com.restaurant.domain.SpecialDish;
import com.restaurant.domain.query.FaterQuery;
import com.restaurant.service.FeedbackService;
import com.restaurant.service.SpecialDishService;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/notices")
@RestController
public class FeedbackController {

    @Autowired
    private FeedbackService dishService;

    @GetMapping()
    public Result list(FaterQuery faterQuery) {
        QueryWrapper<Feedback> queryWrapper = new QueryWrapper<>();
        if (faterQuery.getIsHandled()!=null && faterQuery.getIsHandled()!=0){
            queryWrapper.eq("is_handled",faterQuery.getIsHandled());
        }
        List<Feedback> list = dishService.list(queryWrapper);
        return Result.ok(list);
    }

    @GetMapping("/my")
    public Result myList(@RequestParam Integer studentId) {
        QueryWrapper<Feedback> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_id", studentId);
        queryWrapper.orderByDesc("feedback_time");
        List<Feedback> list = dishService.list(queryWrapper);
        return Result.ok(list);
    }

    @PostMapping("{id}/reply")
    public Result noticesreply(@PathVariable("id") Integer id, @RequestBody Feedback feedback) {
        feedback.setFeedbackId(id);
        dishService.updateById(feedback);
        return Result.ok("添加成功");
    }

    @PostMapping("save")
    public Result save(@RequestBody Feedback feedback) {
        boolean save = dishService.save(feedback);
        return Result.ok();
    }

    @PutMapping("update")
    public Result update(@RequestBody Feedback feedback) {
        boolean save = dishService.updateById(feedback);
        return Result.ok();
    }

    @DeleteMapping("del/{id}")
    public Result del(@PathVariable("id") Integer id) {
        boolean byId = dishService.removeById(id);
        return Result.ok();
    }
}
