package com.example.rene.abicap;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by CamiloDesarrollo on 14/12/2016.
 */

public class BuscarIdCapt {

    private boolean sdDesponible, sdAccesoEscritura;

    public String obtenerDato(){
        verificarEstado();
        String respuesta = "SinID";
        if (sdAccesoEscritura && sdDesponible) {
            File ruta_sd = Environment.getExternalStoragePublicDirectory("/AbitekConfi/");//Environment.DIRECTORY_DOWNLOADS);
            File f = new File(ruta_sd.getAbsolutePath(), "identificador.txt");
            if (f.exists()) {
                try {
                    BufferedReader bufLec =
                            new BufferedReader(new InputStreamReader(new FileInputStream(f)));

                    String tex ;
                    if(!(tex = (bufLec.readLine()).trim()).equals(null)) {
                        respuesta = tex.toString().trim();
                        Log.e("leyendo archivo", tex);
                    }else{
                        respuesta = "SinID";
                    }
                    bufLec.close();

                } catch (Exception ex) {
                    Log.e("Fichero sd", "Error al escribir en la sd");
                    respuesta = "SinID";
                }

            } else {
                respuesta = "SinID";
            }
        }
        return respuesta;
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
}
