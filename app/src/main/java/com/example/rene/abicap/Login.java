package com.example.rene.abicap;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.camilodesarrollo.abicap.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Login extends AppCompatActivity {
    private boolean sdDesponible, sdAccesoEscritura;
    private Button btnCancelar,btnEntrar;
    private EditText edPass,edUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN );

        //CAPA GRAFICA--------------------------------------
        btnCancelar = (Button)findViewById(R.id.btnCancelar);
        btnEntrar = (Button)findViewById(R.id.btnEntrar);

        edUsuario = (EditText)findViewById(R.id.edUsuario);
        edPass = (EditText)findViewById(R.id.edPass);
        //----------------------------------------------------------------------------------

        edUsuario.requestFocus();
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getIntent();
                setResult(RESULT_CANCELED, i);
                finish();
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String u = edUsuario.getText().toString().trim();
                String p = edPass.getText().toString().trim();
                if(u.equalsIgnoreCase("") || p.equalsIgnoreCase("")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    builder.setMessage("Favor Ingresar Datos Requeridos")
                            .setTitle("Precaución")
                            .setCancelable(false)
                            .setNeutralButton("Aceptar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }else {
                    Boolean b = obtenerDato(u, p);
                    if (b) {
                        Intent i = getIntent();
                        setResult(RESULT_OK, i);
                        finish();
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setMessage("Rut o Contraseña Incorrectos")
                                .setTitle("Error")
                                .setCancelable(false)
                                .setNeutralButton("Aceptar",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            }
        });
    }



    public boolean obtenerDato(String usua,String contra){
        verificarEstado();
        if (sdAccesoEscritura && sdDesponible) {
            File ruta_sd = Environment.getExternalStoragePublicDirectory("/AbitekConfi/");//Environment.DIRECTORY_DOWNLOADS);
            File f = new File(ruta_sd.getAbsolutePath(), "login.txt");
            if (f.exists()) {
                try {
                    BufferedReader bufLec =
                            new BufferedReader(new InputStreamReader(new FileInputStream(f)));

                    String tex ;
                    String usr;
                    String pass;
                    if(!(tex = (bufLec.readLine()).trim()).equals(null)) {
                        String[] dividir = tex.split(";");
                        try {
                            usr = dividir[0];
                        }catch (Exception ex){
                            usr = "NONE";
                        }
                        try {
                            pass = dividir[1];
                        }catch (Exception ex){
                            pass = "NONE";
                        }
                        if(usr.equalsIgnoreCase(usua) && pass.equalsIgnoreCase(contra)){
                            String camilo ="le pusiste";
                            return true;
                        }
                    }else{
                        return false;
                    }
                    bufLec.close();

                } catch (Exception ex) {
                    Log.e("Fichero sd", "Error al escribir en la sd");
                    return false;
                }

            } else {
                return false;
            }
        }
        return false;
    }

    public void verificarEstado(){
        String estado = Environment.getExternalStorageState();

        if(estado.equals(Environment.MEDIA_MOUNTED))
        {
            sdDesponible = true;
            sdAccesoEscritura = true;
        }
        else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
        {
            sdDesponible = true;
            sdAccesoEscritura = false;

        }else{
            sdDesponible = false;
            sdAccesoEscritura = false;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        return false;
    }
}
