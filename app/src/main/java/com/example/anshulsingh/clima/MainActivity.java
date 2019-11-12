package com.example.anshulsingh.clima;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
   public double currentLongitude;
   public double currentLatitude;
   ImageView layout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout1=findViewById(R.id.layout1);
//        Picasso.get().load("https://i.redd.it/ihfnlpbze7o01.jpg").into(layout1);

        Picasso.get()
                .load("https://i.redd.it/ihfnlpbze7o01.jpg").fit()
                .centerCrop()
                .into(layout1);
        getDeviceLocation();



                NetworkTask networkTask = new NetworkTask();
                networkTask.execute("http://api.openweathermap.org/data/2.5/weather?lat="+currentLatitude+"&lon="+currentLongitude+"&appid=e72ca729af228beabd5d20e3b7749713");





    }

    class NetworkTask extends AsyncTask<String , Void , String>{


        @Override
        protected String doInBackground(String... strings) {

            String Stringurl = strings[0];
            try {
                URL url = new URL(Stringurl);
               HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();

                Scanner scanner = new Scanner(inputStream);
                scanner.useDelimiter("\\A");

                if(scanner.hasNext())
                {
                    String s = scanner.next();
                    return s;
                }

            } catch (MalformedURLException e) {
                Log.d("hey",e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("hey1",e.toString());


            }

            return "Failed";


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                parseJSON(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            tv = findViewById(R.id.tv);
//            tv.setText(s);

        }
    }

    void parseJSON(String s) throws JSONException {

        JSONObject jsonObject = new JSONObject(s);
        JSONObject items =  jsonObject.getJSONObject("main");
        String strTemp= items.getString("temp");
        strTemp= String.valueOf(Double.valueOf(strTemp)-273.15);
        String strCity = jsonObject.getString("name");
        Log.d("hey",strCity+strTemp);
        TextView tvTemp = findViewById(R.id.tvTemp);
        TextView tvCity =findViewById(R.id.tvCity);
        tvCity.setText(strCity);
        tvTemp.setText(strTemp.substring(0,4)+"° ");

    }

    private void getDeviceLocation() {
        //get location manger instance
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        //check if we have permission to access device location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //add location change listener with update duration 2000 millicseconds or 10 meters
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, new LocationListener() {
            public void onLocationChanged(Location location) {
                currentLongitude = location.getLongitude();
                currentLatitude = location.getLatitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }

        });


        //get last known location to start with
        Location myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        currentLatitude = myLocation.getLatitude();
        currentLongitude = myLocation.getLongitude();
    }


}

//http://api.openweathermap.org/data/2.5/weather?lat=37.3230&lon=-122.0322&appid=e72ca729af228beabd5d20e3b7749713
