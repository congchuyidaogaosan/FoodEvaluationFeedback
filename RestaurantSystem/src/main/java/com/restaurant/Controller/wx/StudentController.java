package com.restaurant.Controller.wx;

import com.restaurant.admin.Result;
import com.restaurant.domain.Dish;
import com.restaurant.domain.Student;
import com.restaurant.service.DishService;
import com.restaurant.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("Student")
@RestController
public class StudentController {

    @Autowired
    private StudentService dishService;


    @GetMapping()
    public Result list(){
        List<Student> list = dishService.list();
        return Result.ok(list);
    }

    @PostMapping("save")
    public Result save(@RequestBody Student dish){

        boolean save = dishService.save(dish);
        return Result.ok();
    }

    @PutMapping("update")
    public Result update(@RequestBody Student dish){

        boolean save = dishService.updateById(dish);
        return Result.ok();
    }

    @DeleteMapping("del/{id}")
    public Result del(@PathVariable("id")Integer id){

        boolean byId = dishService.removeById(id);
        return Result.ok();
    }

}
