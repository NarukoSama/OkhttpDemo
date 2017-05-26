package com.bwei.okhttpdemo.okutils;

/**
 * Created by muhanxi on 17/5/26.
 */

public interface IResponseListener {


    // 成功回调
    // tag 网络请求标示
    public void onSuccess(Object response,String tag);

    // 失败回调
    public void onFailed(Object failed,String tag);

}
