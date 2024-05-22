package com.example.rene.abicap;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.camilodesarrollo.abicap.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class GenerarArchivoAuditoria extends AppCompatActivity {

    private Button btnGenerarArchivoF,btnMenuPrincipalG;
    private ProgressBar pgb;
    private TextView tvProgeso,tvEstatico;
    private ArrayList<String> listaUbicacionFisica,ListaStock,ListaCodigo,ListaDescripcion,ListaUnidadMedia,ListaFisico;
    private boolean sdDesponible, sdAccesoEscritura;
    private ProgressDialog p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_archivo);
        verificarEstado();

        //CAPA GRAFICA
        //----------------------------------------------------------------------------------------
        btnGenerarArchivoF = (Button)findViewById(R.id.btnGenerarArchivoF);
        btnMenuPrincipalG = (Button)findViewById(R.id.btnMenuPrincipalG);

        pgb = (ProgressBar)findViewById(R.id.pgb);

        tvProgeso = (TextView)findViewById(R.id.tvProgeso);
        tvEstatico = (TextView)findViewById(R.id.tvEstatico);

        //----------------------------------------------------------------------------------------

        leerRegistros leer = new leerRegistros();
        leer.execute();

        btnMenuPrincipalG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnGenerarArchivoF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearArchivoAud crear = new crearArchivoAud();
                crear.execute();
            }
        });
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

    private class leerRegistros extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... params) {
            ListaCodigo = new ArrayList<String>();
            ListaDescripcion = new ArrayList<String>();
            listaUbicacionFisica = new ArrayList<String>();
            ListaStock = new ArrayList<String>();
            ListaUnidadMedia = new ArrayList<String>();
            ListaFisico = new ArrayList<String>();
            try{
                SqlHelper sqlHel = new SqlHelper(GenerarArchivoAuditoria.this);
                SQLiteDatabase db = sqlHel.getReadableDatabase();

                String sql = "SELECT * FROM MAEAUDITORIA ORDER BY UBICACION";
                Cursor c = db.rawQuery(sql,null);
                while (c.moveToNext()){
                    ListaCodigo.add(c.getString(c.getColumnIndex("codigo")));
                    ListaDescripcion.add(c.getString(c.getColumnIndex("descripcion")));
                    listaUbicacionFisica.add(c.getString(c.getColumnIndex("ubicacion")));
                    ListaStock.add(c.getString(c.getColumnIndex("stock")));
                    ListaUnidadMedia.add(c.getString(c.getColumnIndex("unidad_medida")));
                    ListaFisico.add(c.getString(c.getColumnIndex("fisico")));
                }
                c.close();
                db.close();
            }catch (Exception ex) {
                Log.e("Fichero sd", "Error");
            }

            return null;
        }

        @Override
        protected  void onPostExecute(Void result){
            tvEstatico.setText(Integer.toString(ListaCodigo.size()));
            p.dismiss();
        }

        @Override
        protected void onPreExecute(){
            p = ProgressDialog.show(GenerarArchivoAuditoria.this, "PROCESANDO", "LEYENDO REGISTROS", false);
        }

    }

    //CAMILO AQUI HAY QUE ENTRAR A PICAR

    private class crearArchivoAud extends AsyncTask<String,Integer,Void> {

        @Override
        protected Void doInBackground(String... params) {

            if (sdAccesoEscritura && sdDesponible) {
                String idCapt = new BuscarIdCapt().obtenerDato();
                try {
                    File ruta_sd = Environment.getExternalStorageDirectory();
                    File f = new File(ruta_sd.getAbsolutePath(), "Inventario"+idCapt+".csv");
                    if (f.exists()) {
                        f.delete();
                    }
                    OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(f));
                    //fout.append("UBICACION" + ";" + "CODIGO"+ ";" + "DESCRIPCION"+ ";" +"CANTIDAD" + "\n");
                    fout.append("CODIGO" + ";" + "DESCRIPCIoN"+ ";" + "UBICACION FISICA"+ ";" +"STOCK"+ ";" +"UNIDAD MEDIDA"+ ";" +"FISICO" + "\n");
                    for (int x=0;x<ListaCodigo.size();x++){
                        //String dato = ListaCodigo.get(x).trim();

                        fout.append(ListaCodigo.get(x) + ";" + ListaDescripcion.get(x)+ ";" + listaUbicacionFisica.get(x) + ";" + ListaStock.get(x)+ ";" + ListaUnidadMedia.get(x)+ ";" + ListaFisico.get(x) + "\n");
                        publishProgress(1+x);
                    }
                    fout.close();
                }catch(Exception e){
                    Log.e("Escribir","error al excribir en sd");
                }
            }else {

            }
            return null;
        }

        @Override
        protected  void onPostExecute(Void result){
            AlertDialog.Builder builder = new AlertDialog.Builder(GenerarArchivoAuditoria.this);
            builder.setMessage("Carga de datos lista")
                    .setTitle("Listo")
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }

        @Override
        protected void onPreExecute(){
            pgb.setMax(ListaCodigo.size());
            pgb.setProgress(0);
        }
        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
            pgb.setProgress(values[0]);
            tvProgeso.setText(Integer.toString(values[0]));
        }
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        return false;
    }
}
