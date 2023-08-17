package com.amst.projectflowwatch;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class Overview extends AppCompatActivity {

    public BarChart graficoBarras;
    private RequestQueue ListaRequest = null;
    private LinearLayout contenedorDatosMedidor;
    private Map<String, TextView> temperaturasTVs;
    private Map<String, TextView> fechasTVs;
    private Overview contexto;
    public EditText edtxTemp,edtx_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        ListaRequest = Volley.newRequestQueue(this);
        contexto = this;

        /*recoger datos de otra actividad*/
        //Intent intent = getIntent();
        //String msjSensor = intent.getStringExtra(MainActivity.SensorSelecionado);

        this.iniciarGrafico();
        this.consumoActual();
        this.historico();

    }

    public void consumoActual(){
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

    public void historico(){
        //recoger datos para anexar al url
        String url_registros = "https://amstlab.onrender.com/api/lecturas";//+ msjSensor;
        JsonArrayRequest requestRegistros = new JsonArrayRequest(Request.Method.GET, url_registros, null,
                response -> {
                    actualizarGrafico(response);
                }, error -> System.out.println(error)
        );
        ListaRequest.add(requestRegistros);
    }

    public void iniciarGrafico() {
        graficoBarras = findViewById(R.id.barChart);
        graficoBarras.getDescription().setEnabled(false);
        graficoBarras.setMaxVisibleValueCount(60);
        graficoBarras.setPinchZoom(false);
        graficoBarras.setDrawBarShadow(false);
        graficoBarras.setDrawGridBackground(false);
        XAxis xAxis = graficoBarras.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        graficoBarras.getAxisLeft().setDrawGridLines(false);
        graficoBarras.animateY(1500);
        graficoBarras.getLegend().setEnabled(false);
    }

    private void actualizarGrafico(JSONArray ConsumoMedidor){
        JSONObject registro_datos;
        String datos;
        String date;
        int count = 1;
        float datosM_val;
        ArrayList<BarEntry> dato_consumo = new ArrayList<>();
        try {
            for (int i = 0; i < ConsumoMedidor.length(); i++) {

                registro_datos = ConsumoMedidor.getJSONObject(i);
                if (registro_datos.getString("key").equals("medidor")) {
                    datos = registro_datos.getString("value");
                    date = registro_datos.getString("data_created");
                    datosM_val = Float.parseFloat(datos);
                    dato_consumo.add(new BarEntry(count, datosM_val));
                    count++;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("error");
        }
        llenarGrafico(dato_consumo);
    }

    private void llenarGrafico(ArrayList<BarEntry> dato_consumo){
        BarDataSet datoConsumoDataSet;
        if (graficoBarras.getData() != null &&
                graficoBarras.getData().getDataSetCount() > 0) {
            datoConsumoDataSet = (BarDataSet) graficoBarras.getData().getDataSetByIndex(0);
            datoConsumoDataSet.setValues(dato_consumo);
            graficoBarras.getData().notifyDataChanged();
            graficoBarras.notifyDataSetChanged();
        } else {
            datoConsumoDataSet = new BarDataSet(dato_consumo, "Data Set");
            datoConsumoDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
            datoConsumoDataSet.setDrawValues(true);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(datoConsumoDataSet);
            BarData data = new BarData(dataSets);
            graficoBarras.setData(data);
            graficoBarras.setFitBars(true);
        }
        graficoBarras.invalidate();

        new Handler(Looper.getMainLooper()).postDelayed(() -> historico(), 3000);
    }
}