package com.example.rene.abicap;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by CamiloDesarrollo on 09/11/2016.
 */

public class CrearArchivosIniciales extends AsyncTask<String,Void,Void>{
    private ProgressDialog p;
    private Context contexto;

    public CrearArchivosIniciales(Context c){
        contexto = c;
    }

    @Override
    protected Void doInBackground(String... strings) {
        crearCarpetaRaiz();
        return null;
    }

    @Override
    protected  void onPostExecute(Void result){
        p.dismiss();
    }

    @Override
    protected void onPreExecute(){
        p = ProgressDialog.show(contexto, "CONFIGURACION", "VERIFICANDO CONFIGURACION INICIAL", false);
    }

    public void crearCarpetaRaiz() {

        File ruta_sd = Environment.getExternalStoragePublicDirectory("/AbitekConfi");//Environment.DIRECTORY_DOWNLOADS);
        if (!ruta_sd.exists()) {
            try{
                ruta_sd.mkdirs();
                CrearArchivosIniciales(ruta_sd);
            }catch (Exception e){
                Log.e("Folder", "Error al crear carpeta en tarjeta SD");
            }
        }else{

            CrearArchivosIniciales(ruta_sd);
        }
    }

    public void CrearArchivosIniciales(File ruta){
        //CREAMOS ARCHIVO DE CONFIGURACION DE IDENTIFICADOR CAPTURADOR CAPTURADOR

        try
        {

            File f = new File(ruta.getAbsolutePath(), "identificador.txt");
            if(!f.exists()) {
                OutputStreamWriter fout =
                        new OutputStreamWriter(
                                new FileOutputStream(f));

                fout.write("1");
                fout.close();
            }
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero identificador a tarjeta SD");
        }
        //CREAMOS ARCHIVO CONFIGURACION DE UBICACION
        try
        {

            File f = new File(ruta.getAbsolutePath(), "confubicacion.txt");
            if(!f.exists()) {
                OutputStreamWriter fout =
                        new OutputStreamWriter(
                                new FileOutputStream(f));

                fout.write("0001");
                fout.close();
            }
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero confubicacion a tarjeta SD");
        }

        try
        {
            File f = new File(ruta.getAbsolutePath(), "login.txt");
            if(!f.exists()) {
                OutputStreamWriter fout =
                        new OutputStreamWriter(
                                new FileOutputStream(f));

                fout.write("admin;admin");
                fout.close();
            }
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero confubicacion a tarjeta SD");
        }

        try
        {
            File f = new File(ruta.getAbsolutePath(), "ipValidador.txt");
            if(!f.exists()) {
                OutputStreamWriter fout =
                        new OutputStreamWriter(
                                new FileOutputStream(f));

                fout.write("190.14.57.107:8080");
                fout.close();
            }
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero confubicacion a tarjeta SD");
        }

        try
        {
            File f = new File(ruta.getAbsolutePath(), "chequeoDuplicado.txt");
            if(!f.exists()) {
                OutputStreamWriter fout = new OutputStreamWriter( new FileOutputStream(f));

                fout.write("false");
                fout.close();
            }
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero confubicacion a tarjeta SD");
        }

        try
        {
            File f = new File(ruta.getAbsolutePath(), "validarContraArchivo.txt");
            if(!f.exists()) {
                OutputStreamWriter fout =
                        new OutputStreamWriter(
                                new FileOutputStream(f));

                fout.write("false");
                fout.close();
            }
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero confubicacion a tarjeta SD");
        }

        try
        {
            File f = new File(ruta.getAbsolutePath(), "mostrarDescripcion.txt");
            if(!f.exists()) {
                OutputStreamWriter fout =
                        new OutputStreamWriter(
                                new FileOutputStream(f));

                fout.write("true");
                fout.close();
            }
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero confubicacion a tarjeta SD");
        }
        try
        {
            File f = new File(ruta.getAbsolutePath(), "ipImpresora.txt");
            if(!f.exists()) {
                OutputStreamWriter fout =
                        new OutputStreamWriter(
                                new FileOutputStream(f));

                fout.write("192.168.0.43");
                fout.close();
            }
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero confubicacion a tarjeta SD");
        }
        try
        {
            File f = new File(ruta.getAbsolutePath(), "puertoImpresora.txt");
            if(!f.exists()) {
                OutputStreamWriter fout =
                        new OutputStreamWriter(
                                new FileOutputStream(f));

                fout.write("9100");
                fout.close();
            }
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero confubicacion a tarjeta SD");
        }
        try
        {
            File f = new File(ruta.getAbsolutePath(), "arranque.txt");
            if(!f.exists()) {
                OutputStreamWriter fout =
                        new OutputStreamWriter(
                                new FileOutputStream(f));

                fout.write("\u0010CT~~CD,~CC^~CT~\n" +
                        "^XA~TA000~JSN^LT0^MNW^MTT^PON^PMN^LH0,0^JMA^PR4,4~SD15^JUS^LRN^CI0^XZ\n" +
                        "^XA\n" +
                        "^MMT\n" +
                        "^PW799\n" +
                        "^LL0400\n" +
                        "^LS0\n" +
                        "^FT153,259^A0N,44,28^FH\\^FD@parametro3^FS\n" +
                        "^FT151,111^A0N,39,19^FH\\^FD@parametro2^FS\n" +
                        "^FT152,63^A0N,42,28^FH\\^FD@parametro1^FS\n" +
                        "^FT440,278^BQN,2,6\n" +
                        "^FH\\^FDMA,@parametro1^FS\n" +
                        "^PQ@parametro4,0,1,Y^XZ"
                );
                fout.close();
            }
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero confubicacion a tarjeta SD");
        }
        try
        {
            File f = new File(ruta.getAbsolutePath(), "etiquetaUbicacion.txt");
            if(!f.exists()) {
                OutputStreamWriter fout =
                        new OutputStreamWriter(
                                new FileOutputStream(f));

                fout.write("\u0010CT~~CD,~CC^~CT~\n" +
                        "^XA~TA000~JSN^LT0^MNW^MTT^PON^PMN^LH0,0^JMA^PR4,4~SD15^JUS^LRN^CI0^XZ\n" +
                        "^XA\n" +
                        "^MMT\n" +
                        "^PW799\n" +
                        "^LL0400\n" +
                        "^LS0\n" +
                        "^FT368,206^A0N,170,72^FH\\^FD@parametro1^FS\n" +
                        "^FT201,246^BQN,2,7\n" +
                        "^FH\\^FDMA,@parametro1^FS\n" +
                        "^PQ1,0,1,Y^XZ"
                );
                fout.close();
            }
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero confubicacion a tarjeta SD");
        }

    }
}
