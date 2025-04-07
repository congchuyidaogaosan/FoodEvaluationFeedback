package com.restaurant.Controller.wx;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.restaurant.admin.Result;
import com.restaurant.domain.Dish;
import com.restaurant.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/dishes")
@RestController
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping("category/{id}")
    public Result category(@PathVariable("id")Integer id){
        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id",id);
        List<Dish> list = dishService.list(queryWrapper);
        return Result.ok(list);
    }


    @GetMapping("special")
    public Result special(){

        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_special",1);
        List<Dish> list = dishService.list(queryWrapper);
        return Result.ok(list);
    }


    @GetMapping("")
    public Result list(){
        List<Dish> list = dishService.list();
        return Result.ok(list);
    }

    @PostMapping("save")
    public Result save(@RequestBody Dish dish){

        boolean save = dishService.save(dish);
        return Result.ok();
    }

    @PutMapping("update")
    public Result update(@RequestBody Dish dish){

        boolean save = dishService.updateById(dish);
        return Result.ok();
    }

    @DeleteMapping("del/{id}")
    public Result del(@PathVariable("id")Integer id){

        boolean byId = dishService.removeById(id);
        return Result.ok();
    }

}
