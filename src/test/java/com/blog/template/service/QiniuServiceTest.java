package com.blog.template.service;

import com.blog.template.BaseTest;
import com.blog.template.service.qiniu.QiniuService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

/**
 * @author: xch
 * @create: 2019-06-16 13:59
 **/
public class QiniuServiceTest extends BaseTest {

    @Autowired
    private QiniuService qiNiuService;

    @Test
    public void uploadFile() throws QiniuException {
        String filename = "C:\\Users\\19624\\Pictures\\Saved Pictures\\2.jpg";
        File file = new File(filename);
        Response response = qiNiuService.uploadFile(file);
        Assert.assertTrue(response.isOK());
    }
}
