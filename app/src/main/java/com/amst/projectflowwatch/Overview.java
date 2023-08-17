package com.amst.projectflowwatch;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class Overview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);


        PieChart pieCharts = findViewById(R.id.pieChart);

        ArrayList<PieEntry> v = new ArrayList<>();
        v.add(new PieEntry(10,""));
        v.add(new PieEntry(35,""));

        PieDataSet pieDataSet = new PieDataSet(v,"valores");
        pieDataSet.setColors(ColorTemplate.LIBERTY_COLORS);

        PieData pieData = new PieData(pieDataSet);

        pieCharts.setData(pieData);
        pieCharts.setCenterText("h");
        pieCharts.setUsePercentValues(true);
        pieCharts.getData().setValueTextSize(20f);
        pieCharts.getLegend().setEnabled(false);
        pieCharts.setTransparentCircleColor(Color.WHITE);
        pieCharts.setRotationEnabled(false);
    }

    public void consumoActual(){

    }
}