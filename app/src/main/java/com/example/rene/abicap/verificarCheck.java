package com.example.rene.abicap;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by CamiloDesarrollo on 07/12/2016.
 */

public class verificarCheck {
    private boolean sdDesponible, sdAccesoEscritura;

    public boolean checkearDescripcion(Context con){
        verificarEstado();
        if (sdAccesoEscritura && sdDesponible) {
            File ruta_sd = Environment.getExternalStoragePublicDirectory("/AbitekConfi/");//Environment.DIRECTORY_DOWNLOADS);
            File f = new File(ruta_sd.getAbsolutePath(), "mostrarDescripcion.txt");
            if (f.exists()) {
                try {
                    BufferedReader bufLec =
                            new BufferedReader(new InputStreamReader(new FileInputStream(f)));

                    String tex ;
                    while (!(tex = (bufLec.readLine()).trim()).equals(null)) {
                        if(tex.equalsIgnoreCase("true")) {
                            return true;
                        }
                    }
                    bufLec.close();

                } catch (Exception ex) {
                    Log.e("Fichero sd", "Error al escribir en la sd");
                }

            } else {
                return false;
            }
        }else{
            return false;
        }
        return false;
    }

    public boolean checkearContraArchivo(Context con){
        verificarEstado();
        if (sdAccesoEscritura && sdDesponible) {
            File ruta_sd = Environment.getExternalStoragePublicDirectory("/AbitekConfi/");//Environment.DIRECTORY_DOWNLOADS);
            File f = new File(ruta_sd.getAbsolutePath(), "validarContraArchivo.txt");
            if (f.exists()) {
                try {
                    BufferedReader bufLec =
                            new BufferedReader(new InputStreamReader(new FileInputStream(f)));

                    String tex ;
                    while (!(tex = (bufLec.readLine()).trim()).equals(null)) {
                        if(tex.equalsIgnoreCase("true")) {
                            return true;
                        }
                    }
                    bufLec.close();

                } catch (Exception ex) {
                    Log.e("Fichero sd", "Error al escribir en la sd");
                }

            } else {
                return false;
            }
        }else{
            return false;
        }
        return false;
    }

    public boolean checkearDuplicado(Context con){
        verificarEstado();
        if (sdAccesoEscritura && sdDesponible) {
            File ruta_sd = Environment.getExternalStoragePublicDirectory("/AbitekConfi/");//Environment.DIRECTORY_DOWNLOADS);
            File f = new File(ruta_sd.getAbsolutePath(), "chequeoDuplicado.txt");
            if (f.exists()) {
                try {
                    BufferedReader bufLec =
                            new BufferedReader(new InputStreamReader(new FileInputStream(f)));

                    String tex ;
                    while (!(tex = (bufLec.readLine()).trim()).equals(null)) {
                        if(tex.equalsIgnoreCase("true")) {
                            return true;
                        }
                    }
                    bufLec.close();

                } catch (Exception ex) {
                    Log.e("Fichero sd", "Error al escribir en la sd");
                }

            } else {
                return false;
            }
        }else{
            return false;
        }
        return false;
    }

    public Boolean sobreescribirCheckeo(String texto, String nomArchivo){
        verificarEstado();
        if (sdAccesoEscritura && sdDesponible) {
            File ruta_sd = Environment.getExternalStoragePublicDirectory("/AbitekConfi/");
            File f = new File(ruta_sd.getAbsolutePath(), nomArchivo);
            if (f.exists()) {
                try {
                    OutputStreamWriter fout =
                            new OutputStreamWriter(
                                    new FileOutputStream(f));

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

    public String obtenerIdCapt(Context con){
        verificarEstado();
        if (sdAccesoEscritura && sdDesponible) {
            File ruta_sd = Environment.getExternalStoragePublicDirectory("/AbitekConfi/");//Environment.DIRECTORY_DOWNLOADS);
            File f = new File(ruta_sd.getAbsolutePath(), "identificador.txt");
            if (f.exists()) {
                try {
                    BufferedReader bufLec =
                            new BufferedReader(new InputStreamReader(new FileInputStream(f)));

                    String tex ;
                    while (!(tex = (bufLec.readLine()).trim()).equals(null)) {
                            return tex;
                    }
                    bufLec.close();

                } catch (Exception ex) {
                    Log.e("Fichero sd", "Error al escribir en la sd");
                }

            } else {
            }
        }else{
        }
        return "0";
    }

    public Boolean editarIdCapt(String texto){
        verificarEstado();
        if (sdAccesoEscritura && sdDesponible) {
            File ruta_sd = Environment.getExternalStoragePublicDirectory("/AbitekConfi/");
            File f = new File(ruta_sd.getAbsolutePath(), "identificador.txt");
            if (f.exists()) {
                try {
                    OutputStreamWriter fout =
                            new OutputStreamWriter(
                                    new FileOutputStream(f));

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
}
