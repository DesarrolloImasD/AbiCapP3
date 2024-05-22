package com.example.rene.abicap;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camilodesarrollo.abicap.R;

public class Ingresar_Producto extends AppCompatActivity {

    private EditText edCodigoLeido, edDescripcionIngresada;
    String codigoObt =""; //global
    String descriObtenida =""; // global
    private TextView tvCodigo, tvDescripcion, tvCodigoObtenido, tvIngreseCodigo, tvIngreseDescripcion, tvDescripcionObtenida;
    private Button btnIngresarDescripcion,btnMenuPrincipal,btnCambiarUbiBarrido,btnTotalUbicacionBarrido;
    public Boolean mostrarDes,contraArch,dupli;
    public Beeper b;
    public verificarMiscelaneos verMis;
    public verificarCheck verifChk;
    public UbicacionDato ubiDat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ingresar_producto);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN );
            //DECLARAR INTERFAZ GRAFICA
            //-----------------------------------------------------------------------------------------------
            edCodigoLeido = (EditText)findViewById(R.id.edCodigoLeido);
            edDescripcionIngresada = (EditText)findViewById(R.id.edDescripcionIngresada);
            btnIngresarDescripcion=(Button)findViewById(R.id.btnIngresarDescripcion);
            btnMenuPrincipal=(Button)findViewById(R.id.btnMenuPrincipal);
            tvIngreseCodigo = (TextView)findViewById(R.id.tvIngreseCodigo);
            tvIngreseDescripcion = (TextView)findViewById(R.id.tvIngreseDescripcion);
            tvCodigo = (TextView)findViewById(R.id.tvCodigo);
            tvCodigoObtenido = (TextView)findViewById(R.id.tvCodigoObtenido);
            tvDescripcionObtenida = (TextView)findViewById(R.id.tvDescripcionObtenida);
            tvDescripcion = (TextView)findViewById(R.id.tvDescripcion);

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
//            tvDescripcionProducto.setVisibility(View.GONE);
            }

            contraArch = verifChk.checkearContraArchivo(this);

            dupli = verifChk.checkearDuplicado(this);

            //------------------------------------------------------------------------------------------
