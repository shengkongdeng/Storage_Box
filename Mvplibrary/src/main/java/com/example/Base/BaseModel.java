package com.example.Base;

import com.example.IBase.IBaseModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 创建者: 走廊里の声控灯
 * 项目名: ToyBox
 * 包名: com.example.Base
 * 创建时间: 2020/11/24 8:53
 */
public class BaseModel implements IBaseModel {

    CompositeDisposable disposableSet = new CompositeDisposable();

    /**
     * 把当前的网络请求添加到缓存
     * @param disposable
     */
    @Override
    public void addDisposable(Disposable disposable) {
        disposableSet.add(disposable);
    }

    /**
     * 取消还未进行的网络请求
     */
    @Override
    public void clear() {
        disposableSet.clear();
    }
}
