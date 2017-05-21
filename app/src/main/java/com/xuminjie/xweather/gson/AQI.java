package com.xuminjie.xweather.gson;

/**
 * Created by Administrator on 2017/5/17.
 */

public class AQI {
    public AQICity city;

    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
