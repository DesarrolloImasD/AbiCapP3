package com.example.rene.abicap;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class SalidaMercaderia extends AppCompatActivity {

    private String ubicacionGlobal,codigoBarridoObtenido;
    private TextView tvUbicacionBarrido,tvCantidadBarrido,tvDescripcionProductoBarrido,tvCodigoLeidoBarridoMostrar;
    private EditText edCodigoLeidoBarrido;
    private Button btnIngresarCodigoBarrido,btnMenuPrincipalBarrido,btnCambiarUbiBarrido,btnTotalUbicacionBarrido;
    public Boolean mostrarDes,contraArch,dupli;
    public Beeper b;
    public verificarMiscelaneos verMis;
    public verificarCheck verifChk;
    public UbicacionDato ubiDat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_lectura_barrido);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN );
            //DECLARAR INTERFAZ GRAFICA
            //-----------------------------------------------------------------------------------------------
            tvUbicacionBarrido = (TextView)findViewById(R.id.tvUbicacionBarrido);
            tvCantidadBarrido = (TextView)findViewById(R.id.tvCodigoLeidoBarridoRevision);
            tvDescripcionProductoBarrido =(TextView)findViewById(R.id.tvDescripcionProductoRevision);
            tvCodigoLeidoBarridoMostrar = (TextView)findViewById(R.id.tvCantidadLote);

            edCodigoLeidoBarrido = (EditText)findViewById(R.id.edCodigo);

            btnIngresarCodigoBarrido=(Button)findViewById(R.id.btnIngresarCodigoRevision);
            btnMenuPrincipalBarrido=(Button)findViewById(R.id.btnMenuPrincipal);
            btnCambiarUbiBarrido=(Button)findViewById(R.id.btnCambiarUbiAuditoria);
            btnTotalUbicacionBarrido=(Button)findViewById(R.id.btnTotalUbicacionBarrido);
            //-----------------------------------------------------------------------------------------------

            //DECLARO VARIABLES GLOBALES
            //---------------------------------------------------------------------------------------
            b = new Beeper();
            verMis = new verificarMiscelaneos();
            verifChk = new verificarCheck();
            ubiDat = new UbicacionDato();
            //---------------------------------------------------------------------------------------
            ubicacionGlobal = ubiDat.obtenerDato();
            tvUbicacionBarrido.setText(ubicacionGlobal);

            //VERIFICO MISCELANEOS
            //------------------------------------------------------------------------------------------
            mostrarDes = verifChk.checkearDescripcion(this);

            if(!mostrarDes){
                tvDescripcionProductoBarrido.setVisibility(View.GONE);
            }
            contraArch = verifChk.checkearContraArchivo(this);

            dupli = verifChk.checkearDuplicado(this);

            //------------------------------------------------------------------------------------------
            edCodigoLeidoBarrido.requestFocus();

            btnIngresarCodigoBarrido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{String codigoObt = edCodigoLeidoBarrido.getText().toString().trim();
                        String ubica = ubiDat.obtenerDato();
                        if(!codigoObt.equalsIgnoreCase("")) {
                            if(dupli){
                                Boolean esDuplicado = verMis.existeRegistro(codigoObt,ubica,SalidaMercaderia.this);
                                if(esDuplicado){
//                            b.activarIncorrecto(SalidaMercaderia.this);
                                    mensaje("El codigo ya fue pistoleado","Advertencia","Aceptar");
                                    edCodigoLeidoBarrido.setText("");
                                }else{
                                    if(contraArch){
                                        Boolean respu = verMis.existeEnMaestro(codigoObt,SalidaMercaderia.this);
                                        if(respu){
                                            metodoAccion(codigoObt,ubica);
                                        }else{
//                                    b.activarIncorrecto(SalidaMercaderia.this);
                                            mensaje("No existe Codigo en Archivo","Advertencia","Aceptar");
                                            edCodigoLeidoBarrido.setText("");
                                        }
                                    }else {
                                        metodoAccion(codigoObt,ubica);
                                    }
                                }
                            }else{
                                if(contraArch){
                                    Boolean respu = verMis.existeEnMaestro(codigoObt,SalidaMercaderia.this);
                                    if(respu){
                                        metodoAccion(codigoObt,ubica);
                                    }else{
//                                b.activarIncorrecto(SalidaMercaderia.this);
                                        mensaje("No existe Codigo en Archivo","Advertencia","Aceptar");
                                        edCodigoLeidoBarrido.setText("");
                                    }
                                }else {
                                    metodoAccion(codigoObt,ubica);
                                }
                            }

                        }
                    }catch (Exception e){
                        mensaje("0.- "+e,"Excepción","Aceptar");
                    }
                }
            });

            btnMenuPrincipalBarrido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        finish();
                    }catch (Exception e){
                        mensaje("1.- "+e,"Excepción","Aceptar");
                    }
                }
            });

            btnCambiarUbiBarrido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        Intent i = new Intent(SalidaMercaderia.this ,Ubicacion.class);
                        startActivityForResult(i,1);
                    }catch (Exception e){
                        mensaje("2.- "+e,"Excepción","Aceptar");
                    }
                }
            });

            btnTotalUbicacionBarrido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        //LO QUE DEBE HACER EL BOTON MOSTRAR CANTIDAD UBICACION
                        ubicacionGlobal = ubiDat.obtenerDato();
                        Intent i = new Intent(SalidaMercaderia.this ,TotalUbicacion.class);
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
            mensaje("5.- "+e,"Excepción","Aceptar");
        }
        return false;
    }

    private void ejecutaLecturaSegunModelo() {
        try{
            String codigoObt = edCodigoLeidoBarrido.getText().toString().trim();
            String ubica = ubiDat.obtenerDato();
            if(!codigoObt.equalsIgnoreCase("")) {
                if(dupli){
                    Boolean esDuplicado = verMis.existeRegistro(codigoObt,ubica,SalidaMercaderia.this);
                    if(esDuplicado){
//                    b.activarIncorrecto(SalidaMercaderia.this);
                        mensaje("El codigo ya fue pistoleado","Advertencia","Aceptar");
                    }else{
                        if(contraArch){
                            Boolean respu = verMis.existeEnMaestro(codigoObt,SalidaMercaderia.this);
                            if(respu){
                                metodoAccion(codigoObt,ubica);
                            }else{
//                            b.activarIncorrecto(SalidaMercaderia.this);
                                mensaje("No existe Codigo en Archivo","Advertencia","Aceptar");
                            }
                        }else {
                            metodoAccion(codigoObt,ubica);
                        }
                    }
                }else{
                    if(contraArch){
                        Boolean respu = verMis.existeEnMaestro(codigoObt,SalidaMercaderia.this);
                        if(respu){
                            metodoAccion(codigoObt,ubica);
                        }else{
//                        b.activarIncorrecto(SalidaMercaderia.this);
                            mensaje("No existe Codigo en Archivo","Advertencia","Aceptar");
                        }
                    }else {
                        metodoAccion(codigoObt,ubica);
                    }
                }
            }
            edCodigoLeidoBarrido.setText("");
        }catch (Exception e){
            mensaje("6.- "+e,"Excepción","Aceptar");
        }
    }

    public String buscarCodigoBarrido(String codi){
        String descrip="SIN DESCRIPCION";
        try{
            try{
                SqlHelper sqlHel = new SqlHelper(SalidaMercaderia.this);
                SQLiteDatabase db = sqlHel.getReadableDatabase();

                String sql = "SELECT * FROM MAEPRODUCTOS WHERE CODIGO = '"+codi+"'";
                Cursor c = db.rawQuery(sql,null);
                while (c.moveToNext()){
                    descrip = c.getString(c.getColumnIndex("descripcion"));
                }
                c.close();
                db.close();
            }catch (Exception ex) {
                Log.e("Fichero sd", "Error");
            }

        }catch (Exception e){
            mensaje("7.- "+e,"Excepción","Aceptar");
        }
        return descrip;
    }

    public void insertarBarridoEnBase(String codi,String ubic){
        try{
            SqlHelper sqlHel = new SqlHelper(SalidaMercaderia.this);
            SQLiteDatabase db = sqlHel.getReadableDatabase();

            String sql = "SELECT * FROM MAEDATOS WHERE CODIGO = '"+codi+"' AND UBICACION = '"+ubic+"'";
            Cursor c = db.rawQuery(sql,null);
            if (c.moveToNext()){
                String cant = c.getString(c.getColumnIndex("cantidad"));
                Float numFinal =  Float.parseFloat(cant);
                DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
                separadoresPersonalizados.setDecimalSeparator('.');
                DecimalFormat formatoDecimales = new DecimalFormat("#.###", separadoresPersonalizados);
                tvCantidadBarrido.setText(formatoDecimales.format(numFinal));
                actualizarCantidad(numFinal,codi,ubic);
            }else {
                insertarRegistroBarrido(codi,ubic);
            }
            c.close();
            db.close();
        }catch (Exception e){
            mensaje("8.- "+e,"Excepción","Aceptar");
        }
    }

    public void actualizarCantidad(Float cantidad,String codig, String ubic) {

        try {
            SqlHelper sqlHel = new SqlHelper(SalidaMercaderia.this);
            SQLiteDatabase db = sqlHel.getWritableDatabase();
            Float cantidadActualizada = (cantidad-1);

            DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
            separadoresPersonalizados.setDecimalSeparator('.');
            DecimalFormat formatoDecimales = new DecimalFormat("#.###", separadoresPersonalizados);
            //formatoDecimales.format(cantidadActualizada);

            //String resultado = String.valueOf(cantidadActualizada);
            String resultado = String.valueOf(formatoDecimales.format(cantidadActualizada));
            if ((db != null) && (cantidadActualizada>=0)) {
                String SQL = "update MAEDATOS SET CANTIDAD = '"+resultado+"' WHERE CODIGO = '"+codig+"' AND UBICACION = '"+ubic+"'";
                db.execSQL(SQL);
                db.close();
                tvCantidadBarrido.setText(resultado);
            }else{
                Toast.makeText(this, "No hay Stock Suficiente para realizar operacion!", Toast.LENGTH_SHORT).show();
                tvCantidadBarrido.setText(formatoDecimales.format(cantidad));
            }
            tvCodigoLeidoBarridoMostrar.setText(codig);
        }catch (Exception e){
            mensaje("9.- "+e,"Excepción","Aceptar");
        }
    }


    public void insertarRegistroBarrido(String codig, String ubic) {
        try {
            SqlHelper sqlHel = new SqlHelper(SalidaMercaderia.this);
            SQLiteDatabase db = sqlHel.getWritableDatabase();
            if (db != null) {
                String SQL = "INSERT INTO MAEDATOS (UBICACION,CODIGO,CANTIDAD) VALUES ('"+ubic+"','"+codig+"','0')";
                db.execSQL(SQL);
                db.close();
            }
            tvCantidadBarrido.setText("0");
            tvCodigoLeidoBarridoMostrar.setText(codig);
        }catch (Exception e){
            mensaje("10.- "+e,"Excepción","Aceptar");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            super.onActivityResult(requestCode, resultCode, data);
            // Comprobamos si el resultado de la segunda actividad es "RESULT_CANCELED".
            if (resultCode == RESULT_OK) {
                ubicacionGlobal = ubiDat.obtenerDato();
                tvUbicacionBarrido.setText(ubicacionGlobal);
                edCodigoLeidoBarrido.setText("");
                Toast.makeText(this, "Operacion Exitosa", Toast.LENGTH_SHORT)
                        .show();

            }
        }catch (Exception e){
            mensaje("11.- "+e,"Excepción","Aceptar");
        }
    }

    public void metodoAccion(String codAc,String ubiAc){
        try{
//            b.activar(SalidaMercaderia.this);
            String descipcion = "NO EXISTE";
            descipcion = buscarCodigoBarrido(codAc);
            tvDescripcionProductoBarrido.setText(descipcion);
            insertarBarridoEnBase(codAc, ubiAc);
        }catch (Exception e){
            mensaje("12.- "+e,"Excepción","Aceptar");
        }
    }

    public void mensaje(String contenido, String titulo, String botonNom){
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(SalidaMercaderia.this);
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
            mensaje("13.- "+e,"Excepción","Aceptar");
        }
    }

}
