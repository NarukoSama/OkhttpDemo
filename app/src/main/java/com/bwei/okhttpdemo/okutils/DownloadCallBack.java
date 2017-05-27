package com.bwei.okhttpdemo.okutils;

import android.content.Intent;
import android.graphics.Path;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamTokenizer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.R.string.ok;

/**
 * Created by muhanxi on 17/5/26.
 */

public class DownloadCallBack implements Callback {

    private DataHandler dataHandler;
    private String path ;
    private Handler handler ;

    private String url ;

    public DownloadCallBack(DataHandler dataHandler,String path,String url){

        this.path = path;
        this.dataHandler = dataHandler ;
        handler = new Handler(Looper.getMainLooper());
        this.url = url ;
    }

    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        File file = null ;
        final String [] arrays =  url.split("/");

        try {


            inputStream = response.body().byteStream();

            int len = 0;
            byte[] buff = new byte[1024];

             file = new File(path);

            if (!file.exists()) {
               boolean result =  file.mkdirs();
            }
            file = new File(file.getPath(),arrays[arrays.length - 1]);
            if(file.exists()){
               file.delete();
            }
            file.createNewFile();

            fileOutputStream = new FileOutputStream(file);

            while ((len = inputStream.read(buff)) != -1) {
                fileOutputStream.write(buff, 0, len);
            }
            fileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            inputStream = null;
            fileOutputStream = null;
            handler.post(new Runnable() {
                @Override
                public void run() {

//                    path+arrays[arrays.length-1] 本地文件的绝对路径
                    dataHandler.listener.onSuccess(response,path+arrays[arrays.length-1]);

                }
            }) ;
        }



    }
}
