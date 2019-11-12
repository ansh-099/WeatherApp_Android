package com.example.anshulsingh.clima;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Main2Activity extends AppCompatActivity {

    EditText enterLat,enterLon;
    Button btnGetLoc;
    public String currentLongitude;
    public String currentLatitude;
    ImageView layout1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        layout1=findViewById(R.id.layout12);
        btnGetLoc = findViewById(R.id.btnGetLoc);
        enterLat = findViewById(R.id.enterLat);
        enterLon = findViewById(R.id.enterLon);




        Picasso.get()
                .load("https://i.redd.it/ihfnlpbze7o01.jpg").fit()
                .centerCrop()
                .into(layout1);

       btnGetLoc.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               currentLatitude = enterLat.getText().toString();
               currentLongitude = enterLon.getText().toString();
               NetworkTask networkTask = new NetworkTask();
               networkTask.execute("http://api.openweathermap.org/data/2.5/weather?lat="+currentLatitude+"&lon="+currentLongitude+"&appid=e72ca729af228beabd5d20e3b7749713");

           }
       });


    }
    class NetworkTask extends AsyncTask<String , Void , String> {


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
        TextView tvTemp = findViewById(R.id.tvTemp2);
        TextView tvCity =findViewById(R.id.tvCity2);
        tvCity.setText(strCity);
        tvTemp.setText(strTemp.substring(0,4)+"° ");

    }
}
