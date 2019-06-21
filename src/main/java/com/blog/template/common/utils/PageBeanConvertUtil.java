package com.blog.template.common.utils;

import com.blog.template.vo.PageBean;
import org.springframework.data.domain.Page;

/**
 * @author: xch
 * @create: 2019-06-21 12:49
 **/
public class PageBeanConvertUtil {

    public static PageBean convertCustomerPageBean(Page page){
        PageBean pageBean = new PageBean();
        pageBean.setContent(page.getContent());
        pageBean.setSize(page.getSize());
        pageBean.setPage(page.getNumber());
        pageBean.setTotalPage(page.getTotalPages());
        pageBean.setTotalSize(page.getTotalElements());
        return pageBean;
    }
}
