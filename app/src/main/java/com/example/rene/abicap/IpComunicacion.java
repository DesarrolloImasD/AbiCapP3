package com.example.rene.abicap;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Miguel Bustamante on 05/12/2018.
 */

public class IpComunicacion {

    private boolean sdDesponible, sdAccesoEscritura;

    public String obtenerIpImpresora(){
        verificarEstado();
        String respuesta = "ERROR";
        if (sdAccesoEscritura && sdDesponible) {
            File ruta_sd = Environment.getExternalStoragePublicDirectory("/AbitekConfi/");//Environment.DIRECTORY_DOWNLOADS);
            File f = new File(ruta_sd.getAbsolutePath(), "ipImpresora.txt"); //confubicacion.txt
            if (f.exists()) {
                try {
                    BufferedReader bufLec = new BufferedReader(new InputStreamReader(new FileInputStream(f)));

                    String tex ;
                    if(!(tex = (bufLec.readLine()).trim()).equals(null)) {
                        respuesta = tex.toString().trim();
                        Log.e("leyendo archivo", tex);
                    }else{
                        respuesta = "ERROR";
                    }
                    bufLec.close();

                } catch (Exception ex) {
                    Log.e("Fichero sd", "Error al escribir en la sd");
                    respuesta = "ERROR";
                }

            } else {
                respuesta = "ERROR";
            }
        }
        return respuesta;
    }

    public String obtenerPuertoImpresora(){
        verificarEstado();
        String respuesta = "ERROR";
        if (sdAccesoEscritura && sdDesponible) {
            File ruta_sd = Environment.getExternalStoragePublicDirectory("/AbitekConfi/");//Environment.DIRECTORY_DOWNLOADS);
            File f = new File(ruta_sd.getAbsolutePath(), "puertoImpresora.txt"); //confubicacion.txt
            if (f.exists()) {
                try {
                    BufferedReader bufLec = new BufferedReader(new InputStreamReader(new FileInputStream(f)));

                    String tex ;
                    if(!(tex = (bufLec.readLine()).trim()).equals(null)) {
                        respuesta = tex.toString().trim();
                        Log.e("leyendo archivo", tex);
                    }else{
                        respuesta = "ERROR";
                    }
                    bufLec.close();

                } catch (Exception ex) {
                    Log.e("Fichero sd", "Error al escribir en la sd");
                    respuesta = "ERROR";
                }

            } else {
                respuesta = "ERROR";
            }
        }
        return respuesta;
    }

    public Boolean editarIpImpresora(String texto){
        verificarEstado();
        if (sdAccesoEscritura && sdDesponible) {
            File ruta_sd = Environment.getExternalStoragePublicDirectory("/AbitekConfi/");
            File f = new File(ruta_sd.getAbsolutePath(), "ipImpresora.txt");
            if (f.exists()) {
                try {
                    OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(f));
                    fout.write(texto);
                    fout.close();
                    return true;
                } catch (Exception ex) {
                    Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
                }
            }
        }
        return false;
    }

    public Boolean editarPuertoImpresora(String texto){
        verificarEstado();
        if (sdAccesoEscritura && sdDesponible) {
            File ruta_sd = Environment.getExternalStoragePublicDirectory("/AbitekConfi/");
            File f = new File(ruta_sd.getAbsolutePath(), "puertoImpresora.txt");
            if (f.exists()) {
                try {
                    OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(f));
                    fout.write(texto);
                    fout.close();
                    return true;
                } catch (Exception ex) {
                    Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
                }
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


}
