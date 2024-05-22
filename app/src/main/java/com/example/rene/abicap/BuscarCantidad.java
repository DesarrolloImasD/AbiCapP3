package com.example.rene.abicap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

/**
 * Created by CamiloDesarrollo on 10/11/2016.
 */

public class BuscarCantidad {

    private boolean sdDesponible, sdAccesoEscritura;

    public String obtenerCantidadProducto(Context cont,String codi,String ubi){
        verificarEstado();
        String respuesta = "0";
        if (sdAccesoEscritura && sdDesponible) {
            try{
                SqlHelper sqlHel = new SqlHelper(cont);
                SQLiteDatabase db = sqlHel.getReadableDatabase();

                String sql = "SELECT * FROM MAEDATOS WHERE CODIGO = '"+codi+"' AND UBICACION = '"+ubi+"'";
                Cursor c = db.rawQuery(sql,null);
                if (c.moveToNext()){
                    respuesta = c.getString(c.getColumnIndex("cantidad"));
                }else{
                    respuesta = "0";
                }
                c.close();
                db.close();
            }catch (Exception ex) {
                Log.e("Fichero sd", "Error");
            }
        }
        return respuesta;
    }




    public void verificarEstado(){
        String estado = Environment.getExternalStorageState();

        if(estado.equals(Environment.MEDIA_MOUNTED))
        {
            sdDesponible = true;
            sdAccesoEscritura = true;
        }
        else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
        {
            sdDesponible = true;
            sdAccesoEscritura = false;

        }else{
            sdDesponible = false;
            sdAccesoEscritura = false;
        }
    }
}
