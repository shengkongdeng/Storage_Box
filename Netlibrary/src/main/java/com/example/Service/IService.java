package com.example.Service;

import com.google.gson.JsonElement;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface IService {
    @GET
    Observable<JsonElement> get(@Url String url, @QueryMap Map<String,Object> params, @HeaderMap Map<String,Object> headers);

    @POST
    @FormUrlEncoded
    Observable<JsonElement> post(@Url String url, @QueryMap Map<String,Object> params, @HeaderMap Map<String,Object> headers);

    @POST
    Observable<JsonElement> postJson(@Url String url, @Body RequestBody requestBody, @HeaderMap Map<String,Object> headers);

    @DELETE
    Observable<JsonElement> delete(@Url String url, @QueryMap Map<String,Object> params, @HeaderMap Map<String,Object> headers);

    @PUT
    Observable<JsonElement> put(@Url String url, @QueryMap Map<String,Object> params, @HeaderMap Map<String,Object> headers);

    @Multipart
    @POST
    Observable<JsonElement> upLoad(@Url String url, @Body RequestBody requestBody, List<MultipartBody.Part> fileList);

    @Streaming
    Observable<ResponseBody> downLoad(@Url String url, @QueryMap Map<String,Object> params, @HeaderMap Map<String,Object> headers);
}
