package com.restaurant.Controller.wx;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.restaurant.admin.Result;
import com.restaurant.domain.LoginResponse;
import com.restaurant.domain.Student;
import com.restaurant.service.StudentService;
import com.restaurant.utill.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/WXLogin")
public class LoginController {

    private final String AppId = "wx958c331bb5b02d97";
    private final String AppSecret = "c7d7d587e080c1dda3b07c7a6ff47f96";

//    private final String AppSecret = "e7965d960c3a161ce50bff28eaf42b15";

//  孙appSecret  c7d7d587e080c1dda3b07c7a6ff47f96

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StudentService kehuService;

    @RequestMapping("setCode")
    public Result setCode(@RequestParam("code") String code) {

        System.out.println("code" + code);

        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("appid", AppId);
        params.add("secret", AppSecret);
        params.add("js_code", code);
        String url = "https://api.weixin.qq.com/sns/jscode2session";

        System.out.println(url + "?appid=" + AppId + "&secret=" + AppSecret + "&js_code=" + code);
        HttpHeaders headers = new HttpHeaders();
        //将请求头部和参数合成一个请求
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        try {
            System.out.println("response.getStatusCode() :" + response.getStatusCode());

            JSONObject jsonObject = JSON.parseObject(response.getBody());
            String session_key = (String) jsonObject.get("session_key");
            String openid = (String) jsonObject.get("openid");
            //这个注释解开 和这个方法里边的注释解开  可以进行用户自行保存
            Student kehuEntity = AddOrUpdate(openid, session_key);


            // 3. 生成token
            String token = jwtUtil.generateToken(kehuEntity);
            //暂时假用
//            String token = jwtUtil.generateToken(session_key, openid);

            // 4. 构建返回结果
            LoginResponse loginresponse = new LoginResponse();
            loginresponse.setToken(token);
            //记得换成用户   kehuEntity
            loginresponse.setUserInfo(kehuEntity);

            return Result.ok(loginresponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.fail("微信登入失败");
        }
    }


    private Student AddOrUpdate(String openID, String sessionKey) {
        Student kehuEntity = new Student();
        kehuEntity.setOpenId(openID);
        kehuEntity.setSessionkey(sessionKey);
        Student info = kehuService.getOne(new QueryWrapper<Student>().eq("open_id", openID));
        if (info == null) {
            kehuService.save(kehuEntity);
            info=kehuEntity;
        } else {
            kehuService.updateById(kehuEntity);
        }

        //   boolean openid = kehuService.saveOrUpdate(kehuEntity, new QueryWrapper<User>().eq("openid", openID));


        return info;
    }

}
