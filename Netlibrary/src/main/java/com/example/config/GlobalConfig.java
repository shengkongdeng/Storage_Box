package com.example.config;

import android.content.Context;
import android.os.Handler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;

/**
 * 配置类
 */
public class GlobalConfig {
    //公共的拦截器
    private List<Interceptor> interceptors;
    //上下文对象
    private Context context;
    //baseUrl
    private String baseUrl;
    //请求超时时间
    private long timeout;
    //时间单位
    private TimeUnit timeUnit;
    //公共请求参数
    private Map<String, Object> baseParams;
    //公共的请求头信息
    private Map<String, Object> baseHeaders;
    //日志开关
    private boolean isShowLog;
    //消息循环处理器
    private Handler handler;
    //存储各种appKey的集合
    private Map<String,String> appKeys;

    /**
     * 这部分其实就是一种单例模式，因为配置文件一般不需要进行修改
     * 使用静态内部类，对属性进行封装并进行初始化，这样在过程中，这个对象就能保证只有一个
     */
    static class GlobalConfigViewGolder{
        private static GlobalConfig instance = new GlobalConfig();}

    /**
     * 返回配置类的对象
     * @return 配置类的对象
     */
    public static GlobalConfig getInstance(){
        return GlobalConfigViewGolder.instance;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Map<String, Object> getBaseParams() {
        return baseParams;
    }

    public void setBaseParams(Map<String, Object> baseParams) {
        this.baseParams = baseParams;
    }

    public Map<String, Object> getBaseHeaders() {
        return baseHeaders;
    }

    public void setBaseHeaders(Map<String, Object> baseHeaders) {
        this.baseHeaders = baseHeaders;
    }

    public boolean isShowLog() {
        return isShowLog;
    }

    public void setShowLog(boolean showLog) {
        isShowLog = showLog;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Map<String, String> getAppKeys() {
        return appKeys;
    }

    public void setAppKeys(Map<String, String> appKeys) {
        this.appKeys = appKeys;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
