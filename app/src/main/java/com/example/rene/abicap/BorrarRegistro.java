package com.example.rene.abicap;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camilodesarrollo.abicap.R;

public class BorrarRegistro extends AppCompatActivity {

    private String ubicacionGlobal,codigoBarridoObtenido;
    private TextView tvUbicacionBorrado,tvCantidadBorrado,tvCodigoLeidoBorrado,tvDescripcionBorrado;
    private EditText edCodigoLeidoBorrado;
    private Button btnCambiarUbiBorrado,btnIngresarCodigoBorrado,btnBorrarRegistro,btnMenuPrincipalBorrado,btnTotalUbicacionBorrado;
    public Boolean mostrarDes,contraArch,dupli;
    public Beeper b;
    public verificarMiscelaneos verMis;
    public verificarCheck verifChk;
    public UbicacionDato ubiDat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_borrar_registro);

            //DEFINICION INTERFAZ GRAFICA
            //------------------------------------------------------------------------------------------
            tvUbicacionBorrado = (TextView)findViewById(R.id.tvUbicacionBorrado);
            tvCantidadBorrado = (TextView)findViewById(R.id.tvCantidadBorrado);
            tvCodigoLeidoBorrado = (TextView)findViewById(R.id.tvCodigoLeidoBorrado);
            tvDescripcionBorrado = (TextView)findViewById(R.id.tvDescripcionBorrado);

            edCodigoLeidoBorrado = (EditText)findViewById(R.id.edCodigo);

            btnCambiarUbiBorrado = (Button)findViewById(R.id.btnCambiarUbiAuditoria);
            btnIngresarCodigoBorrado = (Button)findViewById(R.id.btnIngresarCodigoBorrado);
            btnBorrarRegistro = (Button)findViewById(R.id.btnBorrarRegistro);
            btnMenuPrincipalBorrado = (Button)findViewById(R.id.btnMenuPrincipal);
            btnTotalUbicacionBorrado = (Button)findViewById(R.id.btnTotalUbicacionBarrido);
            //------------------------------------------------------------------------------------------
            //DECLARO VARIABLES GLOBALES
            //---------------------------------------------------------------------------------------
            b = new Beeper();
            verMis = new verificarMiscelaneos();
            verifChk = new verificarCheck();
            ubiDat = new UbicacionDato();
            //---------------------------------------------------------------------------------------

            ubicacionGlobal = ubiDat.obtenerDato();
            tvUbicacionBorrado.setText(ubicacionGlobal);
            //VERIFICO MISCELANEOS
            //------------------------------------------------------------------------------------------
            mostrarDes = verifChk.checkearDescripcion(this);

            if(!mostrarDes){
                tvDescripcionBorrado.setVisibility(View.GONE);
            }
            contraArch = verifChk.checkearContraArchivo(this);

            dupli = verifChk.checkearDuplicado(this);


            //------------------------------------------------------------------------------------------
            edCodigoLeidoBorrado.requestFocus();
            btnIngresarCodigoBorrado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        String codigoObt = edCodigoLeidoBorrado.getText().toString().trim();
                        String ubica = ubiDat.obtenerDato();
                        if (!codigoObt.equalsIgnoreCase("")) {
//                    b.activar(BorrarRegistro.this);
                            metodoAccion(codigoObt, ubica);

                        }
                    }catch (Exception e){
                        mensaje("0.- "+e,"Excepción","Aceptar");
                    }
                }
            });

            btnCambiarUbiBorrado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        Intent i = new Intent(BorrarRegistro.this ,Ubicacion.class);
                        startActivityForResult(i,1);
                    }catch (Exception e){
                        mensaje("1.- "+e,"Excepción","Aceptar");
                    }
                }
            });

            btnMenuPrincipalBorrado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        finish();
                    }catch (Exception e){
                        mensaje("2.- "+e,"Excepción","Aceptar");
                    }
                }
            });

            btnBorrarRegistro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{

                        if(tvCantidadBorrado.getText().toString().trim().equalsIgnoreCase("0")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(BorrarRegistro.this);
                            builder.setMessage("Favor Ingresar un Código Existente")
                                    .setTitle("Advertencia")
                                    .setCancelable(false)
                                    .setNeutralButton("Aceptar",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }else{
                            if(!tvCodigoLeidoBorrado.getText().toString().equalsIgnoreCase("")) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(BorrarRegistro.this);
                                builder.setMessage("¿Esta seguro que desea eliminar este registro?")
                                        .setTitle("Advertencia")
                                        .setCancelable(false)
                                        .setNegativeButton("Cancelar",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                })
                                        .setPositiveButton("Aceptar",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        String codigoLeido = tvCodigoLeidoBorrado.getText().toString().trim();
                                                        String ubicacionFinal = ubiDat.obtenerDato();
                                                        limpiarRegistro(codigoLeido,ubicacionFinal);
                                                        edCodigoLeidoBorrado.setText("");
                                                        tvCantidadBorrado.setText("0");
                                                        tvCodigoLeidoBorrado.setText("");
                                                        tvDescripcionBorrado.setText("");
                                                    }
                                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }
                    }catch (Exception e){
                        mensaje("3.- "+e,"Excepción","Aceptar");
                    }
                }
            });

            btnTotalUbicacionBorrado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        //LO QUE DEBE HACER EL BOTON MOSTRAR CANTIDAD UBICACION
                        ubicacionGlobal = ubiDat.obtenerDato();
                        Intent i = new Intent(BorrarRegistro.this ,TotalUbicacion.class);
                        i.putExtra("codigoUbicacion",ubicacionGlobal);
                        startActivity(i);
                    }catch (Exception e){
                        mensaje("4.- "+e,"Excepción","Aceptar");
                    }
                }
            });
        }catch (Exception e){
            mensaje("5.- "+e,"Excepción","Aceptar");
        }
    }

    public String buscarCodigo(String codi){
        String descrip="SIN RESULTADOS";
        try{
            SqlHelper sqlHel = new SqlHelper(BorrarRegistro.this);
            SQLiteDatabase db = sqlHel.getReadableDatabase();

            String sql = "SELECT * FROM MAEPRODUCTOS WHERE CODIGO = '"+codi+"'";
            Cursor c = db.rawQuery(sql,null);
            while (c.moveToNext()){
                descrip = c.getString(c.getColumnIndex("descripcion"));
            }
            c.close();
            db.close();
        }catch (Exception e){
            mensaje("6.- "+e,"Excepción","Aceptar");
        }

        return descrip;
    }

    public String traerCantidad(String codi,String ubic){
        String respuesta = "0";
        try{
            SqlHelper sqlHel = new SqlHelper(BorrarRegistro.this);
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
        }catch (Exception e){
            mensaje("7.- "+e,"Excepción","Aceptar");
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
            mensaje("8.- "+e,"Excepción","Aceptar");
        }

        return false;
    }

    private void ejecutaLecturaSegunModelo() {
        try{
            String codigoObt = edCodigoLeidoBorrado.getText().toString().trim();
            String ubica = ubiDat.obtenerDato();
            if (!codigoObt.equalsIgnoreCase("")) {
//            b.activar(this);
                metodoAccion(codigoObt, ubica);
            }
        }catch (Exception e){
            mensaje("9.- "+e,"Excepción","Aceptar");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            super.onActivityResult(requestCode, resultCode, data);
            // Comprobamos si el resultado de la segunda actividad es "RESULT_CANCELED".
            if (resultCode == RESULT_OK) {
                ubicacionGlobal = ubiDat.obtenerDato();
                tvUbicacionBorrado.setText(ubicacionGlobal);
                edCodigoLeidoBorrado.setText("");
                Toast.makeText(this, "Operacion Exitosa", Toast.LENGTH_SHORT)
                        .show();
            }
        }catch (Exception e){
            mensaje("10.- "+e,"Excepción","Aceptar");
        }
    }

    public void limpiarRegistro(String cod, String ubi){
        try{
            SqlHelper sqlHel = new SqlHelper(this);
            SQLiteDatabase db = sqlHel.getWritableDatabase();
            if(db!= null){
                String SQL = "DELETE FROM MAEDATOS WHERE UBICACION = '"+ubi+"' AND CODIGO = '"+cod+"'";
                db.execSQL(SQL);
            }
            db.close();
            Toast.makeText(this, "Operacion Exitosa", Toast.LENGTH_SHORT)
                    .show();
        }catch (Exception e){
            mensaje("11.- "+e,"Excepción","Aceptar");
        }

    }

    public void metodoAccion(String cod, String ubica){
        try{
            String descipcion = "NO EXISTE";
            descipcion = buscarCodigo(cod);
            tvDescripcionBorrado.setText(descipcion);
            String cantidadProd = traerCantidad(cod, ubica);
            tvCantidadBorrado.setText(cantidadProd);
            tvCodigoLeidoBorrado.setText(cod);
            edCodigoLeidoBorrado.setText("");
        }catch (Exception e){
            mensaje("12.- "+e,"Excepción","Aceptar");
        }
    }

    public void mensaje(String contenido, String titulo, String botonNom){
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(BorrarRegistro.this);
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
