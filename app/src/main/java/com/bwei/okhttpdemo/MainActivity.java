package com.bwei.okhttpdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bwei.okhttpdemo.okutils.DataHandler;
import com.bwei.okhttpdemo.okutils.IOkHttpClient;
import com.bwei.okhttpdemo.okutils.IRequest;
import com.bwei.okhttpdemo.okutils.IResponseListener;
import com.bwei.okhttpdemo.okutils.RequestParams;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketAddress;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.attr.tag;

public class MainActivity extends Activity implements View.OnClickListener , IResponseListener{


    private Button button;


    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button = (Button) findViewById(R.id.get_id);
        button.setOnClickListener(this);


        findViewById(R.id.get_syn).setOnClickListener(this);
        findViewById(R.id.post_id).setOnClickListener(this);
        findViewById(R.id.post_syn).setOnClickListener(this);
        findViewById(R.id.post_string).setOnClickListener(this);
        findViewById(R.id.post_file).setOnClickListener(this);
        findViewById(R.id.interceptor).setOnClickListener(this);




    }




    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.get_id:


                getSynchronous();

                break;
            case R.id.get_syn:
                getAsychronous();

                break;
            case R.id.post_id:
                postSynchronous();

                break;
            case R.id.post_syn:
                postAsychronous();

                break;

            case R.id.post_string:
                poststring();
                break;
            case R.id.post_file:

                toPic();

                break;
            case R.id.interceptor:
                interceptor();

                break;

        }

    }


    public static String photoCacheDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Bwei";

    public void toPic(){

        if(!new File(photoCacheDir).exists()){
            new File(photoCacheDir).mkdirs();
        }

        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        getAlbum.setType("image/*");
        startActivityForResult(getAlbum, 1);

    }


    private void poststring() {


        executorService.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    String postBody = ""
                            + "Releases\n"
                            + "--------\n"
                            + "\n"
                            + " * _1.0_ May 6, 2013\n"
                            + " * _1.1_ June 15, 2013\n"
                            + " * _1.2_ August 11, 2013\n";

                    MediaType MEDIA_TYPE_MARKDOWN
                         = MediaType.parse("text/x-markdown; charset=utf-8");

                    OkHttpClient client = new OkHttpClient();


                    Request request = new Request.Builder().url("https://api.github.com/markdown/raw")
                            .post(RequestBody.create(MEDIA_TYPE_MARKDOWN,postBody)).build();

                    Response response =  client.newCall(request).execute() ;


                    System.out.println("response = " + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    String url = "http://qhb.2dyt.com/Bwei/login" ;

    private void postSynchronous() {

        executorService.execute(new Runnable() {
            @Override
            public void run() {


                try {
                    OkHttpClient client = new OkHttpClient();


                    RequestBody requestBody = new FormBody.Builder().add("username","18701317750").add("password","123456").add("postkey","1503d").build();

                    //addHeader 添加头
                    Request request = new Request.Builder().url(url).addHeader("key","value").post(requestBody).build();

                    Response response =   client.newCall(request).execute() ;

//                    response.isSuccessful()

                    
                    System.out.println("response = " + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });



    }


    // 异步 post
    private void postAsychronous() {


        Toast.makeText(this, "" + getCacheDir(), Toast.LENGTH_SHORT).show();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    int cacheSize = 10 * 1024 * 1024; // 10 MiB


                    Cache cache = new Cache(getCacheDir(),cacheSize);


                    OkHttpClient client = new OkHttpClient.Builder().cache(cache).build() ;


                    CacheControl cacheControl = new CacheControl.Builder().maxAge(1,TimeUnit.MINUTES).build();

                    Request request = new Request.Builder()
                            .url("http://publicobject.com/helloworld.txt")
                            .cacheControl(cacheControl)
                            .build();

                    Response response1 = client.newCall(request).execute();
                    if (!response1.isSuccessful()) throw new IOException("Unexpected code " + response1);

                    String response1Body = response1.body().string();
                    System.out.println("Response 1 response:          " + response1);
                    System.out.println("Response 1 cache response:    " + response1.cacheResponse());
                    System.out.println("Response 1 network response:  " + response1.networkResponse());

                    Response response2 = client.newCall(request).execute();
                    if (!response2.isSuccessful()) throw new IOException("Unexpected code " + response2);

                    String response2Body = response2.body().string();
                    System.out.println("Response 2 response:          " + response2);
                    System.out.println("Response 2 cache response:    " + response2.cacheResponse());
                    System.out.println("Response 2 network response:  " + response2.networkResponse());

                    System.out.println("Response 2 equals Response 1? " + response1Body.equals(response2Body));





                } catch (IOException e) {
                    e.printStackTrace();
                }


//                client.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//
//                        System.out.println("call = " + response.body().string());
//
//                    }
//                });


            }
        });


    }































    //发送get 请求  同步execute
    public void getSynchronous() {


        executorService.execute(new Runnable() {
            @Override
            public void run() {

                try {
//                    OkHttpClient client = new OkHttpClient.Builder().build();
//
//                    Request request = new Request.Builder().url("http://10.0.0.2:8080/Qhb/login").build();
//
//                    Response response =  client.newCall(request).execute();
//
//
//                    System.out.println("response = " + response.body().string());





                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        String path = "http://gdown.baidu.com/data/wisegame/0852f6d39ee2e213/QQ_676.apk" ;


        final String local =  Environment.getExternalStorageDirectory() +"/aa/";
        Toast.makeText(MainActivity.this, ""+local, Toast.LENGTH_SHORT).show();

        IOkHttpClient.downloadFile(IRequest.createDownloadRequest(path),local,new DataHandler(new IResponseListener() {
            @Override
            public void onSuccess(Object response, String tag) {


                //跳转到安装界面
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(tag)),
                        "application/vnd.android.package-archive");
                startActivity(intent);

            }

            @Override
            public void onFailed(Object failed, String tag) {

            }
        }));


    }


    // get 请求  enqueue 异步
    public void getAsychronous(){

//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//
//                ClearableCookieJar cookieJar =
//                        new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(MainActivity.this));
////                cookieJar.c
//
//                OkHttpClient client = new OkHttpClient.Builder().cookieJar(new CookiesManager()).build();
//
//                Request request = new Request.Builder().url("http://10.0.0.2:8080/Bwei/login").build();
//
//                client.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//
//                        System.out.println("e = " + e.getMessage());
//
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        System.out.println("response = " + response.body().string());
//
//
//                    }
//                });
//
//
//            }
//        });

        Map map = new HashMap();
        map.put("username","11111111111");
        map.put("password","1");
        map.put("postkey","1503d");

        IOkHttpClient.getAsyn(IRequest.createFileRequest(new RequestParams(map), "https://kyfw.12306.cn/otn/"),new DataHandler(this));



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case 1:

                    try {
                        // 相册

                        if (data == null)// 如果没有获取到数据
                            return;
                        Uri originalUri = data.getData();
                        //文件大小判断

                        if (originalUri != null) {
                            File file = null;
                            String[] proj = {MediaStore.Images.Media.DATA};
                            Cursor actualimagecursor = managedQuery(originalUri, proj, null, null, null);
                            if (null == actualimagecursor) {
                                if (originalUri.toString().startsWith("file:")) {
                                    file = new File(originalUri.toString().substring(7, originalUri.toString().length()));
                                    if(!file.exists()){
                                        //地址包含中文编码的地址做utf-8编码
                                        originalUri = Uri.parse(URLDecoder.decode(originalUri.toString(),"UTF-8"));
                                        file = new File(originalUri.toString().substring(7, originalUri.toString().length()));
                                    }
                                }
                            } else {
                                // 系统图库
                                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                                actualimagecursor.moveToFirst();
                                String img_path = actualimagecursor.getString(actual_image_column_index);
                                if (img_path == null) {
                                    InputStream inputStrean = getContentResolver().openInputStream(originalUri);
                                    file = new File(photoCacheDir+"/aa.jpg");
                                    if(!file.exists()){
                                        file.createNewFile();
                                    }
                                    System.out.println(" - " + file.exists());
                                    FileOutputStream outputStream = new FileOutputStream(file);

                                    byte[] buffer = new byte[1024];
                                    int len = 0;
                                    while ((len = inputStrean.read(buffer)) != -1) {
                                        outputStream.write(buffer, 0, len);
                                    }
                                    outputStream.flush();

                                    if (inputStrean != null) {
                                        inputStrean.close();
                                        inputStrean = null;
                                    }

                                    if (outputStream != null) {
                                        outputStream.close();
                                        outputStream = null;
                                    }
                                } else {
                                    file = new File(img_path);
                                }
                            }
                            String camerFileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
                            File newfilenew = new File(photoCacheDir,camerFileName);
                            if (!newfilenew.exists()) {
                                newfilenew.createNewFile();
                            }
                            FileInputStream inputStream = new FileInputStream(file);
                            FileOutputStream outStream = new FileOutputStream(newfilenew);

                            try {
                                byte[] buffer = new byte[1024];
                                int len = 0;
                                while ((len = inputStream.read(buffer)) != -1) {
                                    outStream.write(buffer, 0, len);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }finally {
                                try {
                                    inputStream.close();
                                    outStream.close();
                                    inputStream = null;
                                    outStream = null;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            postFile(newfilenew.toString());

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

            }


        }


    }
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    //path 本地文件路径
    private void postFile(final String path) {



        final String [] split = path.split("/");
        File file = new File(path) ;


        System.out.println("file = " + file.length());
        Map map = new HashMap();
        map.put("username","11111111111");
        map.put("password","1");
        map.put("image",file);
        map.put("postkey","1503d");

        IOkHttpClient.getAsyn(IRequest.createFileRequest(new RequestParams(map,"image"), "http://qhb.2dyt.com/Bwei/upload"),new DataHandler(this));

//
//
//        final String [] split = path.split("/");
//
//
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//
//                File file = new File(path) ;
//
//                System.out.println("file = " + file.length());
//
//                OkHttpClient client = new OkHttpClient.Builder().connectTimeout(20,TimeUnit.SECONDS).readTimeout(20,TimeUnit.SECONDS).writeTimeout(20,TimeUnit.SECONDS).build();
//
//
//                //image
////file
////                face
////                path
//
//                RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                        .addFormDataPart("image",split[split.length-1],RequestBody.create(MEDIA_TYPE_PNG,new File(path)))
//                        .addFormDataPart("username","muhanxi").build();
//
////
//                Request request = new Request.Builder().url("http://qhb.2dyt.com/Bwei/upload").post(requestBody).build();
//
//                client.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        System.out.println("call = postFile onFailure " + call);
//
//                        System.out.println("response = failed" +  e.getMessage());
//
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//
//
//                        System.out.println("response = " + response.body().string());
//
//                    }
//                });
//
//
//            }
//        });







    }




    public void interceptor(){


        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {


                    OkHttpClient client = new OkHttpClient.Builder()
//                            .addNetworkInterceptor(new LoggingInterceptor())
                            .build();

                    Request request = new Request.Builder()
                            .url("http://www.publicobject.com/helloworld.txt")
                            .header("User-Agent", "OkHttp Example")
                            .build();

                    Response response = client.newCall(request).execute();
                    response.body().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    @Override
    public void onSuccess(Object response, String tag) {

        //


        System.out.println("response = " + response);


    }

    @Override
    public void onFailed(Object failed, String tag) {
        System.out.println("onFailed = " + failed);

    }


    class CookiesManager implements CookieJar {

        private final PersistentCookieStore cookieStore = new PersistentCookieStore(getApplicationContext());

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (cookies != null && cookies.size() > 0) {
                for (Cookie item : cookies) {
                    cookieStore.add(url, item);
                }
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url);
            return cookies;
        }
    }


}
