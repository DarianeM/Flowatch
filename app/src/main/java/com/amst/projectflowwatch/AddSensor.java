package com.amst.projectflowwatch;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddSensor extends AppCompatActivity {
    EditText IDmedidor, nameMedidor, ubicacionMedidor, pagoDay;
    private RequestQueue ListaRequest = null;
    private String msjSensor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sensor);

        /*recoger datos de otra actividad*/
        Intent intent = getIntent();
        msjSensor = intent.getStringExtra("userId");

        nameMedidor = findViewById(R.id.etNameMedidor);
        ubicacionMedidor = findViewById(R.id.etGPSmedidor);
        pagoDay = findViewById(R.id.etDate);

    }

    public void addSensor(View view){
        String NameMedidor = nameMedidor.toString();
        String UbiMedidor = ubicacionMedidor.toString();
        Integer dayPago = Integer.parseInt(pagoDay.getText().toString());
        String dateTime = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateTime = ZonedDateTime.now(ZoneId.of("-05:00")).format(DateTimeFormatter.ofPattern("MMM dd yyyy, hh:mm:ss a"));
        }


        Map<String,String> paramString = new HashMap();
        paramString.put("name",NameMedidor);
        paramString.put("userId",msjSensor);
        paramString.put("place",UbiMedidor);
        paramString.put("dateData", dateTime);


        JSONObject parametros = new JSONObject(paramString);
        try{
            parametros.put("paid",17);
            parametros.put("numericData",0);
        }catch (JSONException e){
            throw new RuntimeException();
        }

        String url_post = "https://flowwatch-api.onrender.com/api/users/sensors";
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url_post, parametros,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            Toast.makeText(AddSensor.this, "Medidor Añadido", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog alertDialog = new AlertDialog.Builder(AddSensor.this).create();
                alertDialog.setTitle("Alerta");
                alertDialog.setMessage("Error de Conexion");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
        ListaRequest.add(request);
    }
}