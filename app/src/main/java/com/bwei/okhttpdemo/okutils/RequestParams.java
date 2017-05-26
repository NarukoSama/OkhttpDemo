package com.bwei.okhttpdemo.okutils;

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
        for(Map.Entry<String,Object> entry : sources.entrySet()){
            params.put(entry.getKey(),entry.getValue());
        }
    }

    public  RequestParams(Map<String,Object> sources,String filekey) {
        for(Map.Entry<String,Object> entry : sources.entrySet()){
            params.put(entry.getKey(),entry.getValue());
        }
        this.filekey = filekey ;
    }

}
