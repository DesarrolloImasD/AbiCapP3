package com.example.rene.abicap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by CamiloDesarrollo on 22/11/2016.
 */

public class ObtenerLicencia {

    public Boolean obtenerLicencia(Context cont){
        try{
            SqlHelper sqlHel = new SqlHelper(cont);
            SQLiteDatabase db = sqlHel.getReadableDatabase();

            String sql = "SELECT * FROM MAEUBICACION WHERE CODIGO = 'VALIDADO'";
            Cursor c = db.rawQuery(sql,null);
            if (c.moveToNext()){
                return true;
            }
            c.close();
            db.close();
        }catch (Exception ex) {
            Log.e("Fichero sd", "Error");
        }
        return false;
    }

}
