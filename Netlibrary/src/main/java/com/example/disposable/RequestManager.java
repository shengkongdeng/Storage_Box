package com.example.disposable;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.reactivex.disposables.Disposable;

/**
 * 请求头管理器
 * 此类中封装了对请求头的控制方法
 * */
public class RequestManager implements IRequestManager {


    private static volatile RequestManager Instance;

    /**
     * 订阅关系列表，存放所有的订阅关系
     */
    private static Map<String,Disposable> mMap=new HashMap<>();;

    /**
     * 静态，防止使用无参构造实例化
     */
    private RequestManager(){}


    /**
     * @return 本类对象
     */
    public static RequestManager getInstance(){
        if (Instance == null) {
            synchronized (RequestManager.class){
                if (Instance == null) {
                    Instance=new RequestManager();
                }
            }
        }
        return Instance;
    }

    /**
     * 该方法用于添加新的订阅关系
     * @param tag 订阅关系的标识
     * @param disposable 订阅关系
     */
    @Override
    public void add(String tag, Disposable disposable) {
        if(!TextUtils.isEmpty(tag)) {
            mMap.put(tag, disposable);
        }
    }

    /**
     * 该方法用于从订阅关系表中移除点阅关系
     * 主要由cancle||cancleAll拉起调用
     * @param tag 订阅关系的标识
     */
    @Override
    public void remove(String tag) {
        if(TextUtils.isEmpty(tag)){
            if(mMap.isEmpty()){
                return;
            }
            mMap.remove(tag);
        }
    }

    /**
     *解除绑定订阅关系
     * @param tag 订阅关系的标识
     */
    @Override
    public void cancel(String tag) {
        if(!TextUtils.isEmpty(tag)){
            if (!mMap.isEmpty()){
                if(mMap.get(tag)!=null){
                    Disposable disposable = mMap.get(tag);
                    if(!disposable.isDisposed()){
                        disposable.dispose();
                    }
                    mMap.remove(tag);
                }
            }
        }
    }

    /**
     * 解除绑定所有的订阅关系
     */
    @Override
    public void cancelAll() {
        Disposable disposable=null;
        if(!mMap.isEmpty()){
            Set<String> keys = mMap.keySet();
            for (String key : keys) {
                if(mMap.get(key)!=null){
                    disposable= mMap.get(key);
                }
                if(disposable!=null&&!disposable.isDisposed()){
                    disposable.dispose();
                }
            }
        }
        mMap.clear();
    }

    /**
     * 检测订阅关系是否存在
     * @param tag 订阅关系的标识
     * @return 订阅关系是否存在
     */
    public boolean isDispose(String tag){
        if(!mMap.isEmpty()&&mMap.get(tag)!=null){
            return mMap.get(tag).isDisposed();
        }
        return false;
    }
}
