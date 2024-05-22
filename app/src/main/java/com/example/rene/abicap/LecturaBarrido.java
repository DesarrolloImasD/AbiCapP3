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

public class LecturaBarrido extends AppCompatActivity implements KeyEvent.Callback {

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
                    try{
                        ejecutaLecturaSegunModelo();
                    }catch(Exception e){
                        mensaje("0.- "+e, "Excepción", "Aceptar");
                    }
                }
            });

            btnMenuPrincipalBarrido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        finish();
                    }catch (Exception e){
                        mensaje("1.- "+e, "Excepción", "Aceptar");
                    }
                }
            });

            btnCambiarUbiBarrido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        Intent i = new Intent(LecturaBarrido.this ,Ubicacion.class);
                        startActivityForResult(i,1);
                    }catch (Exception e){
                        mensaje("2.- "+e, "Excepción", "Aceptar");
                    }
                }
            });

            btnTotalUbicacionBarrido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        //LO QUE DEBE HACER EL BOTON MOSTRAR CANTIDAD UBICACION
                        ubicacionGlobal = ubiDat.obtenerDato();
                        Intent i = new Intent(LecturaBarrido.this ,TotalUbicacion.class);
                        i.putExtra("codigoUbicacion",ubicacionGlobal);
                        startActivity(i);
                    }catch (Exception e){
                        mensaje("3.- "+e, "Excepción", "Aceptar");
                    }
                }
            });
        }catch (Exception e){
            mensaje("4.- "+e, "Excepción", "Aceptar");
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event){

        try{
            /*
            ZEBRA TC25BJ     - codigos de botones para capturar 286, 285 (285 boton mango)

            NEWLAND MT65-U     - codigos de botones para capturar 240, 241
            NEWLAND NLS-MT65     - codigos de botones para capturar 242 (lateral), 243 (pistola), 240 (botón central)
            */

            String modeloDispositivo = Build.MODEL;
            int keyCode = event.getKeyCode();
            Log.d("MODEELA", "onKeyUp: " + modeloDispositivo);
            Log.d("código", "" + keyCode);
            Log.d("Evento", "" + event);
            Log.d("Evento", "caja:" + edCodigoLeidoBarrido.getText().toString());
            switch (modeloDispositivo){
                case "NLS-MT65":
                    if ((keyCode == 242) || (keyCode == 243 || keyCode == 240)){
                        ejecutaLecturaSegunModelo();
                        edCodigoLeidoBarrido.setText("");

                    }else{
                        edCodigoLeidoBarrido.setText("");

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
            mensaje("5.- "+e, "Excepción", "Aceptar");
            edCodigoLeidoBarrido.setText("");
        }
        return false;

    }

/*
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        try{

        ZEBRA TC25BJ     - codigos de botones para capturar 286, 285 (285 boton mango)

        NEWLAND MT65-U     - codigos de botones para capturar 240, 241
        NEWLAND NLS-MT65     - codigos de botones para capturar 242 (lateral), 243 (pistola), 240 (botón central)


            String modeloDispositivo = Build.MODEL;
            Log.d("MODEELA", "onKeyUp: " + modeloDispositivo);
            Log.d("código", "" + keyCode);
            Log.d("Evento", "" + event);
            Log.d("Evento", "caja:" + edCodigoLeidoBarrido.getText().toString());
            switch (modeloDispositivo){
                case "NLS-MT65":
                    if ((keyCode == 242) || (keyCode == 243)){
                        ejecutaLecturaSegunModelo();
                        edCodigoLeidoBarrido.setText("");

                    }else{
                        edCodigoLeidoBarrido.setText("");

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
            mensaje("5.- "+e, "Excepción", "Aceptar");
            edCodigoLeidoBarrido.setText("");
        }
        return false;
    } */
    private void ejecutaLecturaSegunModelo() {
        try {
            String codigoObt = edCodigoLeidoBarrido.getText().toString().trim();
            edCodigoLeidoBarrido.setText("");
            String ubica = ubiDat.obtenerDato();
            if (!codigoObt.equalsIgnoreCase("")) {
                if (dupli) {
                    Boolean esDuplicado = verMis.existeRegistro(codigoObt, ubica, LecturaBarrido.this);
                    if (esDuplicado) {
//                        b.activarIncorrecto(LecturaBarrido.this);
                        mensaje("El codigo ya fue pistoleado", "Advertencia", "Aceptar");
                    } else {
                        if (contraArch) {
                            Boolean respu = verMis.existeEnMaestro(codigoObt, LecturaBarrido.this);
                            if (respu) {
                                metodoAccion(codigoObt, ubica);
                            } else {
//                                b.activarIncorrecto(LecturaBarrido.this);
                                mensaje("No existe Codigo en Archivo", "Advertencia", "Aceptar");
                            }
                        } else {
                            metodoAccion(codigoObt, ubica);
                        }
                    }
                } else {
                    if (contraArch) {
                        Boolean respu = verMis.existeEnMaestro(codigoObt, LecturaBarrido.this);
                        if (respu) {
                            metodoAccion(codigoObt, ubica);
                        } else {
//                            b.activarIncorrecto(LecturaBarrido.this);
                            mensaje("No existe Codigo en Archivo", "Advertencia", "Aceptar");
                        }
                    } else {
                        metodoAccion(codigoObt, ubica);
                        edCodigoLeidoBarrido.setText("");
                    }
                }
            }
            edCodigoLeidoBarrido.setText("");
        }catch (Exception e){
            mensaje("6.- "+e, "Excepción", "Aceptar");
        }
    }

    public String buscarCodigoBarrido(String codi){
//        String descrip="SIN RESULTADOS";
        String descrip="SIN DESCRIPCION";
        try{
            SqlHelper sqlHel = new SqlHelper(LecturaBarrido.this);
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
            mensaje("7.- "+e, "Excepción", "Aceptar");
        }
        return descrip;
    }

    public void insertarBarridoEnBase(String codi,String ubic, String desc){

        try{
            SqlHelper sqlHel = new SqlHelper(LecturaBarrido.this);
            SQLiteDatabase db = sqlHel.getReadableDatabase();

            String sql = "SELECT * FROM MAEDATOS WHERE CODIGO = '"+codi+"' AND UBICACION = '"+ubic+"'";
            Cursor c = db.rawQuery(sql,null);
            if (c.moveToNext()){
                String cant = c.getString(c.getColumnIndex("cantidad"));
                Float numFinal = Float.parseFloat(cant);
                actualizarCantidad(numFinal,codi,ubic);
            }else {
                insertarRegistroBarrido(codi,ubic, desc);
            }
            c.close();
            db.close();
        }catch (Exception e) {
            Log.e("Fichero sd", "Error");
            mensaje("8.- "+e, "Excepción", "Aceptar");
        }
    }

    public void actualizarCantidad(Float cantidad,String codig, String ubic) {
        try {
            SqlHelper sqlHel = new SqlHelper(LecturaBarrido.this);
            SQLiteDatabase db = sqlHel.getWritableDatabase();
            String resultado = String.valueOf(cantidad + 1);
            if (db != null) {
                String SQL = "update MAEDATOS SET CANTIDAD = '"+resultado+"', DESCRIPCION = '"+tvDescripcionProductoBarrido.getText().toString().trim()+"' WHERE CODIGO = '"+codig+"' AND UBICACION = '"+ubic+"'";
                db.execSQL(SQL);
                db.close();
            }
            DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
            separadoresPersonalizados.setDecimalSeparator('.');
            DecimalFormat formatoDecimales = new DecimalFormat("#.###", separadoresPersonalizados);

            tvCantidadBarrido.setText(formatoDecimales.format(Float.parseFloat(resultado)));
            tvCodigoLeidoBarridoMostrar.setText(codig);
        }catch(Exception e){
            Log.e("Error bd","error");
            mensaje("9.- "+e, "Excepción", "Aceptar");
        }
    }

    public void insertarRegistroBarrido(String codig, String ubic, String desc) {
        try {
            SqlHelper sqlHel = new SqlHelper(LecturaBarrido.this);
            SQLiteDatabase db = sqlHel.getWritableDatabase();
            if (db != null) {
                String SQL = "INSERT INTO MAEDATOS (UBICACION,CODIGO, DESCRIPCION, CANTIDAD) VALUES ('"+ubic+"','"+codig+"','"+desc+"','1')";
                db.execSQL(SQL);
                db.close();
            }
            DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
            separadoresPersonalizados.setDecimalSeparator('.');
            DecimalFormat formatoDecimales = new DecimalFormat("#.###", separadoresPersonalizados);

            tvCantidadBarrido.setText("1");
            tvCodigoLeidoBarridoMostrar.setText(codig);
        }catch(Exception e){
            Log.e("Error bd","error");
            mensaje("10.- "+e, "Excepción", "Aceptar");
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
                Toast.makeText(this, "Operacion Exitosa", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            mensaje("11.- "+e, "Excepción", "Aceptar");
        }
    }

    public void metodoAccion(String codAc,String ubiAc){
        try {
//            b.activar(LecturaBarrido.this);
            String descipcion = "NO EXISTE";
            descipcion = buscarCodigoBarrido(codAc);
            tvDescripcionProductoBarrido.setText(descipcion);
            insertarBarridoEnBase(codAc, ubiAc, descipcion);
        }catch (Exception e){
            mensaje("12.- "+e, "Excepción", "Aceptar");
        }
    }

    public void mensaje(String contenido, String titulo, String botonNom){
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(LecturaBarrido.this);
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
            mensaje("13.- "+e, "Excepción", "Aceptar");
        }
    }

}