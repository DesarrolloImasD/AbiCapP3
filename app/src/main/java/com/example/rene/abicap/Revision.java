package com.example.rene.abicap;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camilodesarrollo.abicap.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Revision extends AppCompatActivity {
    private String ubicacionGlobal,codigoBarridoObtenido;
    private TextView tvUbicacionRevision,tvCantidadRevision,tvDescripcionProductoRevision,tvCodigoLeidoBarridoRevision,tvCodigoCapturado;
    private EditText edCodigoLeidoRevision;
    private Button btnIngresarCodigoRevision,btnMenuPrincipalRevision,btnCambiarUbiRevision,btnTotalUbicacionRevision;
    public Beeper b;
    public verificarMiscelaneos verMis;
    public verificarCheck verifChk;
    public UbicacionDato ubiDat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_revision);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN );

            //DECLARAR INTERFAZ GRAFICA
            //-----------------------------------------------------------------------------------------------
            tvUbicacionRevision = (TextView)findViewById(R.id.tvUbicacionRevision);
            tvCantidadRevision = (TextView)findViewById(R.id.tvCantidadRevision);
            tvDescripcionProductoRevision =(TextView)findViewById(R.id.tvDescripcionProductoRevision);
            tvCodigoLeidoBarridoRevision = (TextView)findViewById(R.id.tvCodigoLeidoBarridoRevision);

            edCodigoLeidoRevision = (EditText)findViewById(R.id.edCodigo);

            btnIngresarCodigoRevision=(Button)findViewById(R.id.btnIngresarCodigoRevision);
            btnMenuPrincipalRevision=(Button)findViewById(R.id.btnMenuPrincipal);
            btnCambiarUbiRevision=(Button)findViewById(R.id.btnCambiarUbiAuditoria);
            btnTotalUbicacionRevision=(Button)findViewById(R.id.btnTotalUbicacionBarrido);

            //-----------------------------------------------------------------------------------------------
            //DECLARO VARIABLES GLOBALES
            //---------------------------------------------------------------------------------------
            b = new Beeper();
            verMis = new verificarMiscelaneos();
            verifChk = new verificarCheck();
            ubiDat = new UbicacionDato();
            //---------------------------------------------------------------------------------------
            edCodigoLeidoRevision.requestFocus();
            ubicacionGlobal = ubiDat.obtenerDato();
            tvUbicacionRevision.setText(ubicacionGlobal);

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
                        Intent i = new Intent(Revision.this ,Ubicacion.class);
                        startActivityForResult(i,1);
                    }catch (Exception e){
                        mensaje("2.- "+e,"Excepción","Aceptar");
                    }

                }
            });

            btnTotalUbicacionRevision.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        //LO QUE DEBE HACER EL BOTON MOSTRAR CANTIDAD UBICACION
                        ubicacionGlobal = ubiDat.obtenerDato();
                        Intent i = new Intent(Revision.this ,TotalUbicacion.class);
                        i.putExtra("codigoUbicacion",ubicacionGlobal);
                        startActivity(i);
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
            SqlHelper sqlHel = new SqlHelper(Revision.this);
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
            SqlHelper sqlHel = new SqlHelper(Revision.this);
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
                case "NLS-MT65":
                    if ((keyCode == 242) || (keyCode == 243 || keyCode == 240)){
                        ejecutaLecturaSegunModelo();
                    }else{
                        return false;
                    }
                    break;
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
            AlertDialog.Builder builder = new AlertDialog.Builder(Revision.this);
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
}
