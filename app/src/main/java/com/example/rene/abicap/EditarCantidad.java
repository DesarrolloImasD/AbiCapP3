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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camilodesarrollo.abicap.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class EditarCantidad extends AppCompatActivity {

    private TextView tvUbicacionEditar,tvCantidadEditar,tvCodigoLeidoEditar,tvDescripcionEditar;
    private Button btnCambiarUbiEditar,btnIngresarCodigoEditar,btnEditarCantidad,btnMenuPrincipalEditar,btnTotalUbicacionEditar;
    private EditText edCodigoCapturado,edCantidadEditar;
    private String ubicacionGlobal,codigoBarridoObtenido;
    public Beeper b;
    public verificarMiscelaneos verMis;
    public verificarCheck verifChk;
    public UbicacionDato ubiDat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_editar_cantidad);

            //INTERFAZ GRAFICA
            //------------------------------------------------------------------------------------------------
            tvUbicacionEditar = (TextView)findViewById(R.id.tvUbicacionEditar);
            tvCantidadEditar = (TextView)findViewById(R.id.tvCantidadEditar);
            tvCodigoLeidoEditar = (TextView)findViewById(R.id.tvCodigoLeidoEditar);
            tvDescripcionEditar = (TextView)findViewById(R.id.tvDescripcionEditar);

            btnCambiarUbiEditar = (Button)findViewById(R.id.btnCambiarUbiAuditoria);
            btnIngresarCodigoEditar = (Button)findViewById(R.id.btnIngresarCodigoEditar);
            btnEditarCantidad = (Button)findViewById(R.id.btnCantidadFisica);
            btnMenuPrincipalEditar = (Button)findViewById(R.id.btnMenuPrincipal);
            btnTotalUbicacionEditar = (Button)findViewById(R.id.btnTotalUbicacionBarrido);

            edCodigoCapturado = (EditText)findViewById(R.id.edCodigo);
            edCantidadEditar = (EditText)findViewById(R.id.edCantidadEditar);
            //------------------------------------------------------------------------------------------------
            //DECLARO VARIABLES GLOBALES
            //---------------------------------------------------------------------------------------
            b = new Beeper();
            verMis = new verificarMiscelaneos();
            verifChk = new verificarCheck();
            ubiDat = new UbicacionDato();
            //---------------------------------------------------------------------------------------

            edCodigoCapturado.requestFocus();

            ubicacionGlobal = ubiDat.obtenerDato();
            tvUbicacionEditar.setText(ubicacionGlobal);

            btnCambiarUbiEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        Intent i = new Intent(EditarCantidad.this ,Ubicacion.class);
                        startActivityForResult(i,1);
                    }catch (Exception e){
                        mensaje("0.- "+e,"Excepción","Aceptar");
                    }
                }
            });

            btnIngresarCodigoEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        if(!edCodigoCapturado.getText().toString().equalsIgnoreCase("")) {
                            BuscarCantidad busc = new BuscarCantidad();
                            String cantidad = busc.obtenerCantidadProducto(EditarCantidad.this,edCodigoCapturado.getText().toString(),ubiDat.obtenerDato());
                            Float cantidadEnDecimales = Float.parseFloat(cantidad);
                            DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
                            separadoresPersonalizados.setDecimalSeparator('.');
                            DecimalFormat formatoDecimales = new DecimalFormat("#.###", separadoresPersonalizados);
                            //formatoDecimales.format(cantidadActualizada);

                            tvCantidadEditar.setText(formatoDecimales.format(cantidadEnDecimales));

                            String codigoLeido = edCodigoCapturado.getText().toString().trim();
                            String ubicacionFinal = ubiDat.obtenerDato();

                            String descipcion = "NO EXISTE";
                            descipcion = buscarCodigoEditar(codigoLeido);
                            tvDescripcionEditar.setText(descipcion);
                            tvCodigoLeidoEditar.setText(codigoLeido);
                            edCodigoCapturado.setText("");
                            edCantidadEditar.requestFocus();
                        }
                    }catch (Exception e){
                        mensaje("1.- "+e,"Excepción","Aceptar");
                    }
                }
            });

            btnMenuPrincipalEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        finish();
                    }catch (Exception e){
                        mensaje("2.- "+e,"Excepción","Aceptar");
                    }
                }
            });

            btnTotalUbicacionEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        ubicacionGlobal = ubiDat.obtenerDato();
                        Intent i = new Intent(EditarCantidad.this ,TotalUbicacion.class);
                        i.putExtra("codigoUbicacion",ubicacionGlobal);
                        startActivity(i);
                    }catch (Exception e){
                        mensaje("3.- "+e,"Excepción","Aceptar");
                    }
                }
            });

            btnEditarCantidad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        if(tvCodigoLeidoEditar.getText().toString().trim().equalsIgnoreCase("")){
                            mensaje("Favor Ingresar o Capturar Código","Atención","Aceptar");
                        }else{
                            if(tvCantidadEditar.getText().toString().trim().equalsIgnoreCase("0")){
                                mensaje("Para Editar Cantidad Producto Debe Tener mas de 1 Item","Atención","Aceptar");
                            }else{
                                if(edCantidadEditar.getText().toString().trim().equalsIgnoreCase("")){
                                    mensaje("Favor Ingresar Cantidad a Modificar","Atención","Aceptar");
                                }else {
                                    String codigo = tvCodigoLeidoEditar.getText().toString().trim();
                                    String ubi = ubiDat.obtenerDato();
                                    String cant = edCantidadEditar.getText().toString().trim();
                                    editarCantidad(codigo, ubi, cant);
                                    Toast.makeText(EditarCantidad.this, "Operacion Exitosa", Toast.LENGTH_SHORT).show();
                                    limpiarInterfaz();
                                    edCodigoCapturado.requestFocus();
                                }
                            }
                        }
                    }catch (Exception e){
                        mensaje("4.- "+e,"Excepción","Aceptar");
                    }
                }
            });
            edCantidadEditar.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    try{
                        if(edCantidadEditar.getText().toString().trim().equals(".")){
                            mensaje("Debe ingresar un dígito antes de ingresar el punto decimal.","ATENCIÓN","Aceptar");
                            edCantidadEditar.setText("");
                        }
                    }catch (Exception e){
                        mensaje("5.0.- "+e,"Excepción","Aceptar");
                    }
                }
            });
        }catch (Exception e){
            mensaje("5.- "+e,"Excepción","Aceptar");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            super.onActivityResult(requestCode, resultCode, data);
            // Comprobamos si el resultado de la segunda actividad es "RESULT_CANCELED".
            if (resultCode == RESULT_OK) {
                ubicacionGlobal = ubiDat.obtenerDato();
                tvUbicacionEditar.setText(ubicacionGlobal);
                edCodigoCapturado.setText("");
                edCantidadEditar.setText("");
                Toast.makeText(this, "Operacion Exitosa", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            mensaje("6.- "+e,"Excepción","Aceptar");
        }
    }

    public String buscarCodigoEditar(String codi){
        String descrip="SIN RESULTADOS";
        try{
            SqlHelper sqlHel = new SqlHelper(EditarCantidad.this);
            SQLiteDatabase db = sqlHel.getReadableDatabase();

            String sql = "SELECT * FROM MAEPRODUCTOS WHERE CODIGO = '"+codi+"'";
            Cursor c = db.rawQuery(sql,null);
            while (c.moveToNext()){
                descrip = c.getString(c.getColumnIndex("descripcion"));
            }
            c.close();
            db.close();
        }catch (Exception e){
            mensaje("7.- "+e,"Excepción","Aceptar");
        }
        return descrip;
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
            mensaje("8.- "+e,"Excepción","Aceptar");
        }

        return false;
    }

    private void ejecutaLecturaSegunModelo() {
        try{
            if(!edCodigoCapturado.getText().toString().equalsIgnoreCase("")) {
//            b.activar(this);
                BuscarCantidad busc = new BuscarCantidad();
                String cantidad = busc.obtenerCantidadProducto(EditarCantidad.this,edCodigoCapturado.getText().toString(),ubiDat.obtenerDato());
                Float cantidadEnDecimales = Float.parseFloat(cantidad);
                DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
                separadoresPersonalizados.setDecimalSeparator('.');
                DecimalFormat formatoDecimales = new DecimalFormat("#.###", separadoresPersonalizados);
                //formatoDecimales.format(cantidadActualizada);

                tvCantidadEditar.setText(formatoDecimales.format(cantidadEnDecimales));

                String codigoLeido = edCodigoCapturado.getText().toString().trim();
                String ubicacionFinal = ubiDat.obtenerDato();

                String descipcion = "NO EXISTE";
                descipcion = buscarCodigoEditar(codigoLeido);
                tvDescripcionEditar.setText(descipcion);
                tvCodigoLeidoEditar.setText(codigoLeido);
                edCodigoCapturado.setText("");
                edCantidadEditar.requestFocus();
            }
        }catch (Exception e){
            mensaje("9.- "+e,"Excepción","Aceptar");
        }
    }

    public void editarCantidad(String codigo,String ubicacion,String cantidad){
        try {
            SqlHelper sqlHel = new SqlHelper(EditarCantidad.this);
            SQLiteDatabase db = sqlHel.getWritableDatabase();
            if (db != null) {
                String SQL = "update MAEDATOS SET CANTIDAD = '"+cantidad+"' WHERE CODIGO = '"+codigo+"' AND UBICACION = '"+ubicacion+"'";
                db.execSQL(SQL);
                db.close();
            }
        }catch (Exception e){
            mensaje("10.- "+e,"Excepción","Aceptar");
        }
    }

    public void limpiarInterfaz(){
        try{
            tvCantidadEditar.setText("0.0");
            tvCodigoLeidoEditar.setText("");
            edCantidadEditar.setText("");
            tvDescripcionEditar.setText("");
        }catch (Exception e){
            mensaje("11.- "+e,"Excepción","Aceptar");
        }
    }

    public void mensaje(String contenido, String titulo, String botonNom){
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(EditarCantidad.this);
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
            mensaje("12.- "+e,"Excepción","Aceptar");
        }
    }
}
