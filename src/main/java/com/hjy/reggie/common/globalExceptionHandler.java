package com.hjy.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;

import java.sql.SQLIntegrityConstraintViolationException;

// 全局异常处理
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class globalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class) //异常处理方法
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception){
        log.error(exception.getMessage());
        return R.error("添加失败！！！");
    }
}
