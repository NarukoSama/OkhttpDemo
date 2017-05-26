package com.bwei.okhttpdemo.okutils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by muhanxi on 17/5/26.
 */

public class ICallBack implements Callback {


    IResponseListener listener ;
    Class clazz ;
    Handler handler ;
    String tag ;

    public ICallBack(DataHandler dataHandler,String tag){
        this.listener = dataHandler.listener ;
        this.clazz = dataHandler.clazz ;
        this.tag = tag;
        handler = new Handler(Looper.getMainLooper());

    }

    public ICallBack(IResponseListener listener,String tag){
        this.listener = listener ;
        this.tag = tag;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(Call call, final IOException e) {


        handler.post(new Runnable() {
            @Override
            public void run() {

                listener.onFailed(e,tag);

            }
        }) ;

    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (response.isSuccessful()) {
                        String content = response.body().string();
                        if(clazz != null){
                           Object object =   new Gson().fromJson(content,clazz);
                            listener.onSuccess(object,tag);
                        }else {
                            listener.onSuccess(content,tag);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onFailed(" parse json exception",tag);
                }
            }
        });







    }
}
