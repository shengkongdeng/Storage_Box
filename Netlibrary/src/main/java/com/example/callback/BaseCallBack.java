package com.example.callback;

import com.example.exception.ExceptionEngine;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

public abstract class BaseCallBack<T> extends BaseObserver {

    //解析成功的标志
    boolean callSuccess=false;

    /**
     * 执行：得到网络请求数据时由其他方法调用
     * @param object 网络数据
     */
    @Override
    public void success(Object object) {
        String json = new Gson().toJson(object);
        T paser = paser(json);
        if(callSuccess&&isCodeSuccess()){
            onSuccess(paser);
        }
    }

    T paser(String result){
        T data=null;
        try {
            data=onConvert(result);
            callSuccess=true;
        }catch (Exception e){
            callSuccess=false;
            error("解析错误", ExceptionEngine.ANALYTIC_SERVER_DATA_ERROR);
        }
        return data;
    }

    //将JsonElement转换为Response，并且通过子类的实现来获取data数据，手动实现本类子类实现本方法解析第一层数据，判断服务器返回状态，做简单处理
    protected abstract T onConvert(String result);

    //数据返回状态成功
    public abstract boolean isCodeSuccess();

    //解析后的数据回调方法
    public abstract T convert(JsonElement result);

    //回调数据
    public abstract void onSuccess(T t);
}
