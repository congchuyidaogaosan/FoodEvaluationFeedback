package com.restaurant.Controller.wx;

import com.restaurant.admin.Result;
import com.restaurant.domain.Evaluation;
import com.restaurant.domain.dto.EvaluationDTO;
import com.restaurant.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

@RequestMapping("evaluations")
@RestController
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    /**
     * 获取当前用户的评价及对应菜品信息
     * @param requestMap 包含studentId的请求体
     * @return 评价和菜品信息
     */
    @PostMapping("my")
    public Result my(@RequestBody(required = false) Map<String, Object> requestMap){
        System.out.println("接收到的请求参数: " + requestMap);
        
        Integer studentId = null;
        if (requestMap != null && requestMap.containsKey("studentId")) {
            try {
                studentId = Integer.parseInt(requestMap.get("studentId").toString());
            } catch (Exception e) {
                System.out.println("解析studentId出错: " + e.getMessage());
            }
        }
        
        if (studentId == null) {
            System.out.println("studentId为空");
            return Result.fail("未提供学生ID");
        }
        
        System.out.println("查询用户ID: " + studentId + "的评价");
        List<EvaluationDTO> evaluationList = evaluationService.getMyEvaluationsWithDish(studentId);
        System.out.println("查询结果数量: " + (evaluationList != null ? evaluationList.size() : 0));
        
        return Result.ok(evaluationList);
    }

    @GetMapping()
    public Result list(){
        List<Evaluation> list = evaluationService.list();
        return Result.ok(list);
    }

    @PostMapping("save")
    public Result save(@RequestBody Evaluation evaluation){
        boolean save = evaluationService.save(evaluation);
        return Result.ok();
    }

    @PutMapping("update")
    public Result update(@RequestBody Evaluation evaluation){
        boolean save = evaluationService.updateById(evaluation);
        return Result.ok();
    }

    @DeleteMapping("del/{id}")
    public Result del(@PathVariable("id")Integer id){
        boolean byId = evaluationService.removeById(id);
        return Result.ok();
    }

    /**
     * 获取菜品的评价列表
     * @param dishId 菜品ID
     * @return 评价列表
     */
    @GetMapping("dish/{dishId}")
    public Result getDishEvaluations(@PathVariable("dishId") Integer dishId) {
        System.out.println("查询菜品ID: " + dishId + "的评价列表");
        
        if (dishId == null) {
            return Result.fail("未提供菜品ID");
        }
        
        // 查询指定菜品的评价
        LambdaQueryWrapper<Evaluation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Evaluation::getDishId, dishId);
        queryWrapper.orderByDesc(Evaluation::getEvalTime);
        List<Evaluation> evaluationList = evaluationService.list(queryWrapper);
        
        List<EvaluationDTO> dtoList = evaluationService.convertToDtoList(evaluationList);
        System.out.println("查询结果数量: " + (dtoList != null ? dtoList.size() : 0));
        
        return Result.ok(dtoList);
    }
}
