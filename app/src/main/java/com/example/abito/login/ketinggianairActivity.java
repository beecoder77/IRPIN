package com.example.abito.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.abito.login.BuildConfig.DEVICE_IC;
import static com.example.abito.login.BuildConfig.TOKEN_API;
import static com.example.abito.login.BuildConfig.URL_API;

public class ketinggianairActivity extends AppCompatActivity {
    private String mURLL = "https://api.arkademy.com:8443/v0/arkana/device/presets/IO/tinggi";
    private String mURL = URL_API + "/lembab";
    MenuItem item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ketinggianair);
        cekStatus();
        //cekStatusGPIO();
    }
    public void cekStatus() {
        new DownloadWebpageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {
                getStatus(object);
            }
        }).execute( mURLL + "/usc/distance");
    }
    public void cekStatusGPIO() {
        new DownloadWebpageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {
                getStatus(object);
            }
        }).execute(mURL + "/gpio/data");
    }
    public void getStatus(JSONObject object) {
        try {
            //JSONObject table = new JSONObject(jsonResponse);
            JSONObject rows = object.getJSONObject("data");
            JSONObject result = rows.getJSONObject("result");
            String distance = result.getString("0");
            Log.d("coba", distance);
            float a = Float.valueOf(distance);
            TextView ket = findViewById(R.id.ket);
            ket.setText(Float.toString(a));
            //String pin_5= result.getString("5");
            // String pin_5 = result.getString("5");
            if (a<11) {
                Toast.makeText(getApplicationContext(), "AIR CUKUP BANYAK", Toast.LENGTH_SHORT).show();
                final Button siram = findViewById(R.id.siram);
                siram.setText("SIRAM");
                siram.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new HTTPAsyncGPIO(ketinggianairActivity.this).execute(mURL + "/gpio/control", "5", "0");
                        siram.setText("MATIKAN");
                        siram.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new HTTPAsyncGPIO(ketinggianairActivity.this).execute(mURL + "/gpio/control", "5", "1");
                            }
                        });
                    }
                });
            } else if (a>20) {
                Toast.makeText(getApplicationContext(), "AIR TIDAK CUKUP BANYAK", Toast.LENGTH_SHORT).show();
                Button siram = findViewById(R.id.siram);
                siram.setText("SIRAM");
                siram.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"AIR TIDAK CUKUP BANYAK!!!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getBaseContext(), "Pembacaan status gagal...", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
