package com.tt.simpletranslate.service;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*
public interface TranslateService {
    @GET("openapi.do?type=data&doctype=json&version=1.1")
    Call<ResponseBody> translate(
            @Query("keyfrom") String name,
            @Query("key") String key,
            @Query("q") String word
    );
}
*/

public interface TranslateService {
    @GET("openapi.do?type=data&doctype=json&version=1.1")
    Observable<ResponseBody> translate(
            @Query("keyfrom") String name,
            @Query("key") String key,
            @Query("q") String word
    );
}
