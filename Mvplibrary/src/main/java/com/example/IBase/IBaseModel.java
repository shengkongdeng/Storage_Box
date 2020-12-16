package com.example.IBase;

import io.reactivex.disposables.Disposable;

/**
 * 创建者: 走廊里の声控灯
 * 项目名: ToyBox
 * 包名: com.example.Model
 * 创建时间: 2020/11/24 8:47
 */
public interface IBaseModel {
    void addDisposable(Disposable disposable);
    void clear();
}
