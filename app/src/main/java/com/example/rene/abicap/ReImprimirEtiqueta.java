package com.example.rene.abicap;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class ReImprimirEtiqueta extends AppCompatActivity {
    private String ubicacionGlobal,codigoBarridoObtenido, ipImpresora, puertoImpresora;
    private TextView tvUbicacionRevision,tvCantidadRevision,tvDescripcionProductoRevision,tvCodigoLeidoBarridoRevision,tvCodigoCapturado, tvIPImpresora, tvPuertoImpresora;
    private EditText edCodigoLeidoRevision, edCantidadRevision;
    private Button btnIngresarCodigoRevision,btnMenuPrincipalRevision,btnCambiarUbiRevision,btnImprimir;
    public Beeper b;
    public verificarMiscelaneos verMis;
    public verificarCheck verifChk;
    public UbicacionDato ubiDat;
    public IpComunicacion ipImp;
    private String parametro1,parametro2,parametro3, parametro4;
    private String IP;
    private ProgressDialog p;
    private boolean sdDesponible, sdAccesoEscritura;
    private String textoEnviar, txtvalue;
    private Socket socket;
    String TAG = "TGsocket";
    private Integer calcularParametro4,cantidadIngresada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_reimprimir_etiqueta);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN );

            //DECLARAR INTERFAZ GRAFICA
            //-----------------------------------------------------------------------------------------------
            tvUbicacionRevision = (TextView)findViewById(R.id.tvUbicacionRevision);
            tvCantidadRevision = (TextView)findViewById(R.id.tvCantidadRevision);
            tvDescripcionProductoRevision =(TextView)findViewById(R.id.tvDescripcionProductoRevision);
            tvCodigoLeidoBarridoRevision = (TextView)findViewById(R.id.tvCodigoLeidoBarridoRevision);
            tvIPImpresora = (TextView)findViewById(R.id.tvIPImpresora);
            tvPuertoImpresora = (TextView)findViewById(R.id.tvPuertoImpresora );

            edCodigoLeidoRevision = (EditText)findViewById(R.id.edCodigo);
            edCantidadRevision = (EditText)findViewById(R.id.edCantidadRevision);

            btnIngresarCodigoRevision=(Button)findViewById(R.id.btnIngresarCodigoRevision);
            btnMenuPrincipalRevision=(Button)findViewById(R.id.btnMenuPrincipal);
            btnCambiarUbiRevision=(Button)findViewById(R.id.btnCambiarUbiAuditoria);
            btnImprimir=(Button)findViewById(R.id.btnImprimir);

            //-----------------------------------------------------------------------------------------------
            //DECLARO VARIABLES GLOBALES
            //---------------------------------------------------------------------------------------
            b = new Beeper();
            verMis = new verificarMiscelaneos();
            verifChk = new verificarCheck();
            ubiDat = new UbicacionDato();
            ipImp  = new IpComunicacion(); //de aca sale la ip de impresora y el puerto
            //---------------------------------------------------------------------------------------
            edCodigoLeidoRevision.requestFocus();
            ubicacionGlobal = ubiDat.obtenerDato();

            verificarEstado();
            ipImpresora = ipImp.obtenerIpImpresora();
            puertoImpresora = ipImp.obtenerPuertoImpresora();

            tvUbicacionRevision.setText(ubicacionGlobal);
            tvIPImpresora.setText(ipImpresora);
            tvPuertoImpresora.setText(puertoImpresora);

            btnIngresarCodigoRevision.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        if(!edCodigoLeidoRevision.getText().toString().equalsIgnoreCase("")) {
//                    b.activar(Revision.this);
                            String descipcion = "NO EXISTE";
                            String codigoLeido = edCodigoLeidoRevision.getText().toString().trim();
                            String ubicacionFinal = ubiDat.obtenerDato();
                            descipcion = buscarCodigoRevision(codigoLeido);
                            tvDescripcionProductoRevision.setText(descipcion);
                            String cantidadProd = traerCantidad(codigoLeido,ubicacionFinal);
                            tvCantidadRevision.setText(cantidadProd);
                            tvCodigoLeidoBarridoRevision.setText(codigoLeido);
                            edCodigoLeidoRevision.setText("");
                        }
                    }catch (Exception e){
                        mensaje("0.- "+e,"Excepción","Aceptar");
                    }
                }
            });

            btnMenuPrincipalRevision.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        finish();
                    }catch (Exception e){
                        mensaje("1.- "+e,"Excepción","Aceptar");
                    }
                }
            });

            btnCambiarUbiRevision.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        Intent i = new Intent(ReImprimirEtiqueta.this ,Ubicacion.class);
                        startActivityForResult(i,1);
                    }catch (Exception e){
                        mensaje("2.- "+e,"Excepción","Aceptar");
                    }

                }
            });

            btnImprimir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        calcularParametro4=0;
                        cantidadIngresada=0;
                        //LO QUE DEBE HACER EL BOTON IMPRIMIR
                        parametro1 = tvCodigoLeidoBarridoRevision.getText().toString();//"hola"; //codigo
                        parametro2 = tvDescripcionProductoRevision.getText().toString(); //descripcion
                        parametro3 = tvUbicacionRevision.getText().toString(); //ubicacion
                        parametro4 = edCantidadRevision.getText().toString(); //cantidad
                        //cantidadIngresada = Integer.parseInt(edCantidadRevision.getText().toString());
                        //calcularParametro4 = ((cantidadIngresada/2)+(cantidadIngresada%2));
                        //parametro4 = calcularParametro4.toString(); //cantidad a imprimir


                        //la ip sacarla de un archivo de texto.... junto al puerto para que quede guardada configuracion....
            // IP = edIP.getText().toString();
            // edPortNum = Integer.parseInt(edPort.getText().toString());
                        imprimirEtiqueta imp = new imprimirEtiqueta();
                        imp.execute(parametro1, parametro2, parametro3, parametro4);
                    }catch (Exception e){
                        mensaje("3.- "+e,"Excepción","Aceptar");
                    }
                }
            });
        }catch (Exception e){
            mensaje("4.- "+e,"Excepción","Aceptar");
        }
    }


    public String buscarCodigoRevision(String codi){
        String descrip="SIN RESULTADOS";
        try{
            SqlHelper sqlHel = new SqlHelper(ReImprimirEtiqueta.this);
            SQLiteDatabase db = sqlHel.getReadableDatabase();

            String sql = "SELECT * FROM MAEPRODUCTOS WHERE CODIGO = '"+codi+"'";
            Cursor c = db.rawQuery(sql,null);
            while (c.moveToNext()){
                descrip = c.getString(c.getColumnIndex("descripcion"));
            }
            c.close();
            db.close();
        }catch (Exception e) {
            Log.e("Fichero sd", "Error");

            mensaje("5.- "+e,"Excepción","Aceptar");
        }
        return descrip;
    }

    public String traerCantidad(String codi,String ubic){
        String respuesta = "0";
        try{
            SqlHelper sqlHel = new SqlHelper(ReImprimirEtiqueta.this);
            SQLiteDatabase db = sqlHel.getReadableDatabase();

            String sql = "SELECT * FROM MAEDATOS WHERE CODIGO = '"+codi+"' AND UBICACION = '"+ubic+"'";
            Cursor c = db.rawQuery(sql,null);
            if (c.moveToNext()){
                respuesta = c.getString(c.getColumnIndex("cantidad"));
            }else {
                respuesta = "0";
            }
            c.close();
            db.close();
        }catch (Exception e) {
            Log.e("Fichero sd", "Error");
            mensaje("6.- "+e,"Excepción","Aceptar");
        }
        return respuesta;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        try{
/*
        ZEBRA TC25BJ     - codigos de botones para capturar 286, 285 (285 boton mango)

        NEWLAND MT65-U       -codigos de botones para capturar 240, 241
        */

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
            if(!edCodigoLeidoRevision.getText().toString().equalsIgnoreCase("")) {
//            b.activar(this);
                String descipcion = "NO EXISTE";
                String codigoLeido = edCodigoLeidoRevision.getText().toString().trim();
                String ubicacionFinal = ubiDat.obtenerDato();
                descipcion = buscarCodigoRevision(codigoLeido);
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
                edCodigoLeidoRevision.setText("");
                Toast.makeText(this, "Operacion Exitosa", Toast.LENGTH_SHORT)
                        .show();
            }
        }catch (Exception e){
            mensaje("9.- "+e,"Excepción","Aceptar");
        }
    }

    public void mensaje(String contenido, String titulo, String botonNom){
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(ReImprimirEtiqueta.this);
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
                File f = new File(ruta_sd.getAbsolutePath(), "arranque.txt");
                if (f.exists()) {
                    try {
                        BufferedReader bufLec = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                        String tex;
                        while (!(tex = (bufLec.readLine()).trim()).equals(null)) {
                            tex = tex.replace("@parametro1", parametro1);
                            tex = tex.replace("@parametro2", parametro2);
                            tex = tex.replace("@parametro3", parametro3);
                            tex = tex.replace("@parametro4", parametro4);
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
            p = ProgressDialog.show(ReImprimirEtiqueta.this, "PROCESANDO SOLICITUD", "PROCESANDO", false);
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
            p = ProgressDialog.show(ReImprimirEtiqueta.this, "PROCESANDO IMPRIMIR", "IMPRIMIENDO", false);
        }

    }

}
