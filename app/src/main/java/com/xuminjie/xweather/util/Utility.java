package com.xuminjie.xweather.util;

import android.text.TextUtils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.xuminjie.xweather.db.City;
import com.xuminjie.xweather.db.County;
import com.xuminjie.xweather.db.Province;
import com.xuminjie.xweather.gson.Weather;

/**
 * 先使用JSONArray和JSONObject将数据解析出来，然后组装成实体类对象，再调用save()将数据存储到数据库中
 * Created by Administrator on 2017/5/10.
 */

public class Utility {
    /*
    解析处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allProvince = new JSONArray(response);
                for(int i =0; i < allProvince.length(); ++i){
                    JSONObject provinceObject = allProvince.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    /*
     解析处理服务器返回的市级数据
    */
    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allProvince = new JSONArray(response);
                for(int i =0; i < allProvince.length(); ++i){
                    JSONObject cityObject = allProvince.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    /*
       解析处理服务器返回的县级数据
     */
    public static boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allProvince = new JSONArray(response);
                for(int i =0; i < allProvince.length(); ++i){
                    JSONObject countyObject = allProvince.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 将返回的JSON数据解析成weather实体类
     */
    public static Weather handleWeatherResponse(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