//        edCodigoLeido.setEnabled(false);
            edCodigoLeido.requestFocus();

            btnIngresarDescripcion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        descriObtenida = edCodigoLeido.getText().toString().trim();
                        insertarOActualizarDescripcionEnBase(codigoObt,descriObtenida );
                        edCodigoLeido.setText("");
                    }catch (Exception e){
                        mensaje("0.- "+e,"Excepción","Aceptar");
                    }

                }
            });

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
        }catch (Exception e){
            mensaje("2.- "+e,"Excepción","Aceptar");
        }


    }

    public void LimpiarVentana(){

        try{
            tvIngreseCodigo.setVisibility(View.VISIBLE);
            tvIngreseDescripcion.setVisibility(View.GONE);
            btnIngresarDescripcion.setVisibility(View.GONE);
            tvCodigoObtenido.setVisibility(View.GONE);
            tvDescripcionObtenida.setVisibility(View.GONE);
            tvCodigo.setVisibility(View.GONE);
            tvDescripcion.setVisibility(View.GONE);
        }catch (Exception e){
            mensaje("3.- "+e,"Excepción","Aceptar");
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
            mensaje("4.- "+e,"Excepción","Aceptar");
        }

        return false;
    }


    public void ejecutaLecturaSegunModelo() {
        try{
            edCodigoLeido.setFocusable(true);
            codigoObt = edCodigoLeido.getText().toString().trim();

            if (codigoObt.equalsIgnoreCase("")) {
                Toast.makeText(Ingresar_Producto.this, "Se ha pistoleado mal. Intente nuevamente.", Toast.LENGTH_SHORT).show();
                LimpiarVentana();
            } else {
                edCodigoLeido.setText("");

                //hago que se desaparezcan al pistolear
//            edCodigoLeido.setVisibility(View.GONE);
                tvIngreseCodigo.setVisibility(View.GONE);

                //hago que aparezcan al pistolear
//            edDescripcionIngresada.setVisibility(View.VISIBLE);
                tvIngreseDescripcion.setVisibility(View.VISIBLE);
                btnIngresarDescripcion.setVisibility(View.VISIBLE);
                tvCodigoObtenido.setVisibility(View.VISIBLE);
                tvDescripcionObtenida.setVisibility(View.VISIBLE);
                tvCodigo.setVisibility(View.VISIBLE);
                tvDescripcion.setVisibility(View.VISIBLE);

//            edDescripcionIngresada.setFocusable(true);
                descriObtenida = buscarDescripcion(codigoObt);

                btnIngresarDescripcion.setBackgroundDrawable(getResources().getDrawable(R.drawable.botonverde));
                btnIngresarDescripcion.setText("Ingresar Descripción");
                if ((descriObtenida != "") && (descriObtenida != "SIN DESCRIPCION")) {
                    btnIngresarDescripcion.setBackgroundDrawable(getResources().getDrawable(R.drawable.botonamarillo));
                    btnIngresarDescripcion.setText("Ingresar Descripción");
                } else {
                    btnIngresarDescripcion.setBackgroundDrawable(getResources().getDrawable(R.drawable.botonamarillo));
                    btnIngresarDescripcion.setText("Ingresar Descripción");
                    edCodigoLeido.setText("");
                }

                tvCodigoObtenido.setText(codigoObt);
                tvDescripcionObtenida.setText(descriObtenida);
            }
        }catch (Exception e){
            mensaje("5.- "+e,"Excepción","Aceptar");
        }
    }


    //Metodo permite obtener la descripcion de un producto existente en tabla MAESPRODUCTOS a traves del codigo pistoleado
    public String buscarDescripcion(String codi){
        String descrip="SIN DESCRIPCION";
        try{
        SqlHelper sqlHel = new SqlHelper(Ingresar_Producto.this);
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

    //Inserta o Actualiza la descripcion segun exista o no previamente en tabla MAEPRODUCTOS
    public void insertarOActualizarDescripcionEnBase(String codi,String desc){
        try{
            SqlHelper sqlHel = new SqlHelper(Ingresar_Producto.this);
            SQLiteDatabase db = sqlHel.getReadableDatabase();

//            desc=buscarDescripcion(codi);
//            String sql = "SELECT * FROM MAEPRODUCTOS WHERE CODIGO = '"+codi+"'";
            String vacio = edCodigoLeido.getText().toString().trim();
            if (vacio.equalsIgnoreCase("")){
                Toast.makeText(Ingresar_Producto.this, "Debe ingresar una descripción", Toast.LENGTH_SHORT).show();
            }else{
/*
                String descripcion = tvDescripcionObtenida.getText().toString().trim();
                if(descripcion!="SIN DESCRIPCION"){
                    actualizarDescripcion(codi,desc);
                    finish();
                }else{
                    insertarRegistroBarrido(codi,desc);
                    finish();
                }
                */
                String descripcion = tvDescripcionObtenida.getText().toString().trim();
                if(descripcion=="SIN DESCRIPCION") {
                    insertarRegistroBarrido(codi, desc);
                    finish();
                }
            }
            db.close();
        }catch (Exception e){
            mensaje("7.- "+e,"Excepción","Aceptar");
        }
    }

    public void actualizarDescripcion(String codig, String descr) {
        try {
            SqlHelper sqlHel = new SqlHelper(Ingresar_Producto.this);
            SQLiteDatabase db = sqlHel.getWritableDatabase();

            if (db != null) {
                String SQL = "update MAEPRODUCTOS SET DESCRIPCION = '"+descr+"' WHERE CODIGO = '"+codig+"'";
                db.execSQL(SQL);
                db.close();
            }

        }catch (Exception e){
            mensaje("8.- "+e,"Excepción","Aceptar");
        }
    }

    public void insertarRegistroBarrido(String codig, String desc) {
        try {
            SqlHelper sqlHel = new SqlHelper(Ingresar_Producto.this);
            SQLiteDatabase db = sqlHel.getWritableDatabase();
            if (db != null) {
                String SQL = "INSERT INTO MAEPRODUCTOS (CODIGO, DESCRIPCION) VALUES ('"+codig+"','"+desc+"')";
                db.execSQL(SQL);
                       SQL = "update MAEDATOS SET DESCRIPCION = '"+desc+"' WHERE CODIGO = '"+codig+"'";
                db.execSQL(SQL);
                db.close();
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

//            tvUbicacionBarrido.setText(ubicacionGlobal);

                edCodigoLeido.setText("");
                Toast.makeText(this, "Operacion Exitosa", Toast.LENGTH_SHORT).show();

            }
        }catch (Exception e){
            mensaje("10.- "+e,"Excepción","Aceptar");
        }
    }

    public void mensaje(String contenido, String titulo, String botonNom){
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(Ingresar_Producto.this);
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
            mensaje("11.- "+e,"Excepción","Aceptar");
        }
    }

}
