package com.amst.projectflowwatch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddSensor extends AppCompatActivity {
    EditText IDmedidor, nameMedidor, ubicacionMedidor, pagoDay;
    private RequestQueue ListaRequest = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sensor);

        IDmedidor = findViewById(R.id.etID);
        nameMedidor = findViewById(R.id.etNameMedidor);
        ubicacionMedidor = findViewById(R.id.etGPSmedidor);
        pagoDay = findViewById(R.id.etDate);

    }

    public void addSensor(View view){
        String idMedidor = IDmedidor.getText().toString();
        String NameMedidor = nameMedidor.getText().toString();
        String UbiMedidor = ubicacionMedidor.getText().toString();
        Integer dayPago = Integer.parseInt(pagoDay.getText().toString());


        Map<String,String> paramString = new HashMap();
        paramString.put("name",NameMedidor);
        paramString.put("userId",idMedidor);
        paramString.put("place",UbiMedidor);

        JSONObject parametros = new JSONObject(paramString);
        try{
            parametros.put("paid",dayPago);
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