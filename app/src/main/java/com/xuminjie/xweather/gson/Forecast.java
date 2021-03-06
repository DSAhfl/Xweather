package com.xuminjie.xweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/5/17.
 */

public class Forecast {
    public String date;

    public String historyCity;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature{
        public String max;
        public String min;
    }

    public class More{
        @SerializedName("txt_d")
        public String info;

        @SerializedName("code_d")
        public int code;
    }
}
