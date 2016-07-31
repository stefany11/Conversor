package com.facci.conversorsp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivitySP extends AppCompatActivity {

    final String[] datos=new String[] {"DÓLAR","EURO","PESO MEXICÁNO"};

    private Spinner monedaActualSP;
    private Spinner monedaCambioSP;
    private EditText valorCambioET;
    private TextView resultadoTV;

    final private double factorDolarEuro=0.87;
    final private double factorPesoDolar=0.54;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_sp);


        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,datos);

        monedaActualSP= (Spinner) findViewById(R.id.monedaActualSP);
        monedaActualSP.setAdapter(adaptador);

        monedaCambioSP =(Spinner) findViewById(R.id.monedaCambioSP);

        SharedPreferences preferencias = getSharedPreferences("Mispreferencias",Context.MODE_PRIVATE);
        String tmpMonedaActual = preferencias.getString("monedaActual", "");
        String tmpMonedaCambio = preferencias.getString("monedaCambio", "");
        if (!tmpMonedaActual.equals("")){
            int  indice= adaptador.getPosition(tmpMonedaActual);
            monedaActualSP.setSelection(indice);
        }
        if(!tmpMonedaCambio.equals("")){
            int indice= adaptador.getPosition(tmpMonedaCambio);
            monedaCambioSP.setSelection(indice);
        }
    }

    public void clickConvertir(View v){

        monedaActualSP= (Spinner) findViewById(R.id.monedaActualSP);
        monedaCambioSP=(Spinner) findViewById(R.id.monedaCambioSP);
        valorCambioET=(EditText) findViewById(R.id.valorCambioET);
        resultadoTV=(TextView) findViewById(R.id.resustadoTV);

        String monedaActual=  monedaActualSP.getSelectedItem().toString();
        String monedaCambio= monedaCambioSP.getSelectedItem().toString();

        double valorCambio= Double.parseDouble(valorCambioET.getText().toString());

        double resultado= procesarConversion(monedaActual,monedaCambio,valorCambio);

        if (resultado>0){
            resultadoTV.setText(String.format("Por %5.2f %s, usted recibira %5.2f %s",valorCambio,monedaActual,resultado, monedaCambio));
            valorCambioET.setText("");

            SharedPreferences preferencias =getSharedPreferences("Mispreferencias", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor= preferencias.edit();
            editor.putString("monedaActual",monedaActual);
            editor.putString("monedaCambio",monedaCambio);

            editor.commit();

        }else{
            resultadoTV.setText(String.format("Usted recibira"));
            Toast.makeText(MainActivitySP.this,"Las opciones elegidas no tienen un factor de conversión",Toast.LENGTH_SHORT).show();
        }
    }

    private double procesarConversion(String monedaActual, String monedaCambio,double valorCambio){

        double resultadoConversion=0;
        switch (monedaActual){
            case  "DÓLAR":
               if (monedaCambio.equals("EURO"))
                   resultadoConversion= valorCambio * factorDolarEuro;
                if (monedaCambio.equals("PESO MEXICÁNO"))
                    resultadoConversion= valorCambio  /factorPesoDolar;
                break;
            case"EURO":
                 if (monedaCambio.equals("DÓLAR"))
                     resultadoConversion=valorCambio/ factorDolarEuro;

                    break;
            case "PESO MEXICÁNO":
                  if(monedaCambio.equals("DÓLAR")){
                      resultadoConversion= valorCambio *factorPesoDolar;
                  }
                    break;
        }
        return resultadoConversion;
    }
}




