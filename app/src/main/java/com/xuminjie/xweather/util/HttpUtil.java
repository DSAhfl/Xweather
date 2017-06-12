package com.xuminjie.xweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 发起一条HTTP请求只需调用该方法，传入请求地址，注册一个回调来处理服务器的响应
 * Created by Administrator on 2017/5/10.
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
