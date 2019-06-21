package com.blog.template.service.qiniu;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;

/**
 * @author 19624
 */
@Service
@Slf4j
public class QiNiuServiceImpl implements InitializingBean,QiniuService {
    private final UploadManager uploadManager;

    private final Auth auth;

    @Value("${qiniu.bucket}")
    private String bucket;

    private StringMap putPolicy;


    private BucketManager bucketManager;

    @Autowired
    public QiNiuServiceImpl(UploadManager uploadManager, Auth auth, BucketManager bucketManager) {
        this.uploadManager = uploadManager;
        this.auth = auth;
        this.bucketManager = bucketManager;
    }

    /**
     * 七牛云上传文件
     *
     * @param file 文件
     * @return 七牛上传Response
     * @throws QiniuException 七牛异常
     */
    @Override
    public Response uploadFile(File file) throws QiniuException {
        Response response = this.uploadManager.put(file, null, getUploadToken());
        int retry = 0;
        while (response.needRetry() && retry < 3) {
            response = this.uploadManager.put(file, null, getUploadToken());
            retry++;
        }
        return response;
    }

    /**
     * 七牛云上传文件
     *
     * @param inputStream 文件输入流
     * @return 七牛上传Response
     * @throws QiniuException 七牛异常
     */
    @Override
    public Response uploadFile(InputStream inputStream) throws QiniuException {
        Response response = this.uploadManager.put(inputStream, null, getUploadToken(), putPolicy, null);
        int retry = 0;
        while (response.needRetry() && retry < 3) {
            response = this.uploadManager.put(inputStream, null, getUploadToken(), putPolicy, null);
            retry++;
        }
        return response;
    }


    /**
    * @Description: 删除七牛云得上传文件
    * @Author: xch
    * @Date: 2019/6/16
    */
    @Override
    public Response delete(String key) throws QiniuException {
        Response response = bucketManager.delete(bucket, key);
        int retry = 0;
        if (response.needRetry() && ++retry < 3) {
            response = bucketManager.delete(bucket, key);
        }
        return response;
    }


    @Override
    public void afterPropertiesSet() {
        this.putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"width\":$(imageInfo.width), \"height\":${imageInfo.height}}");
    }

    /**
     * 获取上传凭证
     *
     * @return 上传凭证
     */
    private String getUploadToken() {
        return this.auth.uploadToken(bucket, null, 3600, putPolicy);
    }
}
