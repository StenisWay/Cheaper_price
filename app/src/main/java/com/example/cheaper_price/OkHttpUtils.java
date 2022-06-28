package com.example.cheaper_price;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpUtils {

    final static String TAG = "OkHttpUtils";
    public static String OkGetArt(String url){
        String html = null;
        OkHttpClient client = new OkHttpClient();

        Request request =new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            html = response.body().string();
        }catch (IOException e){
            e.printStackTrace();
        }
        return html;

    }



}
