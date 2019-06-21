package com.blog.template.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageBean<T>  {

    private List<T> content;  //内容列表

    private int size ;  //每页大小

    private int page; //当前页数

    private int totalPage;   //总的页数

    private long totalSize;   //总共的数量
}

