package com.restaurant.Controller.wx;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.restaurant.admin.Result;
import com.restaurant.domain.Dish;
import com.restaurant.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/dishes")
@RestController
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 获取菜品详情
     * @param id 菜品ID
     * @return 菜品详情
     */
    @GetMapping("{id}")
    public Result getById(@PathVariable("id") Integer id) {
        System.out.println("获取菜品详情, ID: " + id);
        
        if (id == null) {
            return Result.fail("菜品ID不能为空");
        }
        
        Dish dish = dishService.getById(id);
        if (dish == null) {
            return Result.fail("菜品不存在");
        }
        
        System.out.println("菜品详情: " + dish);
        return Result.ok(dish);
    }

    /**
     * 搜索菜品
     * @param keyword 搜索关键词
     * @return 搜索结果
     */
    @GetMapping("search")
    public Result search(@RequestParam(required = false) String keyword) {
        System.out.println("搜索菜品, 关键词: " + keyword);
        
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            // 根据菜名或原料进行模糊搜索
            queryWrapper.like(Dish::getDishName, keyword)
                    .or()
                    .like(Dish::getIngredients, keyword);
        }
        
        List<Dish> list = dishService.list(queryWrapper);
        System.out.println("搜索结果数量: " + list.size());
        
        return Result.ok(list);
    }

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
