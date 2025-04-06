package com.restaurant.Controller.wx;

import com.restaurant.admin.Result;
import com.restaurant.domain.DishCategory;
import com.restaurant.domain.SpecialDish;
import com.restaurant.service.DishCategoryService;
import com.restaurant.service.SpecialDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("SpecialDish")
@RestController
public class SpecialDishController {

    @Autowired
    private SpecialDishService dishService;


    @GetMapping()
    public Result list(){
        List<SpecialDish> list = dishService.list();
        return Result.ok(list);
    }

    @PostMapping("save")
    public Result save(@RequestBody SpecialDish dish){

        boolean save = dishService.save(dish);
        return Result.ok();
    }

    @PutMapping("update")
    public Result update(@RequestBody SpecialDish dish){

        boolean save = dishService.updateById(dish);
        return Result.ok();
    }

    @DeleteMapping("del/{id}")
    public Result del(@PathVariable("id")Integer id){

        boolean byId = dishService.removeById(id);
        return Result.ok();
    }

}
