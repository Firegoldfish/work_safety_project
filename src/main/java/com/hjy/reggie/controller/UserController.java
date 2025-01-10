package com.hjy.reggie.controller;

import com.hjy.reggie.common.R;
import com.hjy.reggie.entity.User;
import com.hjy.reggie.service.UserService;
import com.hjy.reggie.utils.SMSUtils;
import com.hjy.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    //发验证码
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            //生成4位验证码
            String code = ValidateCodeUtils.generateValidateCode(6).toString();
            log.info("code:{}",code);
            //调用阿里云短信API
            SMSUtils.sendMessage("喂宠系统", "SMS_310340917", phone, code);
            //保存验证码到session
            session.setAttribute(phone, code);
            return R.success("发送成功");
        }
        return R.error("发送失败");


    }
}
