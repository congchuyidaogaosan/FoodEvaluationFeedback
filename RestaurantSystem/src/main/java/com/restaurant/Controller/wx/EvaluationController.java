package com.restaurant.Controller.wx;

import com.restaurant.admin.Result;
import com.restaurant.domain.Evaluation;
import com.restaurant.domain.Feedback;
import com.restaurant.service.EvaluationService;
import com.restaurant.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/evaluations")
@RestController
public class EvaluationController {

    @Autowired
    private EvaluationService dishService;

    @PostMapping("my")
    public Result my(){
        List<Evaluation> list = dishService.list();
        return Result.ok(list);
    }


    @GetMapping()
    public Result list(){
        List<Evaluation> list = dishService.list();
        return Result.ok(list);
    }

    @PostMapping("save")
    public Result save(@RequestBody Evaluation dish){

        boolean save = dishService.save(dish);
        return Result.ok();
    }

    @PutMapping("update")
    public Result update(@RequestBody Evaluation dish){

        boolean save = dishService.updateById(dish);
        return Result.ok();
    }

    @DeleteMapping("del/{id}")
    public Result del(@PathVariable("id")Integer id){

        boolean byId = dishService.removeById(id);
        return Result.ok();
    }

}
