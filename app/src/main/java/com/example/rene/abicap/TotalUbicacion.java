package com.example.rene.abicap;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.camilodesarrollo.abicap.R;

import java.util.ArrayList;
import java.util.List;

public class TotalUbicacion extends AppCompatActivity {
    private GridView gvConteo;
    private Button btnMenuPrincipalCantidadUbi;
    private TextView tvTotal;
    private ListView listaDatos;
    ArrayList <Producto> Lista;
    private String ubicacionGlobal;
    public UbicacionDato ubiDat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_total_ubicacion);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN );

            //DECLARACION COMPONENTES GRAFICOS
            //--------------------------------------------------------------------------------------
            //       gvConteo = (GridView) findViewById(R.id.gvConteo);

            tvTotal = (TextView) findViewById(R.id.tvTotal);
            ubiDat = new UbicacionDato();

            ubicacionGlobal = ubiDat.obtenerDato();
            tvTotal.setText("Ubicación: "+ubicacionGlobal);

            listaDatos = (ListView) findViewById(R.id.listView);

            btnMenuPrincipalCantidadUbi = (Button)findViewById(R.id.btnMenuPrincipalCantidadUbi);
            //--------------------------------------------------------------------------------------
            String ubi = getIntent().getExtras().getString("codigoUbicacion");
            List<String> listadoFinal = obtenerListadoCantidad(ubi);

            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listadoFinal);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listadoFinal);




            Adaptador miadaptador = new Adaptador(getApplicationContext(), Lista);

            listaDatos.setAdapter(miadaptador);

            //       gvConteo.setAdapter(adapter);

            btnMenuPrincipalCantidadUbi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }catch (Exception e){
            mensaje("0.- "+e,"Excepción","Aceptar");
        }
    }



    public List<String> obtenerListadoCantidad(String ubic){
        List<String> listado = new ArrayList<String>();
//        listaDatos = (ListView)findViewById(R.id.listView);

        try{
            Lista = new ArrayList<Producto>();

            SqlHelper sqlHel = new SqlHelper(TotalUbicacion.this);
            SQLiteDatabase db = sqlHel.getReadableDatabase();

            String total = "";
            Float SumaCantidades=0.f;

            String sql = "SELECT * FROM MAEDATOS WHERE UBICACION = '"+ubic+"'";
            Cursor c = db.rawQuery(sql,null);
            Integer contador = 0;
            while (c.moveToNext()){
                contador++;
/*
                listado.add(c.getString(c.getColumnIndex("descripcion")));
                listado.add(c.getString(c.getColumnIndex("codigo")));
                listado.add(c.getString(c.getColumnIndex("cantidad")));
*/
                String descripcion = c.getString(c.getColumnIndex("descripcion"));
                String codigo = c.getString(c.getColumnIndex("codigo"));
                String cantidad = c.getString(c.getColumnIndex("cantidad"));
                Log.d("CANTIDAD", cantidad);
                //en siguiente IF aseguramos que no exista un null para que no se caiga.....
                if ((descripcion.equalsIgnoreCase("")) || (descripcion.equals(null))){
                    descripcion="SIN DESCRIPCION";
                }

                Lista.add(new Producto(contador, descripcion, codigo, cantidad, ubic));
                //SumaCantidades = SumaCantidades + (Integer.parseInt(c.getString(c.getColumnIndex("cantidad"))));
            }

            //total = "Total: "+SumaCantidades;
            // tvTotal.setText(total);
            c.close();
            db.close();
        }catch (Exception e) {
            mensaje("1.- "+e,"Excepción","Aceptar");
        }
        return listado;
    }


    @Override
    public boolean onKeyUp(int kyCode, KeyEvent event)
    {
        return false;
    }
    public void mensaje(String contenido, String titulo, String botonNom){
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(TotalUbicacion.this);
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
            mensaje("2.- "+e,"Excepción","Aceptar");
        }
    }
}