package com.example.rene.abicap;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camilodesarrollo.abicap.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ImprimirUbicacion extends AppCompatActivity {
    private String ubicacionGlobal, ipImpresora, puertoImpresora;
    private TextView tvIPImpresora, tvPuertoImpresora;
    private Button btnMenuPrincipal,btnImprimir;
    public Beeper b;
    public verificarMiscelaneos verMis;
    public verificarCheck verifChk;
    public UbicacionDato ubiDat;
    public IpComunicacion ipImp;
    private String parametro1;
    private ProgressDialog p;
    private boolean sdDesponible, sdAccesoEscritura;
    private String textoEnviar;
    private Socket socket;
    String TAG = "TGsocket";
    private EditText edUbicacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_imprimir_ubicacion);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN );

            //DECLARAR INTERFAZ GRAFICA
            //-----------------------------------------------------------------------------------------------
            btnImprimir = (Button)findViewById(R.id.btnImprimir);
            btnMenuPrincipal = (Button)findViewById(R.id.btnMenuPrincipal);

            edUbicacion = (EditText)findViewById(R.id.edUbicacion);
            tvIPImpresora = (TextView)findViewById(R.id.tvIPImpresora);
            tvPuertoImpresora = (TextView)findViewById(R.id.tvPuertoImpresora );
            //-----------------------------------------------------------------------------------------------
            //DECLARO VARIABLES GLOBALES
            //---------------------------------------------------------------------------------------
            b = new Beeper();
            verMis = new verificarMiscelaneos();
            verifChk = new verificarCheck();
            ubiDat = new UbicacionDato();
            ipImp  = new IpComunicacion(); //de aca sale la ip de impresora y el puerto
            //---------------------------------------------------------------------------------------
            ubicacionGlobal = ubiDat.obtenerDato();
            ipImpresora = ipImp.obtenerIpImpresora();
            puertoImpresora = ipImp.obtenerPuertoImpresora();

            verificarEstado();

            tvIPImpresora.setText(ipImpresora);
            tvPuertoImpresora.setText(puertoImpresora);


            btnMenuPrincipal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        finish();
                    }catch (Exception e){
                        mensaje("1.- "+e,"Excepción","Aceptar");
                    }
                }
            });


            btnImprimir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        //LO QUE DEBE HACER EL BOTON IMPRIMIR
                        if(!edUbicacion.getText().toString().equalsIgnoreCase("")){
                            parametro1 = edUbicacion.getText().toString(); //ubicacion

                            //la ip sacarla de un archivo de texto.... junto al puerto para que quede guardada configuracion....
                            // IP = edIP.getText().toString();
                            // edPortNum = Integer.parseInt(edPort.getText().toString());
                            imprimirEtiqueta imp = new imprimirEtiqueta();
                            imp.execute(parametro1);
                        }else{
                            Toast.makeText(ImprimirUbicacion.this, "Debe Ingresar una ubicacion", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        mensaje("3.- "+e,"Excepción","Aceptar");
                    }
                }
            });
        }catch (Exception e){
            mensaje("4.- "+e,"Excepción","Aceptar");
        }
    }
