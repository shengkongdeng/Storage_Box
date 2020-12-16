package com.example.callback;

import com.example.disposable.RequestManager;
import com.example.exception.ApiException;
import com.example.exception.ExceptionEngine;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 观察者父类(观察者他爹
 */
public abstract class BaseObserver implements Observer {

    private String tag;

    /**
     * 执行：当订阅关系产生时
     * 行为：添加新的订阅关系至订阅关系表
     * @param d 订阅关系
     */
    @Override
    public void onSubscribe(Disposable d) {
        if (tag!=null){
            RequestManager.getInstance().add(tag,d);
        }
    }

    /**
     * 执行：当网络接口完成请求接收到回调数据时
     * 行为：将数据通过 onSuccess(T t) 回调至CallBack
     * @param o 网络数据
     */
    @Override
    public void onNext(Object o) {
        success(o);
        if(tag!=null){
            RequestManager.getInstance().remove(tag);
        }
    }

    /**
     * 执行：当网络请求遭遇错误时
     * 行为：将失败的请求对应的订阅关系解绑，并根据异常状况回调错误信息至 error()
     * @param e 异常
     */
    @Override
    public void onError(Throwable e) {
        if (tag!=null){
            RequestManager.getInstance().cancel(tag);
        }
        if(e instanceof ApiException){
            ApiException apiException= (ApiException) e;
            error(apiException.getMsg(),apiException.getCode());
        }else{
            error("未知异常", ExceptionEngine.UN_KNOWN_ERROR);
        }
    }

    /**
     * 执行：网络请求完成时
     * 行为：解绑订阅关系
     */
    @Override
    public void onComplete() {
        if(tag!=null&&!RequestManager.getInstance().isDispose(tag)){
            RequestManager.getInstance().cancel(tag);
        }
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public abstract void success(Object object);

    public abstract void error(String error,Object code);
}
