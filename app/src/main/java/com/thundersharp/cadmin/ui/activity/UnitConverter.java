package com.thundersharp.cadmin.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.thundersharp.cadmin.R;

public class UnitConverter extends AppCompatActivity {
    RelativeLayout area,length,temperature,weight;
    ImageView areacal,scccallen,convwei,tempc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_converter);

        areacal=findViewById(R.id.areacal);
        scccallen=findViewById(R.id.scccallen);
        convwei=findViewById(R.id.convwei);
        tempc=findViewById(R.id.tempc);
        area=findViewById(R.id.area);
        temperature=findViewById(R.id.temperature);
        length=findViewById(R.id.length);
        weight=findViewById(R.id.weight);


        area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(UnitConverter.this,AreaConverter.class);
                startActivity(intent1);
            }
        });
        length.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(UnitConverter.this,LengthConverter.class);
                startActivity(intent2);
            }
        });
        weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3=new Intent(UnitConverter.this, WeightConverter.class);
                startActivity(intent3);
            }
        });
        temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4=new Intent(UnitConverter.this,TemperatureConverter.class);
                startActivity(intent4);
            }
        });
    }
}