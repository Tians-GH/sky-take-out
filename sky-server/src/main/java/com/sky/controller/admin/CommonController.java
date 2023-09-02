package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @Author Tians
 * @Date 2023/9/2 16:48
 * @Version 17
 */
@Api(tags = "公共接口")
@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {
    @Autowired
    AliOssUtil aliOssUtil;

    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传：{}", file);
        try {
            // 原始文件名
            String originalFilename = file.getOriginalFilename();
            // 截取文件名
            String substring = originalFilename.substring(originalFilename.indexOf("."));
            // 构造新名字
            String fileName = UUID.randomUUID().toString() + substring;
            // 文件的请求路径
            String upload = aliOssUtil.upload(file.getBytes(), fileName);
            return Result.success(upload);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
