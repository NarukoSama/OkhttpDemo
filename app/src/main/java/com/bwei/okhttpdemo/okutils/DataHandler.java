package com.bwei.okhttpdemo.okutils;

/**
 * Created by muhanxi on 17/5/26.
 */

public class DataHandler {


    public IResponseListener listener ;
    //需要转化的实体类
    public Class clazz ;

    public DataHandler(IResponseListener listener){
        this.listener = listener ;
    }



    public DataHandler(IResponseListener listener,Class clazz){
        this.listener = listener ;
        this.clazz = clazz;
    }

}
