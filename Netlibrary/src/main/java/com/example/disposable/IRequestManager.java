package com.example.disposable;

import io.reactivex.disposables.Disposable;

public interface IRequestManager {

    //添加订阅关系
    void add(String tag, Disposable disposable);

    //移除订阅关系
    void remove(String tag);

    //取消订阅
    void cancel(String tag);

    //取消所有的订阅
    void cancelAll();
}
