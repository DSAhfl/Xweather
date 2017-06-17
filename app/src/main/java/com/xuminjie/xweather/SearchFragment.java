package com.xuminjie.xweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xuminjie.xweather.gson.Forecast;
import com.xuminjie.xweather.gson.Weather;
import com.xuminjie.xweather.service.AutoUpdateService;
import com.xuminjie.xweather.util.HttpUtil;
import com.xuminjie.xweather.util.Utility;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/14.
 */

public class SearchFragment extends Fragment {
    private ImageView bingPicImg;
    private TextView selectDate;
    private CustomDatePicker datePicker;
    private String date,time;
    private Button searchBtn;
    private EditText searchCity;
    private LinearLayout historyLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        bingPicImg = (ImageView) view.findViewById(R.id.bing_pic_img1);
        selectDate = (TextView) view.findViewById(R.id.search_date);
        searchBtn = (Button) view.findViewById(R.id.search_button);
        searchCity = (EditText) view.findViewById(R.id.search_city);
        historyLayout = (LinearLayout) view.findViewById(R.id.history_layout);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String bingPic = prefs.getString("bing_pic", null);
        Glide.with(this).load(bingPic).into(bingPicImg);

        initPicker();
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click","click selectDate");
                datePicker.show(date);
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Log.d("click","click searchBtn");
                        search(selectDate.getText().toString(),searchCity.getText().toString());

            }
        });

        return view;
    }


    private void search(final String date, final String city) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                   Log.d("searchHis","inRun");
                    //10.25.109.37
                    String serverUrl = "http://10.6.22.233:8080/Xweather/GetDataServlet";
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("date",date)
                            .add("city",city)
                            .build();
                    okhttp3.Request request = new okhttp3.Request.Builder().url(serverUrl).post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    final String responseText = response.body().string();
                    Log.d("searchHis",responseText);
//                    Toast.makeText(getActivity(), responseData, Toast.LENGTH_SHORT).show();
                    final Weather weather = Utility.handleWeatherResponse(responseText);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(weather != null){
                                showWeatherInfo(weather);
                            }else{
                                Toast.makeText(getActivity(),"获取天气信息失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void showWeatherInfo(Weather weather) {
        historyLayout.removeAllViews();
        for(Forecast forecast : weather.forecastList){
            View view = LayoutInflater.from(getContext()).inflate(R.layout.history_item, historyLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.history_date_text);
            TextView infoText = (TextView) view.findViewById(R.id.history_info_text);
            TextView tmpText = (TextView) view.findViewById(R.id.history_tmp_text);
            TextView cityText = (TextView) view.findViewById(R.id.history_city_text);
            ImageView weatherImg = (ImageView) view.findViewById(R.id.history_weather_img);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            cityText.setText(forecast.historyCity);
            weatherImg.getDrawable().setLevel(forecast.more.code);//drawable里设置xml文件，选择对应的图片
            tmpText.setText(forecast.temperature.max + "℃" + " / " + forecast.temperature.min + "℃");
            historyLayout.addView(view);
        }
    }

    /**
     * 初始化日期选择
     */
    private void initPicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        time = sdf.format(new Date());
        date = time.split(" ")[0];
        //设置当前显示的日期
      //  selectDate.setText(date);
        //设置当前显示的时间
//        currentTime.setText(time);

        /**
         * 设置年月日
         */
        datePicker = new CustomDatePicker(getContext(), "请选择日期", new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                selectDate.setText(time.split(" ")[0]);
            }
        }, "2007-01-01 00:00", time);
        datePicker.showSpecificTime(false); //显示时和分
        datePicker.setIsLoop(false);
        datePicker.setDayIsLoop(true);
        datePicker.setMonIsLoop(true);

//        timePicker = new CustomDatePicker(this, "请选择时间", new CustomDatePicker.ResultHandler() {
//            @Override
//            public void handle(String time) {
//                currentTime.setText(time);
//            }
//        }, "2007-01-01 00:00", "2027-12-31 23:59");//"2027-12-31 23:59"
//        timePicker.showSpecificTime(true);
//        timePicker.setIsLoop(true);
    }

}
