package com.hjy.reggie.controller;

import com.hjy.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
    @GetMapping("/download")
    public void download(HttpServletResponse response, String name) throws IOException {
        try {
            FileInputStream fis = new FileInputStream(new File(basePath+ name));
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer))!=-1){
                outputStream.write(buffer,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fis.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
