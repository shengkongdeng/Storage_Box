package com.example.client;


import android.text.TextUtils;
import android.util.Log;

import com.example.Service.IService;
import com.example.callback.BaseCallBack;
import com.example.config.GlobalConfig;
import com.example.netmanager.NetManager;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpClient {
    HttpMethod method;//
    Map<String, Object> params;//参数清单
    Map<String, Object> headers;//请求头清单
    LifecycleProvider lifecycleProvider;//生命周期
    ActivityEvent activityEvent;//Activity事件
    FragmentEvent fragmentEvent;//Fragment事件
    String baseUrl;//接口头
    String apiUrl;//接口体
    boolean isJson;//是否是json格式
    String jsonBody;//json请求体
    long time;//超时时间
    TimeUnit timeUnit;//时间单位
    BaseCallBack baseCallBack;//响应格式
    String tag;//请求标记

    private HttpClient() {}

    public HttpClient(Builder builder) {
        this.method = builder.method;
        this.activityEvent = builder.activityEvent;
        this.fragmentEvent = builder.fragmentEvent;
        this.params = builder.params;
        this.headers = builder.headers;
        this.lifecycleProvider = builder.lifecycleProvider;
        this.baseUrl = builder.baseUrl;
        this.apiUrl = builder.apiUrl;
        this.isJson = builder.isJson;
        this.jsonBody = builder.jsonBody;
        this.time = builder.time;
        this.timeUnit = builder.timeUnit;
        this.tag = builder.tag;
    }

    public void enqueue(BaseCallBack baseCallBack) {
        this.baseCallBack = baseCallBack;
        if (baseCallBack == null) {
            new RuntimeException("网络请求响应格式为null,请检查响应对象");
        }
        doRequest();
    }

    /**
     * 行为：通过当前系统时间生成订阅关系标签，绑定请求头及参数
     */
    private void doRequest() {
        if (tag == null) {
            tag = System.currentTimeMillis() + "";
        }
        baseCallBack.setTag(tag);
        addHeaders();
        addParams();
        Observable observable = createObservable();
        HttpObservable httpObservable = new HttpObservable.Buidler(observable)
                .setActivityEvent(activityEvent)
                .setFragmentEvent(fragmentEvent)
                .setLifecycleProvider(lifecycleProvider)
                .setBaseObserver(baseCallBack).build();
        httpObservable.observer();
    }

    /**
     * 行为：添加请求参数，若params为null则添加空集合
     * 并且添加公共请求参数。
     */
    private void addParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        //添加公共请求参数
        if (GlobalConfig.getInstance().getBaseParams() != null) {
            params.putAll(GlobalConfig.getInstance().getBaseParams());
        }
    }

    /**
     * 行为：添加请求头，，若headres为null则添加空集合
     * 并添加公共请求头信息
     */
    private void addHeaders() {
        if (headers == null) {
            headers = new HashMap<>();
        }
        //添加公共请求头信息
        if (GlobalConfig.getInstance().getBaseHeaders() != null ) {
            headers.putAll(GlobalConfig.getInstance().getBaseHeaders());
        }

    }

    /**
     * 行为：创建观察者对象
     * @return 观察者对象
     */
    private Observable createObservable() {
        Observable observable = null;
        boolean hasBodyString = !TextUtils.isEmpty(jsonBody);
        RequestBody requestBody = null;
        if (hasBodyString) {
            Log.e("liangxq", "createObservable: " + hasBodyString);
            String mediaType = isJson ? "application/json; charset=utf-8" : "text/plain;charset=utf-8";
            requestBody = RequestBody.create(okhttp3.MediaType.parse(mediaType), jsonBody);
        }
        if (method == null) {
            method = HttpMethod.POST;
        }
        this.baseUrl = GlobalConfig.getInstance().getBaseUrl()==null?this.baseUrl:GlobalConfig.getInstance().getBaseUrl();
        IService server = NetManager.getNetManager().createServer(IService.class,baseUrl);
        if(apiUrl==null|| apiUrl.equals(baseUrl)){
            new RuntimeException("apiUrl is null or equals baseUrl");
        }

        switch (method) {
            case GET:
                observable=server.get(apiUrl,params,headers);
                break;
            case POST:
                if (isJson) {
                    observable= server.postJson(apiUrl, requestBody, headers);
                } else {
                    observable= server.post(apiUrl, params, headers);
                }
                break;
            case DELETE:
                observable = server.delete(apiUrl, params, headers);
                break;
            case PUT:
                observable = server.put(apiUrl, params, headers);
                break;
        }
        return observable;
    }

    public static class Builder {
        //请求方式
        HttpMethod method;
        //请求参数
        Map<String, Object> params;
        //请求头信息
        Map<String, Object> headers;
        //RxJava绑定生命周期
        LifecycleProvider lifecycleProvider;
        //绑定Activity具体的生命周的
        ActivityEvent activityEvent;
        //绑定Fragment的具体的生命周期的
        FragmentEvent fragmentEvent;
        String baseUrl;
        //拼接的url
        String apiUrl;
        //是否是json上传表示
        boolean isJson;
        //json字符串
        String jsonBody;
        //超时时间
        long time;
        //时间单位
        TimeUnit timeUnit;
        //回调接口
        BaseCallBack baseCallBack;
        //订阅关系的标签
        String tag;
        public Builder get() {
            this.method = HttpMethod.GET;
            return this;
        }

        public Builder post() {
            this.method = HttpMethod.POST;
            return this;
        }

        public Builder put() {
            this.method = HttpMethod.PUT;
            return this;
        }

        public Builder delete() {
            this.method = HttpMethod.DELETE;
            return this;
        }

        public Builder setParams(Map<String, Object> params) {
            this.params = params;
            return this;
        }

        public Builder setHeaders(Map<String, Object> headres) {
            this.headers = headres;
            return this;
        }

        public Builder setLifecycleProvider(LifecycleProvider lifecycleProvider) {
            this.lifecycleProvider = lifecycleProvider;
            return this;
        }

        public Builder setActivityEvent(ActivityEvent activityEvent) {
            this.activityEvent = activityEvent;
            return this;
        }

        public Builder setFragmentEvent(FragmentEvent fragmentEvent) {
            this.fragmentEvent = fragmentEvent;
            return this;
        }

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setApiUrl(String apiUrl) {
            this.apiUrl = apiUrl;
            return this;
        }

        public Builder setJson(boolean json) {
            isJson = json;
            return this;
        }

        public Builder setJsonBody(String jsonBody) {
            this.jsonBody = jsonBody;
            return this;
        }

        public Builder setTime(long time) {
            this.time = time;
            return this;
        }

        public Builder setTimeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
            return this;
        }


        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public HttpClient build() {
            return new HttpClient(this);
        }
    }

}
