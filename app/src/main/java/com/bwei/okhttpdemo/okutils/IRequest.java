package com.bwei.okhttpdemo.okutils;

import java.io.File;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.R.attr.path;

/**
 * Created by muhanxi on 17/5/26.
 */

public class IRequest {







    //创建一个 get request

    /**
     *
     * @param requestParams 封装的参数
     * @param url   请求的接口地址
     * @return Request ＝＝  封装好了 请求参数 和url 的request
     */
    public static Request createGetRequest(RequestParams requestParams,String url){

        String resulturl = url ;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(url);
        if(requestParams.params.size() > 0){
            stringBuilder.append("?");
            // www.baidu.com/login?username=111&password=123
            for(Map.Entry<String,Object> entry : requestParams.params.entrySet()){
                stringBuilder.append(entry.getKey());
                stringBuilder.append("=");
                stringBuilder.append(entry.getValue());
                stringBuilder.append("&");
            }
            resulturl =   stringBuilder.substring(0,stringBuilder.length()-1);
        }
       return new Request.Builder().url(resulturl).tag(url).build();

    }


    //创建一个 post request

    /**
     *post
     * @param requestParams 封装的参数
     * @param url   请求的接口地址
     * @return Request ＝＝  封装好了 请求参数 和url 的request
     */
    public static Request createPostRequest(RequestParams requestParams,String url){
        FormBody.Builder builder = new FormBody.Builder();
        for(Map.Entry<String,Object> entry : requestParams.params.entrySet()){
            builder.add(entry.getKey(),(String) entry.getValue());
        }
        FormBody formBody = builder.build() ;
       return  new  Request.Builder().url(url).post(formBody).tag(url).build();
    }


    //
    /**创建一个 file request
     *post
     * @param requestParams 封装的参数
     * @param url   请求的接口地址
     * @return Request ＝＝  封装好了 请求参数（包含了file） 和url 的request
     */
    public static Request createFileRequest(RequestParams requestParams,String url){
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for(Map.Entry<String,Object> entry : requestParams.params.entrySet()){
//            entry.getValue()    /sdcark/data/a.jpg
            if(entry.getValue() instanceof File){
//                requestParams.filekey
                String [] split = ((File) entry.getValue()).getPath().split("/");
                builder.addFormDataPart(entry.getKey(),split[split.length-1],RequestBody.create(MediaType.parse("image/png"),(File) entry.getValue()));
            } else {
                builder.addFormDataPart(entry.getKey(),(String) entry.getValue());
            }

        }
        MultipartBody formBody = builder.build() ;
        return  new  Request.Builder().url(url).post(formBody).tag(url).build();
    }







}
