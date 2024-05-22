package com.example.rene.abicap;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class LecturaLoteo extends AppCompatActivity {

    private String ubicacionGlobal,codigoBarridoObtenido;
    private TextView tvUbicacionLoteo,tvCantidadLote,tvDescripcionProductoLoteo,tvTotalCantidad;
    private EditText edCodigoLeidoLoteo,edCantidadLoteo;
    private Button btnIngresarCodigoLoteo,btnMenuPrincipalLoteo,btnCambiarUbiLoteo,btnTotalUbicacionLoteo,btnAgregarRegistro;
    public Boolean mostrarDes,contraArch,dupli;
    public Beeper b;
    public verificarMiscelaneos verMis;
    public verificarCheck verifChk;
    public UbicacionDato ubiDat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_lectura_loteo);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN );

            //DECLARAR INTERFAZ GRAFICA
            //-----------------------------------------------------------------------------------------------
            tvUbicacionLoteo = (TextView)findViewById(R.id.tvUbicacionLoteo);
            tvCantidadLote = (TextView)findViewById(R.id.tvCodigoLeidoBarridoRevision);
            tvDescripcionProductoLoteo =(TextView)findViewById(R.id.tvDescripcionProductoRevision);
            tvTotalCantidad = (TextView)findViewById(R.id.tvTotalCantidad);

            edCodigoLeidoLoteo = (EditText)findViewById(R.id.edCodigo);
            edCantidadLoteo = (EditText)findViewById(R.id.edCantidadLoteo);

            btnIngresarCodigoLoteo=(Button)findViewById(R.id.btnIngresarCodigoRevision);
            btnMenuPrincipalLoteo=(Button)findViewById(R.id.btnMenuPrincipal);
            btnCambiarUbiLoteo=(Button)findViewById(R.id.btnCambiarUbiAuditoria);
            btnTotalUbicacionLoteo=(Button)findViewById(R.id.btnTotalUbicacionBarrido);
            btnAgregarRegistro = (Button)findViewById(R.id.btnAgregarRegistro);
            //-----------------------------------------------------------------------------------------------
            //DECLARO VARIABLES GLOBALES
            //---------------------------------------------------------------------------------------
            b = new Beeper();
            verMis = new verificarMiscelaneos();
            verifChk = new verificarCheck();
            ubiDat = new UbicacionDato();
            //---------------------------------------------------------------------------------------
            //VERIFICO MISCELANEOS
            //------------------------------------------------------------------------------------------
            mostrarDes = verifChk.checkearDescripcion(this);

            if(!mostrarDes){
                tvDescripcionProductoLoteo.setVisibility(View.GONE);
            }
            contraArch = verifChk.checkearContraArchivo(this);

            dupli = verifChk.checkearDuplicado(this);

            //------------------------------------------------------------------------------------------

            edCodigoLeidoLoteo.requestFocus();

            ubicacionGlobal = ubiDat.obtenerDato();
            tvUbicacionLoteo.setText(ubicacionGlobal);

            btnCambiarUbiLoteo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        Intent i = new Intent(LecturaLoteo.this ,Ubicacion.class);
                        startActivityForResult(i,1);
                    }catch (Exception e){
                        mensaje("0.- "+e,"Excepción","Aceptar");
                    }
                }
            });

            btnIngresarCodigoLoteo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{

                        String codigoObt = edCodigoLeidoLoteo.getText().toString().trim();
                        String ubica = ubiDat.obtenerDato();
                        if(!codigoObt.equalsIgnoreCase("")) {
                            if(dupli){
                                Boolean esDuplicado = verMis.existeRegistro(codigoObt,ubica,LecturaLoteo.this);
                                if(esDuplicado){
                                    //                           b.activarIncorrecto(LecturaLoteo.this);
                                    mensaje("El codigo ya fue pistoleado","Advertencia","Aceptar");
                                    edCodigoLeidoLoteo.setText("");
                                }else{
                                    if(contraArch){
                                        Boolean respu = verMis.existeEnMaestro(codigoObt,LecturaLoteo.this);
                                        if(respu){
                                            metodoAccion(codigoObt,ubica);
                                        }else{
                                            //                                   b.activarIncorrecto(LecturaLoteo.this);
                                            mensaje("No existe Codigo en Archivo","Advertencia","Aceptar");
                                            edCodigoLeidoLoteo.setText("");
                                        }
                                    }else {
                                        metodoAccion(codigoObt,ubica);
                                    }
                                }
                            }else{
                                if(contraArch){
                                    Boolean respu = verMis.existeEnMaestro(codigoObt,LecturaLoteo.this);
                                    if(respu){
                                        metodoAccion(codigoObt,ubica);
                                    }else{
                                        //                               b.activarIncorrecto(LecturaLoteo.this);
                                        mensaje("No existe Codigo en Archivo","Advertencia","Aceptar");
                                        edCodigoLeidoLoteo.setText("");
                                    }
                                }else {
                                    metodoAccion(codigoObt,ubica);
                                }
                            }
                        }
                    }catch (Exception e){
                        mensaje("1.- "+e,"Excepción","Aceptar");
                    }
                }
            });

            btnAgregarRegistro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        Float totalInsertar = 0.0f;
                        if (edCantidadLoteo.getText().toString().equalsIgnoreCase("")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LecturaLoteo.this);
                            mensaje("FAVOR INGRESAR UNA CIFRA PARA AUMENTAR","Atención","Aceptar");
                        } else {
                            if (edCodigoLeidoLoteo.getText().toString().equalsIgnoreCase("")) {
                                mensaje("FAVOR INGRESAR UN CODIGO POR FAVOR","Atención","Aceptar");
                            } else {

                                if (Float.parseFloat(tvTotalCantidad.getText().toString().trim()) <= 0) {
                                    mensaje("FAVOR INGRESAR UN VALOR MAYOR A 0","Atención","Aceptar");
                                } else {

                                    if(tvDescripcionProductoLoteo.getText().equals("xx")){
                                        mensaje("PRIMERO DEBE INGRESAR EL CODIGO POR FAVOR","Atención","Aceptar");
                                    }else {
                                        totalInsertar = Float.parseFloat(tvTotalCantidad.getText().toString().trim());
//modificado 27/02/2018 - se agrego columna DESCRIPCION
                                        insertarLoteoEnBase(edCodigoLeidoLoteo.getText().toString().trim(), ubiDat.obtenerDato(), tvDescripcionProductoLoteo.getText().toString().trim(), tvTotalCantidad.getText().toString().trim());
                                        Toast toast = Toast.makeText(getApplicationContext(), "OPERACION EXITOSA", Toast.LENGTH_SHORT);
                                        toast.show();
                                        limpiarGrafica();
                                    }
                                }
                            }
                        }
                    }catch (Exception e){
                        mensaje("2.- "+e,"Excepción","Aceptar");
                    }
                }
            });

            btnTotalUbicacionLoteo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        ubicacionGlobal = ubiDat.obtenerDato();
                        Intent i = new Intent(LecturaLoteo.this ,TotalUbicacion.class);
                        i.putExtra("codigoUbicacion",ubicacionGlobal);
                        startActivity(i);
                    }catch (Exception e){
                        mensaje("3.- "+e,"Excepción","Aceptar");
                    }
                }
            });

            btnMenuPrincipalLoteo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        finish();
                    }catch (Exception e){
                        mensaje("4.- "+e,"Excepción","Aceptar");
                    }
                }
            });

            edCantidadLoteo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    try{
                        Float cantidadInicial = 0.0f;
                        Float cantidadNueva = 0.0f;
                        Float total = 0.0f;

                        cantidadInicial = Float.parseFloat(tvCantidadLote.getText().toString().trim());
                        if(edCantidadLoteo.getText().toString().trim().equalsIgnoreCase("")){
                            cantidadNueva = 0.0f;
                        }else{
                            if(edCantidadLoteo.getText().toString().trim().equals(".")){
                                mensaje("Debe ingresar un dígito antes de ingresar el punto decimal.","ATENCIÓN","Aceptar");
                                edCantidadLoteo.setText("");
                            }else{
                                String cant= edCantidadLoteo.getText().toString().trim();
                                String cod= edCodigoLeidoLoteo.getText().toString().trim();
                                if (!cant.equals(cod)){
                                    cantidadNueva = Float.parseFloat(cant);
                                }else{
                                    edCantidadLoteo.setText("");
                                }
                            }
                        }
                        total = cantidadInicial + cantidadNueva;
                        DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
                        separadoresPersonalizados.setDecimalSeparator('.');
                        DecimalFormat formatoDecimales = new DecimalFormat("#.###", separadoresPersonalizados);
                        tvTotalCantidad.setText(formatoDecimales.format(total));
                    }catch (Exception e){
                        mensaje("5.- "+e,"Excepción","Aceptar");
                    }
                }
            });

        }catch (Exception e){
            mensaje("6.- "+e,"Excepción","Aceptar");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            super.onActivityResult(requestCode, resultCode, data);
            // Comprobamos si el resultado de la segunda actividad es "RESULT_CANCELED".
            if (resultCode == RESULT_OK) {
                ubicacionGlobal = ubiDat.obtenerDato();
                tvUbicacionLoteo.setText(ubicacionGlobal);
                edCodigoLeidoLoteo.setText("");
                edCantidadLoteo.setText("");
                Toast.makeText(this, "Operacion Exitosa", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            mensaje("7.- "+e,"Excepción","Aceptar");
        }
    }

    public String buscarCodigoLoteo(String codi){

        String descrip="SIN DESCRIPCION";
        try{
            SqlHelper sqlHel = new SqlHelper(LecturaLoteo.this);
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
            mensaje("8.- "+e,"Excepción","Aceptar");
        }
        return descrip;
    }

    public void insertarLoteoEnBase(String codi,String ubic, String desc,String cantidad){

        try{
            SqlHelper sqlHel = new SqlHelper(LecturaLoteo.this);
            SQLiteDatabase db = sqlHel.getReadableDatabase();

            String sql = "SELECT * FROM MAEDATOS WHERE CODIGO = '"+codi+"' AND UBICACION = '"+ubic+"'";
            Cursor c = db.rawQuery(sql,null);
            if (c.moveToNext()){
                actualizarCantidad(codi,ubic,cantidad);
            }else {
//modificado 27/02/2018 - se agrego columna DESCRIPCION
                insertarRegistroLote(codi,ubic,desc,cantidad);
            }
            c.close();
            db.close();
        }catch (Exception e) {
            Log.e("Fichero sd", "Error");
            mensaje("9.- "+e,"Excepción","Aceptar");
        }
    }

    public void actualizarCantidad(String codig, String ubic,String totalCantidad) {
        try {
            SqlHelper sqlHel = new SqlHelper(LecturaLoteo.this);
            SQLiteDatabase db = sqlHel.getWritableDatabase();
            if (db != null) {
                String SQL = "update MAEDATOS SET CANTIDAD = '"+totalCantidad+"' WHERE CODIGO = '"+codig+"' AND UBICACION = '"+ubic+"'";
                db.execSQL(SQL);
                db.close();
            }
        }catch(Exception e){
            Log.e("Error bd","error");
            mensaje("10.- "+e,"Excepción","Aceptar");
        }
    }


    public void insertarRegistroLote(String codig, String ubic,String desc ,String cantidad) {
        try {
            SqlHelper sqlHel = new SqlHelper(LecturaLoteo.this);
            SQLiteDatabase db = sqlHel.getWritableDatabase();
            if (db != null) {
//modificado 27/02/2018 - se agrego columna DESCRIPCION
                String SQL = "INSERT INTO MAEDATOS (UBICACION,CODIGO, DESCRIPCION, CANTIDAD) VALUES ('"+ubic+"','"+codig+"','"+desc+"','"+cantidad+"')";
                db.execSQL(SQL);
                db.close();
            }
        }catch(Exception e){
            Log.e("Error bd","error");
            mensaje("11.- "+e,"Excepción","Aceptar");
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
            mensaje("12.- "+e,"Excepción","Aceptar");
        }
        return false;
    }

    private void ejecutaLecturaSegunModelo() {
        try{
            String codigoObt = edCodigoLeidoLoteo.getText().toString().trim();
            String ubica = ubiDat.obtenerDato();
            if(!codigoObt.equalsIgnoreCase("")) {
                if(dupli){
                    Boolean esDuplicado = verMis.existeRegistro(codigoObt,ubica,LecturaLoteo.this);
                    if(esDuplicado){
//                    b.activarIncorrecto(LecturaLoteo.this);
                        mensaje("El codigo ya fue pistoleado","Advertencia","Aceptar");
                        edCodigoLeidoLoteo.setText("");
                    }else{
                        if(contraArch){
                            Boolean respu = verMis.existeEnMaestro(codigoObt,LecturaLoteo.this);
                            if(respu){
                                metodoAccion(codigoObt,ubica);
                            }else{
                                //                           b.activarIncorrecto(LecturaLoteo.this);
                                mensaje("No existe Codigo en Archivo","Advertencia","Aceptar");
                                edCodigoLeidoLoteo.setText("");
                            }
                        }else {
                            metodoAccion(codigoObt,ubica);
                        }
                    }
                }else{
                    if(contraArch){
                        Boolean respu = verMis.existeEnMaestro(codigoObt,LecturaLoteo.this);
                        if(respu){
                            metodoAccion(codigoObt,ubica);
                        }else{
                            //                       b.activarIncorrecto(LecturaLoteo.this);
                            mensaje("No existe Codigo en Archivo","Advertencia","Aceptar");
                            edCodigoLeidoLoteo.setText("");
                        }
                    }else {
                        metodoAccion(codigoObt,ubica);
                    }
                }
            }
        }catch (Exception e){
            mensaje("13.- "+e,"Excepción","Aceptar");
        }
    }

    public void limpiarGrafica(){
        try{
            edCodigoLeidoLoteo.setText("");
            tvCantidadLote.setText("0.0");
            tvTotalCantidad.setText("0.0");
            tvDescripcionProductoLoteo.setText("xx");
            edCodigoLeidoLoteo.requestFocus();
            edCantidadLoteo.setText("");
        }catch (Exception e){
            mensaje("14.- "+e,"Excepción","Aceptar");
        }
    }

    public void metodoAccion(String codAc,String ubiAc){
        try{
            //        b.activar(this);
            BuscarCantidad busc = new BuscarCantidad();
            String cantidad = busc.obtenerCantidadProducto(LecturaLoteo.this,codAc,ubiAc);
            tvCantidadLote.setText(cantidad);

            String descipcion = "NO EXISTE";
            descipcion = buscarCodigoLoteo(codAc);
            tvDescripcionProductoLoteo.setText(descipcion);
            edCantidadLoteo.requestFocus();
        }catch (Exception e){
            mensaje("15.- "+e,"Excepción","Aceptar");
        }
    }

    public void mensaje(String contenido, String titulo, String botonNom){
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(LecturaLoteo.this);
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
            mensaje("16.- "+e,"Excepción","Aceptar");
        }
    }


}
