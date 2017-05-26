package com.bwei.okhttpdemo.okutils;

import com.bwei.okhttpdemo.CookiesManager;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by muhanxi on 17/5/26.
 */

public class IOkHttpClient {

    static OkHttpClient client ;


    static ExecutorService executorService ;

    static {

        executorService = Executors.newCachedThreadPool();

        client = new OkHttpClient.Builder()
                .cookieJar(new CookiesManager())
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS)
                .writeTimeout(60,TimeUnit.SECONDS)
                .build();
    }



    // 异步发送请求
    public static void getAsyn(final Request request,final DataHandler dataHandler){

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                client.newCall(request).enqueue(new ICallBack(dataHandler,(String) request.tag()));

            }
        });
    }


    /**
     *
     * @param request
     * @param dataHandler
     * @param path 文件下载保存的位置  不需要文件名
     */
    public static void downloadFile(final Request request,final String path,final DataHandler dataHandler){

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                client.newCall(request).enqueue(new DownloadCallBack(dataHandler,path,(String)request.tag()));

            }
        });
    }



}
