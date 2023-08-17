package com.amst.projectflowwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void overview(View view){
        Intent i = new Intent(this,Overview.class);
        startActivity(i);
    }
    public void addSensor(View view){
        Intent i = new Intent(this,AddSensor.class);
        startActivity(i);
    }
}