package com.xuminjie.xweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xuminjie.xweather.gson.Weather;
import com.xuminjie.xweather.service.AutoUpdateService;
import com.xuminjie.xweather.util.HttpUtil;
import com.xuminjie.xweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/14.
 */

public class UserFragment extends Fragment {
    private TextView userText;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment,container,false);
        userText = (TextView) view.findViewById(R.id.user_text);

        getData();

        return view;
    }

    private void getData() {
        String weatherUrl = "http://172.20.10.2:8080/Xweather/IndexServlet";
//        Log.d("responseText",weatherId);
        //http://guolin.tech/api/weather?cityid=CN101020300&key=bc0418b57b2d4918819d3974ac1285d9
        //http://localhost:8080/IndexServlet
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                Log.d("responseText",responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userText.setText(responseText);
                    }
                });
            }
        });

    }
}
