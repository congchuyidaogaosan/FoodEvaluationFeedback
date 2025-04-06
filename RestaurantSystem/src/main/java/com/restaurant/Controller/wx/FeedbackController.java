package com.restaurant.Controller.wx;

import com.restaurant.admin.Result;
import com.restaurant.domain.Feedback;
import com.restaurant.domain.SpecialDish;
import com.restaurant.service.FeedbackService;
import com.restaurant.service.SpecialDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("Feedback")
@RestController
public class FeedbackController {

    @Autowired
    private FeedbackService dishService;


    @GetMapping()
    public Result list(){
        List<Feedback> list = dishService.list();
        return Result.ok(list);
    }

    @PostMapping("save")
    public Result save(@RequestBody Feedback dish){

        boolean save = dishService.save(dish);
        return Result.ok();
    }

    @PutMapping("update")
    public Result update(@RequestBody Feedback dish){

        boolean save = dishService.updateById(dish);
        return Result.ok();
    }

    @DeleteMapping("del/{id}")
    public Result del(@PathVariable("id")Integer id){

        boolean byId = dishService.removeById(id);
        return Result.ok();
    }

}
