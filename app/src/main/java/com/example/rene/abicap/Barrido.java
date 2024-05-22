package com.example.rene.abicap;

import android.os.Environment;
import android.provider.LiveFolders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.camilodesarrollo.abicap.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Barrido extends AppCompatActivity {

    private Button btnBuscar,btnPrueba;
    private TextView texto;
    private boolean sdDesponible = false;
    private boolean sdAccesoEscritura = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barrido);

        btnBuscar = (Button)findViewById(R.id.btnIngresar);
        texto = (TextView)findViewById(R.id.txtDescripcion);
        btnPrueba = (Button)findViewById(R.id.button);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /** try{
                    InputStream lec =
                            getResources().openRawResource(R.raw.conf);
                    BufferedReader bufLec =
                            new BufferedReader(new InputStreamReader(lec));
                    String linea = bufLec.readLine();
                    texto.setText(linea);

                    lec.close();

                }catch (Exception ex)
                {
                    Log.e("Ficheros", "Error al leer fichero desde recurso raw");
                }*/
            }
        });

        btnPrueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                /**if(sdAccesoEscritura && sdDesponible){
                    try {
                        File ruta_sd = Environment.getExternalStorageDirectory();
                        File f = new File(ruta_sd.getAbsolutePath(), "AbiConf.txt");


                        OutputStreamWriter rea =
                                new OutputStreamWriter(new FileOutputStream(f));

                        rea.write("vamos vamos");
                        rea.close();
                    }
                    catch (Exception ex)
                    {
                        Log.e("Fichero sd","Error al escribir en la sd");
                    }
                }*/



            if(sdAccesoEscritura && sdDesponible){


                    File ruta_sd = Environment.getExternalStorageDirectory();
                    File f = new File(ruta_sd.getAbsolutePath(), "AbiConf.txt");
                    if (f.exists()) {
                        try {
                            BufferedReader bufLec =
                                    new BufferedReader(new InputStreamReader(new FileInputStream(f)));

                            String tex = bufLec.readLine();
                            texto.setText(tex);
                            bufLec.close();
                        }
                        catch(Exception ex)
                        {
                            Log.e("Fichero sd", "Error al escribir en la sd");
                        }

                }else{

                        try {
                            File ruta_ = Environment.getExternalStorageDirectory();
                            File f_ = new File(ruta_.getAbsolutePath(), "AbiConf.txt");


                            OutputStreamWriter rea =
                                    new OutputStreamWriter(new FileOutputStream(f_));

                            rea.write("pal que lee");
                            rea.close();
                            texto.setText("se acaba de crear el archivo");
                        }
                        catch (Exception ex)
                        {

                            Log.e("Fichero sd","Error al escribir en la sd");
                        }

                }
            }

        }
        });
    }
}
