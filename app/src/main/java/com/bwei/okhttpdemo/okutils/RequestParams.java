package com.bwei.okhttpdemo.okutils;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by muhanxi on 17/5/26.
 */

public class RequestParams {


     Map<String,Object> params = new HashMap<String, Object>();

     String filekey ;



    /**
     * map 封装成RequestParams 对象
     * @param sources
     */
    public  RequestParams(Map<String,Object> sources) {
        if(sources != null){
            for(Map.Entry<String,Object> entry : sources.entrySet()){
                params.put(entry.getKey(),entry.getValue());
            }
        }

    }

    /**
     * 上传文件request
     * @param sources
     * @param filekey
     */
    public  RequestParams(Map<String,Object> sources,String filekey) {
        if(sources != null){
            for(Map.Entry<String,Object> entry : sources.entrySet()){
                params.put(entry.getKey(),entry.getValue());
            }
        }
        if(!TextUtils.isEmpty(filekey)){
            this.filekey = filekey ;
        }
    }


}
