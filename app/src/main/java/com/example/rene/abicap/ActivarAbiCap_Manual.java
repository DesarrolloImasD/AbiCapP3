package com.example.rene.abicap;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camilodesarrollo.abicap.R;

import java.io.UnsupportedEncodingException;


public class ActivarAbiCap_Manual extends AppCompatActivity {

    private TextView tvLicencia, tvNumeroSerial;
    private EditText edEncriptado;
    private Button btnActivar,btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activar_manual);

        final String serialDispositivo = Build.SERIAL;

        //CAPA GRAFICA
        //-------------------------------------------------------------------
        tvLicencia = (TextView) findViewById(R.id.tvLicencia);
        tvNumeroSerial = (TextView) findViewById(R.id.tvNumeroSerial);
        btnActivar = (Button)findViewById(R.id.btnActivar);
        btnMenu = (Button)findViewById(R.id.btnMenu);
        edEncriptado = (EditText) findViewById(R.id.edEncriptado);
        //-------------------------------------------------------------------
        tvNumeroSerial.setText("Número de Serie : "+serialDispositivo);

        btnActivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Encriptador encriptador = new Encriptador();
                try {
                    encriptador.encriptar();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                //Toast.makeText(ActivarAbiCap.this, "Encriptacion es: "+encriptador.serialEncriptado, Toast.LENGTH_SHORT).show();

                if(!edEncriptado.getText().toString().trim().equalsIgnoreCase(""))
                {
                    if(edEncriptado.getText().toString().trim().equals(encriptador.encriptadoA64)){
                        //activa el dispositivo por serialencriptada
                        forzarActivacion();

                    }else{
                        Toast.makeText(ActivarAbiCap_Manual.this, "Debes Ingresar una licencia valida", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    public void forzarActivacion(){
        try{
            SqlHelper sqlHel = new SqlHelper(ActivarAbiCap_Manual.this);
            SQLiteDatabase db = sqlHel.getWritableDatabase();
            if (db != null) {
                String SQL = "INSERT INTO MAEUBICACION (codigo,descripcion) VALUES ('VALIDADO','VALIDADO')";
                db.execSQL(SQL);
            }
            db.close();
            Intent i = new Intent(ActivarAbiCap_Manual.this ,MainActivity.class);
            startActivity(i);
            Toast.makeText(this, "ABICAP ACTIVADO", Toast.LENGTH_SHORT).show();
            finish();
        }catch (Exception ex){
            Log.e(ex.toString(), "Error");
        }
    }


}