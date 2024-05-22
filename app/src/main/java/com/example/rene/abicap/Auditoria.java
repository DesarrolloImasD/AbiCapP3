package com.example.rene.abicap;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import com.example.camilodesarrollo.abicap.R;

public class Auditoria extends AppCompatActivity {
    private String ubicacionGlobal, codigoBarridoObtenido;
    private TextView tvUbicacionAuditoria, tvCantidadAuditoria, tvUnidadMedida, tvCodigoLeidoAuditoria, tvDescripcionAuditoria, tvUbicaciones;
    private EditText edCodigo, edCantidadAuditoria;
    private Button btnCambiarUbiAuditoria, btnIngresarCodigoAuditoria, btnCantidadFisica, btnMenuPrincipal;
    public Boolean mostrarDes, contraArch, dupli;
    public Beeper b;
    public verificarMiscelaneos verMis;
    public verificarCheck verifChk;
    public UbicacionDato ubiDat;
    public Float cantidadFisicaFloat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_auditoria);
            //DECLARAR INTERFAZ GRAFICA
            //-----------------------------------------------------------------------------------------------
            tvUbicacionAuditoria = (TextView) findViewById(R.id.tvUbicacionAuditoria);
            tvCantidadAuditoria = (TextView) findViewById(R.id.tvCantidadAuditoria);
            tvUnidadMedida = (TextView) findViewById(R.id.tvUnidadMedida);
            tvCodigoLeidoAuditoria = (TextView) findViewById(R.id.tvCodigoLeidoAuditoria);
            tvDescripcionAuditoria = (TextView) findViewById(R.id.tvDescripcionAuditoria);
            tvUbicaciones = (TextView) findViewById(R.id.tvUbicaciones);

            edCodigo = (EditText) findViewById(R.id.edCodigo);
            edCantidadAuditoria = (EditText) findViewById(R.id.edCantidadAuditoria);

            btnCambiarUbiAuditoria = (Button) findViewById(R.id.btnCambiarUbiAuditoria);
            btnIngresarCodigoAuditoria = (Button) findViewById(R.id.btnIngresarCodigoAuditoria);
            btnCantidadFisica = (Button) findViewById(R.id.btnCantidadFisica);
            btnMenuPrincipal = (Button) findViewById(R.id.btnMenuPrincipal);

            //-----------------------------------------------------------------------------------------------

            //DECLARO VARIABLES GLOBALES
            //---------------------------------------------------------------------------------------
            b = new Beeper();
            verMis = new verificarMiscelaneos();
            verifChk = new verificarCheck();
            ubiDat = new UbicacionDato();
            //---------------------------------------------------------------------------------------
            ubicacionGlobal = ubiDat.obtenerDato();
            tvUbicacionAuditoria.setText(ubicacionGlobal);

            //VERIFICO MISCELANEOS
            //------------------------------------------------------------------------------------------
            mostrarDes = verifChk.checkearDescripcion(this);

            if (!mostrarDes) {
                tvDescripcionAuditoria.setVisibility(GONE);
            }

            contraArch = verifChk.checkearContraArchivo(this);

            dupli = verifChk.checkearDuplicado(this);

            //------------------------------------------------------------------------------------------
            edCodigo.requestFocus();

            btnIngresarCodigoAuditoria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        ejecutaLecturaSegunModelo();
                    } catch (Exception e) {
                        mensaje("0.- " + e, "Excepción", "Aceptar");
                    }
                }
            });
            btnMenuPrincipal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        finish();
                    }catch (Exception e){
                        mensaje("1.- "+e, "Excepción", "Aceptar");
                    }
                }
            });

            btnCambiarUbiAuditoria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        Intent i = new Intent(Auditoria.this ,Ubicacion.class);
                        startActivityForResult(i,1);
                    }catch (Exception e){
                        mensaje("2.- "+e, "Excepción", "Aceptar");
                    }
                }
            });

            btnCantidadFisica.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        cantidadFisicaFloat = Float.parseFloat(edCantidadAuditoria.getText().toString());
                        actualizarCantidad(cantidadFisicaFloat,tvCodigoLeidoAuditoria.getText().toString(),ubicacionGlobal);
                        edCodigo.requestFocus();
                        btnCantidadFisica.setVisibility(GONE);
                    }catch (Exception e){
                        mensaje("2.- "+e, "Excepción", "Aceptar");
                    }
                }
            });


        }catch (Exception e){
            mensaje("4.- "+e, "Excepción", "Aceptar");
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        try{
         /*
        ZEBRA TC25BJ     - codigos de botones para capturar 286, 285 (285 boton mango)

        NEWLAND MT65-U     - codigos de botones para capturar 240, 241
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
                    if ((keyCode == 240) || (keyCode == 241)|| (keyCode == 243)){
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
        }
        return false;
    }

    private void ejecutaLecturaSegunModelo() {
        try {
            String codigoObt = edCodigo.getText().toString().trim();
            String ubica = ubiDat.obtenerDato();
            if (!codigoObt.equalsIgnoreCase("")) {
                if (dupli) {
                    Boolean esDuplicado = verMis.existeRegistro(codigoObt, ubica, Auditoria.this);
                    if (esDuplicado) {
//                        b.activarIncorrecto(LecturaBarrido.this);
                        mensaje("El codigo ya fue pistoleado", "Advertencia", "Aceptar");
                    } else {
                        if (contraArch) {
                            Boolean respu = verMis.existeEnMaestro(codigoObt, Auditoria.this);
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
                        Boolean respu = verMis.existeEnMaestro(codigoObt, Auditoria.this);
                        if (respu) {
                            metodoAccion(codigoObt, ubica);
                        } else {
//                            b.activarIncorrecto(LecturaBarrido.this);
                            mensaje("No existe Codigo en Archivo", "Advertencia", "Aceptar");
                        }
                    } else {
                        metodoAccion(codigoObt, ubica);
                    }
                }
            }
            edCodigo.setText("");
        }catch (Exception e){
            mensaje("6.- "+e, "Excepción", "Aceptar");
        }
    }

    public void metodoAccion(String codAc, final String ubiAc){
        try {
//            b.activar(LecturaBarrido.this);
            String descrip = "NO EXISTE";
            String sto = "NO EXISTE";
            String unidadMed = "NO EXISTE";
            String fisi = "NO EXISTE";
            String ubicaciones = "SIN UBICACIONES";

            buscarCodigoBarrido(codAc, ubiAc);
            ArrayList<String> arregloBD = buscarCodigoBarrido(codAc, ubiAc);
            ArrayList<String> arregloBD2=buscarUbicaciones(codAc);


            sto=arregloBD.get(0);
            fisi=arregloBD.get(1);

            ubicaciones=arregloBD2.get(0);
            descrip=arregloBD2.get(1);
            unidadMed=arregloBD2.get(2);
            if(ubicaciones.equals("")){ //para cuando no existe el codigo en ninguna ubicacion....
                ubicaciones = "SIN UBICACIONES";
            }

            if(ubicaciones.equals("; ")){ //para cuando existe el codigo en otra ubicacion ....
                ubicaciones = "SIN UBICACIONES";
            }

            tvCantidadAuditoria.setText(sto);
            tvUnidadMedida.setText(unidadMed);
            tvCodigoLeidoAuditoria.setText(codAc);
            edCantidadAuditoria.setText(fisi);
            tvDescripcionAuditoria.setText(descrip);
            tvUbicaciones.setText(ubicaciones);


           // mensaje("En sistema el codigo: " +codAc + " existe: "+sto+ " veces. ¿Es correcto?", "Atención", "Aceptar");

            if(sto.equals("SIN STOCK")){
                mensaje("Codigo no existe en esa ubicacion", "Atención", "Aceptar");
                tvCantidadAuditoria.setTextColor(Color.RED);
                tvUnidadMedida.setTextColor(Color.RED);
                tvCodigoLeidoAuditoria.setTextColor(Color.RED);
                edCantidadAuditoria.setTextColor(Color.RED);
                tvDescripcionAuditoria.setTextColor(Color.RED);
                tvUbicaciones.setTextColor(Color.RED);
                btnCantidadFisica.setVisibility(GONE);
            }else{
                tvCantidadAuditoria.setTextColor(Color.BLACK);
                tvUnidadMedida.setTextColor(Color.BLACK);
                tvCodigoLeidoAuditoria.setTextColor(Color.BLACK);
                edCantidadAuditoria.setTextColor(Color.BLACK);
                tvDescripcionAuditoria.setTextColor(Color.BLACK);
                tvUbicaciones.setTextColor(Color.BLACK);


                cantidadFisicaFloat = Float.parseFloat(sto);
                AlertDialog.Builder builder = new AlertDialog.Builder(Auditoria.this);
                builder.setMessage("Codigo: " +codAc + "\nCantidad QAD: "+sto+ "\nConteo: "+fisi+"\n¿Es correcto?")
                        .setTitle("Advertencia")
                        .setCancelable(false)
                        .setNegativeButton("Cancelar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        edCantidadAuditoria.requestFocus();
                                        btnCantidadFisica.setVisibility(VISIBLE);
                                    }
                                })
                        .setPositiveButton("Continuar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        actualizarCantidad(cantidadFisicaFloat,tvCodigoLeidoAuditoria.getText().toString(),ubiAc);
                                        edCodigo.requestFocus();
                                        btnCantidadFisica.setVisibility(GONE);
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }


            //insertarBarridoEnBase(codAc, ubiAc, descipcion);
        }catch (Exception e){
            mensaje("12.- "+e, "Excepción", "Aceptar");
        }
    }

    public void actualizarCantidad(Float cantidad,String codig, String ubic) {
        try {
            SqlHelper sqlHel = new SqlHelper(Auditoria.this);
            SQLiteDatabase db = sqlHel.getWritableDatabase();
            String resultado = String.valueOf(cantidad);
            if (db != null) {
                String SQL = "update MAEAUDITORIA SET FISICO = '"+resultado+"' WHERE CODIGO = '"+codig+"' AND UBICACION = '"+ubic+"'";
                db.execSQL(SQL);
                db.close();
            }
            DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
            separadoresPersonalizados.setDecimalSeparator('.');
            DecimalFormat formatoDecimales = new DecimalFormat("#.###", separadoresPersonalizados);

            edCantidadAuditoria.setText(formatoDecimales.format(Float.parseFloat(resultado)));
            tvCodigoLeidoAuditoria.setText(codig);

            Toast.makeText(Auditoria.this, "Se ha actualizado el registro.", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Log.e("Error bd","error");
            mensaje("9.- "+e, "Excepción", "Aceptar");
        }
    }

    public void insertarBarridoEnBase(String codi,String ubic, String desc){

        try{
            SqlHelper sqlHel = new SqlHelper(Auditoria.this);
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

    public void actualizarCantidadd(Float cantidad,String codig, String ubic) {
        try {
            SqlHelper sqlHel = new SqlHelper(Auditoria.this);
            SQLiteDatabase db = sqlHel.getWritableDatabase();
            String resultado = String.valueOf(cantidad + 1);
            if (db != null) {
                String SQL = "update MAEAUDITORIA SET FISICO = '"+resultado+"', DESCRIPCION = '"+tvDescripcionAuditoria.getText().toString().trim()+"' WHERE CODIGO = '"+codig+"' AND UBICACION = '"+ubic+"'";
                db.execSQL(SQL);
                db.close();
            }
            DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
            separadoresPersonalizados.setDecimalSeparator('.');
            DecimalFormat formatoDecimales = new DecimalFormat("#.###", separadoresPersonalizados);

            tvCantidadAuditoria.setText(formatoDecimales.format(Float.parseFloat(resultado)));
            tvCodigoLeidoAuditoria.setText(codig);
        }catch(Exception e){
            Log.e("Error bd","error");
            mensaje("9.- "+e, "Excepción", "Aceptar");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            super.onActivityResult(requestCode, resultCode, data);
            // Comprobamos si el resultado de la segunda actividad es "RESULT_CANCELED".
            if (resultCode == RESULT_OK) {
                ubicacionGlobal = ubiDat.obtenerDato();
                tvUbicacionAuditoria.setText(ubicacionGlobal);
                edCodigo.setText("");
                Toast.makeText(this, "Operacion Exitosa", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            mensaje("11.- "+e, "Excepción", "Aceptar");
        }
    }

    public void insertarRegistroBarrido(String codig, String ubic, String desc) {
        try {
            SqlHelper sqlHel = new SqlHelper(Auditoria.this);
            SQLiteDatabase db = sqlHel.getWritableDatabase();
            if (db != null) {
                String SQL = "INSERT INTO MAEDATOS (UBICACION,CODIGO, DESCRIPCION, CANTIDAD) VALUES ('"+ubic+"','"+codig+"','"+desc+"','1')";
                db.execSQL(SQL);
                db.close();
            }
            DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
            separadoresPersonalizados.setDecimalSeparator('.');
            DecimalFormat formatoDecimales = new DecimalFormat("#.###", separadoresPersonalizados);

            tvCantidadAuditoria.setText("1");
            tvCodigoLeidoAuditoria.setText(codig);
        }catch(Exception e){
            Log.e("Error bd","error");
            mensaje("10.- "+e, "Excepción", "Aceptar");
        }
    }

    public ArrayList<String> buscarCodigoBarrido(String codi, String ubi){
//        String descrip="SIN RESULTADOS";
        ArrayList<String> arreglito = new ArrayList<>();
//        String descrip="SIN DESCRIPCION";
        String stoc="SIN STOCK";
//        String medi="SIN UNIDAD MEDIDA";
        String fisic="SIN CANTIDAD FISICA";
        String listaubi="";
        String listaubicacion="";

        try{
            SqlHelper sqlHel = new SqlHelper(Auditoria.this);
            SQLiteDatabase db = sqlHel.getReadableDatabase();

            String sql = "SELECT * FROM MAEAUDITORIA WHERE CODIGO = '"+codi+"' AND UBICACION = '"+ubi+"'";
            Cursor c = db.rawQuery(sql,null);
            while (c.moveToNext()){

                stoc = c.getString(c.getColumnIndex("stock"));
                fisic = c.getString(c.getColumnIndex("fisico"));

//                descrip = c.getString(c.getColumnIndex("descripcion"));
//                medi = c.getString(c.getColumnIndex("unidad_medida"));
            }
            c.close();
            db.close();

            /*
            SqlHelper sqlHel2 = new SqlHelper(Auditoria.this);
            SQLiteDatabase db2 = sqlHel2.getReadableDatabase();
            String sql2 = "SELECT * FROM MAEAUDITORIA WHERE CODIGO = '"+codi+"'";
            Cursor c2 = db2.rawQuery(sql2,null);
            while (c2.moveToNext()){
                listaubi = c2.getString(c2.getColumnIndex("ubicacion"));
                listaubicacion = listaubicacion+listaubi+"; ";
            }
            c2.close();
            db2.close();
            */
            //arreglito.add(descrip);
            arreglito.add(stoc);
            //arreglito.add(medi);
            arreglito.add(fisic);
            //arreglito.add(listaubicacion);

        }catch (Exception e) {
            Log.e("Fichero sd", "Error");
            mensaje("7.- "+e, "Excepción", "Aceptar");
        }
        return arreglito;
    }

    public ArrayList<String> buscarUbicaciones(String codi){
        ArrayList<String> arreglito2 = new ArrayList<>();
        String descrip="SIN DESCRIPCION";
        String stoc="SIN STOCK";
        String medi="SIN UNIDAD MEDIDA";
        String fisic="SIN CANTIDAD FISICA";
        String listaubi="";
        String listaubicacion="";

        try{
            SqlHelper sqlHel = new SqlHelper(Auditoria.this);
            SQLiteDatabase db = sqlHel.getReadableDatabase();


            SqlHelper sqlHel2 = new SqlHelper(Auditoria.this);
            SQLiteDatabase db2 = sqlHel2.getReadableDatabase();
            String sql2 = "SELECT * FROM MAEAUDITORIA WHERE CODIGO = '"+codi+"'";
            Cursor c2 = db2.rawQuery(sql2,null);
            while (c2.moveToNext()){
                listaubi = c2.getString(c2.getColumnIndex("ubicacion"));
                listaubicacion = listaubicacion+listaubi+"; ";

                descrip = c2.getString(c2.getColumnIndex("descripcion"));
                medi = c2.getString(c2.getColumnIndex("unidad_medida"));
            }
            arreglito2.add(listaubicacion);
            arreglito2.add(descrip);
            arreglito2.add(medi);

        }catch (Exception e) {
            Log.e("Fichero sd", "Error");
            mensaje("7.5- "+e, "Excepción", "Aceptar");
        }
        return arreglito2;
    }
    public void mensaje(String contenido, String titulo, String botonNom){
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(Auditoria.this);
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
