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
import android.widget.EditText;
import android.widget.TextView;


import com.example.camilodesarrollo.abicap.R;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class EtiquetadoInicial extends AppCompatActivity {

    private String ubicacion;
    private EditText edUbicacion;
    private String ipImpresora, puertoImpresora;
    private TextView tvIPImpresora,tvPuertoImpresora;
    private Button btnMenuPrincipal, btnImprimir;
    private ProgressDialog p;
    public Beeper b;
    private Socket socket;
    public verificarMiscelaneos verMis;
    public IpComunicacion ipImp;
    String TAG = "TGsocket";
    private boolean sdDesponible, sdAccesoEscritura;
    private String parametro1,parametro2,parametro3, parametro4;
    private String textoEnviar, txtvalue;
    ArrayList<Producto> Lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etiquetado_inicial);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        btnImprimir = (Button)findViewById(R.id.btnImprimir);
        btnMenuPrincipal = (Button)findViewById(R.id.btnMenuPrincipal);

        edUbicacion = (EditText)findViewById(R.id.edUbicacion);
        tvIPImpresora = (TextView)findViewById(R.id.tvIPImpresora);
        tvPuertoImpresora = (TextView)findViewById(R.id.tvPuertoImpresora );
        //-----------------------------------------------------------------------------------------------
        //DECLARO VARIABLES GLOBALES
        //---------------------------------------------------------------------------------------

        b = new Beeper();
        verMis = new verificarMiscelaneos();
        ipImp  = new IpComunicacion(); //de aca sale la ip de impresora y el puerto
        //---------------------------------------------------------------------------------------

        verificarEstado();
        ipImpresora = ipImp.obtenerIpImpresora();
        puertoImpresora = ipImp.obtenerPuertoImpresora();

        tvIPImpresora.setText(ipImpresora);
        tvPuertoImpresora.setText(puertoImpresora);

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

        btnImprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    //LO QUE DEBE HACER EL BOTON IMPRIMIR

                    //la ip sacarla de un archivo de texto.... junto al puerto para que quede guardada configuracion....
                    // IP = edIP.getText().toString();
                    // edPortNum = Integer.parseInt(edPort.getText().toString());
                    EnviarData ed = new EnviarData();
                    ed.execute();
                }catch (Exception e){
                    mensaje("3.- "+e,"Excepción","Aceptar");
                }
            }
        });



    }

    public void verificarEstado() {
        String estado = Environment.getExternalStorageState();
        if (estado.equals(Environment.MEDIA_MOUNTED)) {
            sdDesponible = true;
            sdAccesoEscritura = true;
        } else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            sdDesponible = true;
            sdAccesoEscritura = false;
        } else {
            sdDesponible = false;
            sdAccesoEscritura = false;
        }
    }

    public class EnviarData extends AsyncTask<String,Void,Void> {
        private Boolean respuesta;
        @Override
        protected Void doInBackground(String... strings) {
            try {
                Integer conver = 0;
                Integer numIncial=0;
                try{
                 //   conver = Integer.parseInt(edCantEti.getText().toString().trim());
                 //   numIncial = Integer.parseInt(edNumInicio.getText().toString().trim());
                }catch (Exception ex){

                }


                ubicacion = edUbicacion.getText().toString().trim();
                ArrayList<Producto> listadoFinal = obtenerListadoCantidad(ubicacion);

                for (int i = 0; i < listadoFinal.size() ; i++) {
                    parametro1 = listadoFinal.get(i).getCodigo();
                    parametro2 = listadoFinal.get(i).getDescripcion();
                    parametro3 = listadoFinal.get(i).getUbicacion();
                    parametro4 = "1";

                    String textoEtiqueta = remplazar();
                    imprimir(textoEtiqueta);
                }

                respuesta = true;
            } catch (Exception ex) {
                ex.printStackTrace();
                respuesta = false;
            }
            return null;
        }


        //desaparece componente grafico
        @Override
        protected  void onPostExecute(Void result){
            p.dismiss();
            if (socket != null){
                try {
                    Log.i(TAG, "socket closed");
                    socket.close();
                } catch (IOException e) {
                    Log.e(TAG, "at thread finally IO " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        //crea componente grafico
        @Override
        protected void onPreExecute(){
            p = ProgressDialog.show(EtiquetadoInicial.this, "PROCESANDO", "IMPRIMIENDO", false);
        }
    }
    public String remplazar(){
        if (sdAccesoEscritura && sdDesponible) {
            textoEnviar = "";
            File ruta_sd = Environment.getExternalStoragePublicDirectory("/AbitekConfi/");//Environment.DIRECTORY_DOWNLOADS);
            File f = new File(ruta_sd.getAbsolutePath(), "arranque.txt");
            if (f.exists()) {
                try {
                    BufferedReader bufLec = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                    String tex;
                    while (!(tex = (bufLec.readLine()).trim()).equals(null)) {
                        tex = tex.replace("@parametro1", parametro1);
                        tex = tex.replace("@parametro2", parametro2);
                        tex = tex.replace("@parametro3", parametro3);
                        tex = tex.replace("@parametro4", parametro4);
                        Log.e("leyendo archivo", tex);
                        textoEnviar = textoEnviar + tex + "\n";
                    }
                    bufLec.close();
                    //imprimir()
                } catch (Exception ex) {
                    Log.e("Fichero sd", "Error al escribir en la sd");
                }

            } else {
                mensaje("Compruebe la existencia de la etiqueta","Advertencia","Aceptar");
            }
        }
        return textoEnviar;
    }

    public void imprimir (String textoEtiqueta){
        try {
            // InetAddress serverAddr = InetAddress.getByName(IP)
            //Socket socket = new Socket(IP, edPortNum);
            socket = new Socket(ipImpresora, Integer.parseInt(puertoImpresora));
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            out.println(textoEtiqueta);
            textoEtiqueta = "";
            socket.close();
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    public void mensaje(String contenido, String titulo, String botonNom){
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(EtiquetadoInicial.this);
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
            mensaje("10.- "+e,"Excepción","Aceptar");
        }
    }

    public ArrayList<Producto> obtenerListadoCantidad(String ubic){
        List<String> listado = new ArrayList<String>();
//        listaDatos = (ListView)findViewById(R.id.listView);

        try{
            Lista = new ArrayList<Producto>();

            SqlHelper sqlHel = new SqlHelper(EtiquetadoInicial.this);
            SQLiteDatabase db = sqlHel.getReadableDatabase();

            String total = "";
            Float SumaCantidades=0.f;

            String sql = "SELECT * FROM MAEPRODUCTOS WHERE UBICACION = '"+ubic+"'";
            Cursor c = db.rawQuery(sql,null);
            Integer contador = 0;
            while (c.moveToNext()){
                contador++;

                String descripcion = c.getString(c.getColumnIndex("descripcion"));
                String codigo = c.getString(c.getColumnIndex("codigo"));
                //String cantidad = c.getString(c.getColumnIndex("cantidad"));
                String ubicacion = c.getString(c.getColumnIndex("ubicacion"));


                //en siguiente IF aseguramos que no exista un null para que no se caiga.....
                if ((descripcion.equalsIgnoreCase("")) || (descripcion.equals(null))|| (ubicacion.equals(null))){
                    descripcion="SIN DESCRIPCION";
                }

                //Lista.add(new Producto(contador, descripcion, codigo, cantidad));
                Lista.add(new Producto(contador, descripcion, codigo,ubicacion));
               }
            c.close();
            db.close();
        }catch (Exception e) {
            mensaje("1.- "+e,"Excepción","Aceptar");
        }
        return Lista;
    }

    @Override
    public boolean onKeyUp(int kyCode, KeyEvent event)
    {
        return false;
    }

}
