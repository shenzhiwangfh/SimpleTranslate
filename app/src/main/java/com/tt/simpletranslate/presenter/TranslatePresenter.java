package com.tt.simpletranslate.presenter;

import android.content.Context;
import android.content.Intent;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.orhanobut.logger.Logger;
import com.tt.simpletranslate.BroadcastUtils;
import com.tt.simpletranslate.service.TranslateService;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TranslatePresenter implements ITranslatePresenter {
    private Context context;

    public TranslatePresenter(Context context) {
        this.context = context;
    }

    @Override
    public void translate(String name, String key, String word) {
        //步骤4:创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fanyi.youdao.com/") // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        // 步骤5:创建 网络请求接口 的实例
        final TranslateService request = retrofit.create(TranslateService.class);

        Observable<ResponseBody> observable = request.translate(name, key, word);
        // 3. 发送网络请求(异步)
        observable.subscribeOn(Schedulers.io())               // 在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread())  // 回到主线程 处理请求结果
                .subscribe(new Observer<ResponseBody>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        //Logger.i("onSubscribe,d=" + d);
                    }

                    @Override
                    public void onNext(ResponseBody body) {
                        Logger.i("onNext,body = " + body);

                        try {
                            Intent intent = new Intent();
                            String result = body.string();
                            intent.putExtra("result", result);
                            BroadcastUtils.getInstance(context).sendMessage(BroadcastUtils.ID_TRANSLATION_RESULT, intent);
                        } catch (IOException e) {
                            Logger.e("onNext,e = " + e);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError,e=" + e);
                    }

                    @Override
                    public void onComplete() {
                        Logger.i("onComplete");
                    }
                });

        /*
        //对 发送请求 进行封装
        Call<ResponseBody> call = request.translate(name, key, word);

        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Log.e(TAG, "response=" + response.body());
                try {
                    ResponseBody body = response.body();
                    String result = body.string();
                    Logger.e("result=" + result);

                    Intent intent = new Intent();
                    intent.putExtra("result", result);
                    BroadcastUtils.getInstance(context).sendMessage(BroadcastUtils.ID_TRANSLATION_RESULT, intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Logger.e("throwable=" + throwable.toString());
                //mResult.setText(throwable.toString());

                //Intent intent = new Intent();
                //intent.putExtra("result", result);
                //BroadcastUtils.getInstance().sendMessage(BroadcastUtils.ID_TRANSLATION_RESULT, intent);
            }
        });
        */
    }
}
