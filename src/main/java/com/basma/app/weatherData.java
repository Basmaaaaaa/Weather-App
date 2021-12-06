package com.basma.app;

import org.json.JSONException;
import org.json.JSONObject;

public class weatherData {
    private String micon,mTemperature,mcity,mweather,mWeatherType;
    private int mCondition;
    public static weatherData fromJson(JSONObject jsonObject){
        try{
            weatherData weatherD=new weatherData();
            weatherD.mcity=jsonObject.getString("name");
            weatherD.mCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.mWeatherType=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.micon=updateWeatherIcon(weatherD.mCondition);
            double tempResult=jsonObject.getJSONObject("main").getDouble("name")-273.15;
            int RoundedValue= (int) Math.rint(tempResult);
            weatherD.mTemperature=Integer.toString(roundedValue);
            return weatherD;
        } catch(JSONException e){
            e.printStackTrace();
        return null;}

    }
    private static String updateWeatherIcon(int condition) {
        if (condition >= 0 && condition <= 300) {
            return "thenderstorm1";
        }
        if (condition >=300 && condition <= 500) {
            return "lightrain";
        }
        if (condition >=500 && condition <= 600) {
            return "shower";
        }
        if (condition >=600 && condition <= 700) {
            return "snower2";
        }
        if (condition >= 701 && condition <= 771) {
            return "fog";
        }
        if (condition==800) {
            return "overcast";
        }
        if (condition >=801 && condition <=804) {
            return "cloudy";
        }
        if (condition >=900 && condition <=902) {
            return "thenderstorm1";
        }
        if (condition ==903) {
            return "snow1";
        }
        if (condition >= 905 && condition <= 1000) {
            return "thenderstorm2";
        }
        return "dunno";
    }
}