/*
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        try{

//        ZEBRA TC25BJ     - codigos de botones para capturar 286, 285 (285 boton mango)

//        NEWLAND MT65-U       -codigos de botones para capturar 240, 241

            String modeloDispositivo = Build.MODEL;
            switch (modeloDispositivo){
                case "TC25":
                    if ((keyCode == 286) || (keyCode == 285)){
                        ejecutaLecturaSegunModelo();
                    }else{
                        return false;
                    }
                    break;

                case "MT65":
                    if ((keyCode == 240) || (keyCode == 241)){
                        ejecutaLecturaSegunModelo();
                    }else{
                        return false;
                    }
                    break;

                case "MT65-U":
                    if ((keyCode == 240) || (keyCode == 241)){
                        ejecutaLecturaSegunModelo();
                    }else{
                        return false;
                    }
                    break;
                case "MT65-V":
                    if ((keyCode == 240) || (keyCode == 241)){
                        ejecutaLecturaSegunModelo();
                    }else{
                        return false;
                    }
                    break;
                default:
                    Toast.makeText(this, "Modelo no reconocido para Abicap, contactese con personal Abitek", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            mensaje("7.- "+e,"Excepción","Aceptar");
        }
        return false;
    }

    private void ejecutaLecturaSegunModelo() {
        try{
            if(!edUbicacion.getText().toString().equalsIgnoreCase("")) {
//            b.activar(this);

                tvDescripcionProductoRevision.setText(descipcion);
                String cantidadProd = traerCantidad(codigoLeido,ubicacionFinal);
                Float cantidadDecimal = Float.parseFloat(cantidadProd);
                DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
                separadoresPersonalizados.setDecimalSeparator('.');
                DecimalFormat formatoDecimales = new DecimalFormat("#.###", separadoresPersonalizados);
                tvCantidadRevision.setText(formatoDecimales.format(cantidadDecimal));
                //tvCantidadRevision.setText(cantidadProd);
                tvCodigoLeidoBarridoRevision.setText(codigoLeido);
                edCodigoLeidoRevision.setText("");
            }
        }catch (Exception e){
            mensaje("8.- "+e,"Excepción","Aceptar");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            super.onActivityResult(requestCode, resultCode, data);
            // Comprobamos si el resultado de la segunda actividad es "RESULT_CANCELED".
            if (resultCode == RESULT_OK) {
                ubicacionGlobal = ubiDat.obtenerDato();
                tvUbicacionRevision.setText(ubicacionGlobal);

                Toast.makeText(this, "Operacion Exitosa", Toast.LENGTH_SHORT)
                        .show();
            }
        }catch (Exception e){
            mensaje("9.- "+e,"Excepción","Aceptar");
        }
    }
    */

    public void mensaje(String contenido, String titulo, String botonNom){
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(ImprimirUbicacion.this);
            builder.setMessage(contenido)
                    .setTitle(titulo)
                    .setCancelable(false)
                    .setNeutralButton(botonNom,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }catch (Exception e){
            mensaje("10.- "+e,"Excepción","Aceptar");
        }
    }

    public void verificarEstado() {
        String estado = Environment.getExternalStorageState();
        if (estado.equals(Environment.MEDIA_MOUNTED)) {
            sdDesponible = true;
            sdAccesoEscritura = true;
        } else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            sdDesponible = true;
            sdAccesoEscritura = false;
        } else {
            sdDesponible = false;
            sdAccesoEscritura = false;
        }
    }

    private class imprimirEtiqueta extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            if (sdAccesoEscritura && sdDesponible) {
                File ruta_sd = Environment.getExternalStoragePublicDirectory("/AbitekConfi/");//Environment.DIRECTORY_DOWNLOADS);
                File f = new File(ruta_sd.getAbsolutePath(), "etiquetaUbicacion.txt");
                if (f.exists()) {
                    try {
                        BufferedReader bufLec = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                        String tex;
                        while (!(tex = (bufLec.readLine()).trim()).equals(null)) {
                            tex = tex.replace("@parametro1", parametro1);

                            Log.e("leyendo archivo", tex);
                            textoEnviar = textoEnviar + tex + "\n";
                        }
                        bufLec.close();
                        //imprimir()
                    } catch (Exception ex) {
                        Log.e("Fichero sd", "Error al escribir en la sd");
                    }

                } else {
                    mensaje("Compruebe la existencia de la etiqueta","Advertencia","Aceptar");
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            p.dismiss();
            imprimir i = new imprimir();
            i.execute();
        }

        @Override
        protected void onPreExecute() {
            p = ProgressDialog.show(ImprimirUbicacion.this, "PROCESANDO SOLICITUD", "PROCESANDO", false);
        }

    }

    private class imprimir extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                // InetAddress serverAddr = InetAddress.getByName(IP)
                //Socket socket = new Socket(IP, edPortNum);
                socket = new Socket(ipImpresora, Integer.parseInt(puertoImpresora));
                PrintWriter out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())),
                        true);
                out.println(textoEnviar);
                textoEnviar = "";
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            p.dismiss();
            if (socket != null){
                try {
                    Log.i(TAG, "socket closed");
                    socket.close();
                } catch (IOException e) {
                    Log.e(TAG, "at thread finally IO " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPreExecute() {
            p = ProgressDialog.show(ImprimirUbicacion.this, "PROCESANDO IMPRIMIR", "IMPRIMIENDO", false);
        }

    }

}
