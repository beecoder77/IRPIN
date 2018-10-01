package com.example.abito.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.content.Intent;

public class utamaActivity extends AppCompatActivity {

    ImageView ketair, tanah;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utama);
        ketair = (ImageView) findViewById(R.id.ketAir);
        ketair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ketinggian = new Intent(utamaActivity.this, ketinggianairActivity.class);
                startActivity(ketinggian);
            }
        });
        tanah = (ImageView) findViewById(R.id.tanah);
        tanah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tanah = new Intent(utamaActivity.this, kelembabanActivity.class);
                startActivity(tanah);
            }
        });
    }
}
