package com.example.netmanager;


import android.util.Log;

import com.example.config.GlobalConfig;
import com.example.constant.Constants;
import com.example.https.DefaultTrustManager;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetManager {

    private OkHttpClient mOkHttpClient;
    //创建本类对象的参数，存储状态，不提供对外访问权限，保证数据安全
    private static volatile NetManager mNetManager;
    //将无参构造重写为”私有“状态，不提供对外实例化权限，保证单例
    private NetManager() {
    }

    //创建get方法，单例模式构件并提供本类对象，双重锁保证线程安全问题
    public static NetManager getNetManager() {
        if (mNetManager == null){
            synchronized (NetManager.class){
                if (mNetManager == null){
                    mNetManager = new NetManager();
                }
            }
        }
        return mNetManager;
    }
    //获取Retrofit对象
    private Retrofit getRetrofit(String baseUrl){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getBaseOkHttpClient())
                .build();
    }

    //如果不使用https拉起接口则调用该方法
    public OkHttpClient getBaseOkHttpClient() {
        return getOkHttpClient(null);
    }

    //调用该方法拉起https网络请求，提供OkHttpClient对象，内置日志拦截器
    public static OkHttpClient getOkHttpClient(String[] cers){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("","okhttp===" + message);
            }
        });
        //构建OkHttpClient设置基本参数，并且根据需要添加拦截器
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(Constants.DEFAULT_OUT_TIME,Constants.TIME_UNIT_SECONDS);
        builder.readTimeout(Constants.DEFAULT_OUT_TIME,Constants.TIME_UNIT_SECONDS);
        builder.writeTimeout(Constants.DEFAULT_OUT_TIME,Constants.TIME_UNIT_SECONDS);
        builder.retryOnConnectionFailure(true);
        builder.addInterceptor(httpLoggingInterceptor);
        //此处遍历集合，如果集合为空则不添加，如果不为空遍历集合添加拦截器
        if (GlobalConfig.getInstance().getInterceptors()!=null){
            List<Interceptor> interceptors = GlobalConfig.getInstance().getInterceptors();
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }

        //设置https证书
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            if (cers != null && cers.length>0) {
                setCerTrustManger(builder,cers);
            } else {
                X509TrustManager tm = DefaultTrustManager.getDefaultTrustManager();
                sc.init(null, new TrustManager[]{tm}, new SecureRandom());
                builder.sslSocketFactory(sc.getSocketFactory(), tm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    //创建Service对象
    //如果有多个baseUrl，则调用该方法，传入url进行操作
    public <T> T createServer(Class<T> mClass ,String baseUrl){
        return getRetrofit(baseUrl).create(mClass);
    }

    //如果baseUrl只有一个，则调用该方法，不需要变动url
    public <T> T createServer(Class<T> mClass){
        return getRetrofit(GlobalConfig.getInstance().getBaseUrl()).create(mClass);
    }

    //设置https证书
    private static void setCerTrustManger(OkHttpClient.Builder builder, String[] certificates) {
        //CertificateFactory用来证书生成
        CertificateFactory certificateFactory;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");
            //Create a KeyStore containing our trusted CAs
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            for (int i = 0; i < certificates.length; i++) {
                //读取本地证书
                InputStream is = GlobalConfig.getInstance().getContext().getAssets().open(certificates[i]);
                keyStore.setCertificateEntry(String.valueOf(i), certificateFactory.generateCertificate(is));
                is.close();
            }
            //Create a TrustManager that trusts the CAs in our keyStore
            final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            //Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            builder.sslSocketFactory(sslContext.getSocketFactory(), new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                    for (int i = 0; i < trustManagers.length; i++) {
                        TrustManager trustManager = trustManagers[i];
                        if (trustManager != null && trustManager instanceof X509TrustManager)
                            try {
                                ((X509TrustManager) trustManager).checkServerTrusted(chain, authType);
                            } catch (java.security.cert.CertificateException e) {
                                e.printStackTrace();
                            }
                    }
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            });
        } catch (Exception e) {
        }
    }
}
