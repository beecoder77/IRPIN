package com.example.abito.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static java.security.AccessController.getContext;
import static com.example.abito.login.BuildConfig.DEVICE_IC;
import static com.example.abito.login.BuildConfig.TOKEN_API;
import static com.example.abito.login.BuildConfig.URL_API;
public class kelembabanActivity extends AppCompatActivity {
    private String mURL = URL_API + "/lembab";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelembaban);

        Button nyala = (Button)findViewById(R.id.nyala);
        nyala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HTTPAsyncGPIO(kelembabanActivity.this).execute(mURL + "/gpio/control", "4", "1");
            }
        });
        cekStatus();
        cekStatusGPIO();
    }

    // cek status kelembaban
    public void cekStatus () {
        new DownloadWebpageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {
                getStatus(object);
            }
        }).execute(mURL + "/adc/data");
    }
    public void cekStatusGPIO() {
        new DownloadWebpageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {
                getStatus(object);
            }
        }).execute(mURL + "/gpio/data");
    }
    // ini trigger buat nelakukan sesuatu
    public void getStatus (JSONObject object){
        try {
            //JSONObject table = new JSONObject(jsonResponse);
            JSONObject rows = object.getJSONObject("data");
            JSONObject result = rows.getJSONObject("result");
            String adc = result.getString("0");
            Log.d("coba", adc);
            float a = Float.valueOf(adc);
            TextView hasill = findViewById(R.id.hasill);
            hasill.setText(Float.toString(a));
            //String pin_4= result.getString("4");
            if (a > 1000) {
                Toast.makeText(kelembabanActivity.this, "KERING", Toast.LENGTH_SHORT).show();
                final Button nyala = findViewById(R.id.nyala);
                nyala.setText("NYALAKAN");
                nyala.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new HTTPAsyncGPIO(kelembabanActivity.this).execute(mURL + "/gpio/control", "4", "0");
                        //10 detik
                        nyala.setText("MATIKAN");
                        nyala.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new HTTPAsyncGPIO(kelembabanActivity.this).execute(mURL + "/gpio/control", "4", "1");
                            }
                        });
                    }
                });
            } else if (a < 1000) {
                Toast.makeText(kelembabanActivity.this, "CUKUP BASAH", Toast.LENGTH_SHORT).show();
                final Button nyala = findViewById(R.id.nyala);
                nyala.setText("MATIKAN");
                nyala.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new HTTPAsyncGPIO(kelembabanActivity.this).execute(mURL + "/gpio/control", "4", "1");
                        //3 detik
                        nyala.setText("NYALAKAN");
                        nyala.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new HTTPAsyncGPIO(kelembabanActivity.this).execute(mURL + "/gpio/control", "4", "0");
                            }
                        });
                    }
                });
            } else {
                Toast.makeText(kelembabanActivity.this, "Pembacaan status gagal...", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
