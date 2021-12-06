package com.basma.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    final String APP_ID="38f509946f557bd69c2fe26fb056cd4d";
    final String WEATHER_URL="https://home.openweathermap.org/data/2.5/weather";
    final long MIN_TIME=5000;
    final float MIN_DIASTANCE=1000;
    final int REQUEST_CODE=101;
    String Location_Provider= LocationManager.GPS_PROVIDER;
    TextView NameOfcity,weatherstate,Temperature;
    ImageView mweatherIcon;
    RelativeLayout mCityFinder;
    LocationManager mLocationManager;
    LocationListener mLocationListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherstate=findViewById(R.id.weatherCondition);
        Temperature=findViewById(R.id.Temperature);
        mweatherIcon=findViewById(R.id.weatherIcon);
        mCityFinder=findViewById(R.id.cityFinder);
        NameOfcity=findViewById(R.id.cityName);
        mCityFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,cityFinder.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected  void onResume(){
        super.onResume();
        getWeatherForCurrentLocation();
        
    }


    private void getWeatherForCurrentLocation() {
        mLocationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener=new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());
                RequestParams params=new RequestParams();
                params.put("lat",Latitude);
                params.put("long",Longitude);
                params.put("appid",APP_ID);
                LetsdoSomeNetworking(params);
            }

            @Override
           public void onStatusChanged(String provider, int status, Bundle extras){

           }


            public void onProviderChanged(String provider){

            }


            @Override
            public void onProviderDisabled(String provider) {

            }

        };
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager
                .PERMISSION_GRANTED){
            return ;
        }
        mLocationManager.requestLocationUpdates(Location_Provider,MIN_TIME,MIN_DIASTANCE,mLocationListener); }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Location get Successfuly", Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            } else {

            }
        }}

    private void LetsdoSomeNetworking(RequestParams params){
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(WEATHER_URL,params,new JsonHttpResponseHandler(){
         @Override
         public void onSuccess(int statusCode, Header[] headers, JSONObject response){
             Toast.makeText(MainActivity.this, "Data get Success", Toast.LENGTH_SHORT).show();
             weatherData weatherD=weatherData.fromJson(response);
             updateUI(weatherD);
         }
         @Override
            public void onFailure(int statusCode,Header[]headers,Throwable throwable,JSONObject errorResponse){   }


        });   }
        private void updateUI(weatherData weather){
        Temperature.setText(weather.getmTemperature());
        NameOfcity.setText(weather.getmcity());
        weatherstate.setText(weather.getweatherType());
        int resourceID=getResources().getIdentifier(weather.getMicon(),"drawable",getPackageName());
    }
    @Override
    protected void onPause(){
        super.onPause();
        if(mLocationManager!=null){
           mLocationManager.removeUpdates(mLocationListener);
        }

    }
}
