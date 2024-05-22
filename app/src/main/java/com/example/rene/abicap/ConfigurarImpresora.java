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

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class ConfigurarImpresora extends AppCompatActivity {
    private Button btnGuardarCambios,btnMenuPrinc;
    private EditText edIpImpresora, edPuertoImpresora;
    private boolean sdDesponible, sdAccesoEscritura;
    public IpComunicacion ipImp;
    private String  ipImpresora, puertoImpresora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_impresora);

        //DECLARACION DE COMPONENTES GRAFICOS
        //----------------------------------------------------------------------------
        btnGuardarCambios=(Button)findViewById(R.id.btnGuardarCambios);
        btnMenuPrinc=(Button)findViewById(R.id.btnMenuPrincipal);

        edIpImpresora=(EditText)findViewById(R.id.edIpImpresora);
        edPuertoImpresora=(EditText)findViewById(R.id.edPuertoImpresora);

        //----------------------------------------------------------------------------

        ipImp  = new IpComunicacion(); //de aca sale la ip de impresora y el puerto
        ipImpresora = ipImp.obtenerIpImpresora();
        puertoImpresora = ipImp.obtenerPuertoImpresora();

        verificarEstado();

        edIpImpresora.setText(ipImpresora);
        edPuertoImpresora.setText(puertoImpresora);

        edIpImpresora.requestFocus();
        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((edIpImpresora.getText().toString().equalsIgnoreCase("")) || edPuertoImpresora.getText().toString().equalsIgnoreCase("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConfigurarImpresora.this);
                    builder.setMessage("FAVOR INGRESAR DATOS")
                            .setTitle("Atención!!")
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
                    reemplazarArchivoIpImpresora();
                    reemplazarArchivoPuertoImpresora();

                    Intent i = getIntent();
                    setResult(RESULT_OK, i);

                    finish();
                }
            }
        });


        btnMenuPrinc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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

    public void reemplazarArchivoIpImpresora() {
        if (sdAccesoEscritura && sdDesponible) {
            File ruta_sd = Environment.getExternalStoragePublicDirectory("/AbitekConfi/");
            File f = new File(ruta_sd.getAbsolutePath(), "ipImpresora.txt");
            if (f.exists()) {
                try {
                    OutputStreamWriter fout =
                            new OutputStreamWriter(
                                    new FileOutputStream(f));

                    fout.write(edIpImpresora.getText().toString().trim());
                    fout.close();
                } catch (Exception ex) {
                    Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
                }
            }
        }
    }

    public void reemplazarArchivoPuertoImpresora() {
        if (sdAccesoEscritura && sdDesponible) {
            File ruta_sd = Environment.getExternalStoragePublicDirectory("/AbitekConfi/");
            File f = new File(ruta_sd.getAbsolutePath(), "puertoImpresora.txt");
            if (f.exists()) {
                try {
                    OutputStreamWriter fout =
                            new OutputStreamWriter(
                                    new FileOutputStream(f));

                    fout.write(edPuertoImpresora.getText().toString().trim());
                    fout.close();
                } catch (Exception ex) {
                    Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
                }
            }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        return false;
    }
}
