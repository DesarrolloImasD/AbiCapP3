package com.example.rene.abicap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SqlHelper extends SQLiteOpenHelper {

    private static String name = "AbiCapAct";
    private static int version = 1;
    private static SQLiteDatabase.CursorFactory cursorFactory = null;


    public SqlHelper(Context context){
        super(context,name,cursorFactory, version);
    }

    protected static String TableProductos = "MAEPRODUCTOS";
    protected static String TableUbicacion = "MAEUBICACION";
    protected static String TableDatos     = "MAEDATOS";
    protected static String TableAuditoria = "MAEAUDITORIA";

    // se agrega columna descripcion y fecha a tabla MAEDATOS
    private String SQLCrearProductos = "CREATE TABLE " + TableProductos +  " (id_iden INTEGER PRIMARY KEY AUTOINCREMENT , codigo VARCHAR(1000), descripcion VARCHAR(1000), ubicacion VARCHAR(1000) ) ";
    private String SQLCrearUbicacion = "CREATE TABLE " + TableUbicacion +  " (id_iden INTEGER PRIMARY KEY AUTOINCREMENT , codigo VARCHAR(1000), descripcion VARCHAR(1000) ) ";
    private String SQLCrearDatos     = "CREATE TABLE " + TableDatos     +  " (id_iden INTEGER PRIMARY KEY AUTOINCREMENT , ubicacion VARCHAR(1000), codigo VARCHAR(1000), descripcion VARCHAR(1000), cantidad VARCHAR(1000), fecha VARCHAR(1000) ) ";
    private String SQLCrearAuditoria = "CREATE TABLE " + TableAuditoria +  " (id_iden INTEGER PRIMARY KEY AUTOINCREMENT , codigo VARCHAR(1000), descripcion VARCHAR(1000), ubicacion VARCHAR(1000), stock VARCHAR(1000) ,unidad_medida VARCHAR(1000), fisico VARCHAR(1000) ) ";
                                                                                                 //ubicacion, codigo,descripcion, unidad_medida, stock, fisico

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQLCrearProductos);
        sqLiteDatabase.execSQL(SQLCrearUbicacion);
        sqLiteDatabase.execSQL(SQLCrearDatos);
        sqLiteDatabase.execSQL(SQLCrearAuditoria);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
