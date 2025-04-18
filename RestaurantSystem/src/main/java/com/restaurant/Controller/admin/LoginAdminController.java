package com.restaurant.Controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.restaurant.admin.Result;
import com.restaurant.domain.Admin;
import com.restaurant.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/LoginAdmin")
@RestController
public class LoginAdminController {


    @Autowired
    private AdminService adminService;

    @RequestMapping("login")
    public Result login(@RequestBody Admin admin){

        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",admin.getUsername());
        queryWrapper.eq("password",admin.getPassword());


        List<Admin> list = adminService.list(queryWrapper);

        if (list!=null && list.size()==1){
            return   Result.ok(list.get(0));
        }else if (list!=null &&  list.size()>=1){
            return  Result.ok(list.get(0));
        }
        return Result.ok();

    }

}
