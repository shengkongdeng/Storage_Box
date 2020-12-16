package com.example.listAdapter;

/**
 * 创建者: Administrator
 * 项目名: ChildrenSongsTap
 * 包名: com.example.listAdapter
 * 创建时间: 2020/11/11 16:30
 */
public interface CommonType<D> {
    //根据不同类型的返回对应的布局
    int getTypeLayoutId(int viewType);
    //返回类型
    int getType(int positon, D data);
}
