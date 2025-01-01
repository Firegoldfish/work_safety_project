package com.hjy.reggie.controller;

import com.hjy.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${safety.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        log.info(file.toString());
        String originalFilename = file.getOriginalFilename();   //文件原名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString()+suffix; //新文件名
        File dir = new File(basePath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        try{
            file.transferTo(new File(basePath+ uuid));  //存到指定位置
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return R.success(uuid);
    }
}
