package com.blog.template.controller.common;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.blog.template.service.qiniu.QiniuService;
import com.blog.template.vo.ResponseMsg;
import com.qiniu.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author: xch
 * @create: 2019-06-16 14:19
 **/
@RestController
@Slf4j
public class UploadController {

    @Autowired
    private QiniuService qiNiuService;

    @Value("${qiniu.prefix}")
    private String prefix;

    @PostMapping("uploadImg")
    @PreAuthorize("isAuthenticated()")
    public ResponseMsg uploadImg(@RequestParam("file") MultipartFile file) {
        Response response;
        if (file.isEmpty()) {
            return ResponseMsg.fail400("请上传文件");
        }
        try {
            response = qiNiuService.uploadFile(file.getInputStream());
            if (response.isOK()) {
                JSONObject jsonObject = JSONUtil.parseObj(response.bodyString());
                String yunFileName = jsonObject.getStr("key");
                String yunFilePath = StrUtil.appendIfMissing(prefix, "/") + yunFileName;
                return ResponseMsg.success200(Dict.create().set("yunFileName", yunFileName).set("yunFilePath", yunFilePath));
            } else {
                log.error("【七牛云】上传文件失败,{}", JSONUtil.toJsonStr(response));
                return ResponseMsg.fail500();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("【七牛云】上传文件失败,io异常");
            return ResponseMsg.fail500();
        }

    }
}
