package com.restaurant.Controller.wx;

import com.restaurant.admin.Result;
import com.restaurant.domain.DishCategory;
import com.restaurant.domain.Student;
import com.restaurant.service.DishCategoryService;
import com.restaurant.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/DishCategory")
@RestController
public class DishCategoryController {

    @Autowired
    private DishCategoryService dishService;


    @GetMapping()
    public Result list(){
        List<DishCategory> list = dishService.list();
        return Result.ok(list);
    }

    @PostMapping("save")
    public Result save(@RequestBody DishCategory dish){

        boolean save = dishService.save(dish);
        return Result.ok();
    }

    @PutMapping("update")
    public Result update(@RequestBody DishCategory dish){

        boolean save = dishService.updateById(dish);
        return Result.ok();
    }

    @DeleteMapping("del/{id}")
    public Result del(@PathVariable("id")Integer id){

        boolean byId = dishService.removeById(id);
        return Result.ok();
    }

}
