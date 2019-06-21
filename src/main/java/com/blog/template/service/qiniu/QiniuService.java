package com.blog.template.service.qiniu;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;

import java.io.File;
import java.io.InputStream;

/**
 * @author: xch
 * @create: 2019-06-16 16:39
 **/
public interface QiniuService {

    /**
     * 七牛云上传文件
     *
     * @param file 文件
     * @return 七牛上传Response
     * @throws QiniuException 七牛异常
     */
    Response uploadFile(File file) throws QiniuException;

    /**
     * 七牛云上传文件
     *
     * @param inputStream 文件输入流
     * @return 七牛上传Response
     * @throws QiniuException 七牛异常
     */
    Response uploadFile(InputStream inputStream) throws QiniuException;

    /**
     * @Description: 删除七牛云得上传文件
     * @Author: xch
     * @Date: 2019/6/16
     */
    Response delete(String key) throws QiniuException;

}
