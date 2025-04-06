package com.restaurant.Controller.wx;

import com.restaurant.admin.Result;
import com.restaurant.domain.EvalStatistics;
import com.restaurant.domain.Evaluation;
import com.restaurant.service.EvalStatisticsService;
import com.restaurant.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("EvalStatistics")
@RestController
public class EvalStatisticsController {

    @Autowired
    private EvalStatisticsService dishService;


    @GetMapping()
    public Result list(){
        List<EvalStatistics> list = dishService.list();
        return Result.ok(list);
    }

    @PostMapping("save")
    public Result save(@RequestBody EvalStatistics dish){

        boolean save = dishService.save(dish);
        return Result.ok();
    }

    @PutMapping("update")
    public Result update(@RequestBody EvalStatistics dish){

        boolean save = dishService.updateById(dish);
        return Result.ok();
    }

    @DeleteMapping("del/{id}")
    public Result del(@PathVariable("id")Integer id){

        boolean byId = dishService.removeById(id);
        return Result.ok();
    }

}
