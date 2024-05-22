package com.example.rene.abicap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camilodesarrollo.abicap.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class ActOnline extends AppCompatActivity {

    private TextView tvDatosConexion, tvRutEmpresa,tvNumeroSerial;
    private EditText edDatosConexion, edRutEmpresa;
    private Button btnSolicitarActivacion,btnMenu;
    private ProgressDialog p;
    private String ipglobal;
    //private boolean respuesta;
    private String respuestaWS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activar_online);
        verificarEstado();
        final String serialDispositivo = Build.SERIAL;
        ipglobal = obtenerDato(); //establece la ip del servidor para activar licencia
        if(ipglobal.equalsIgnoreCase("ERROR")){
            Intent i = getIntent();
            setResult(RESULT_CANCELED, i);
            finish();
        }
        //CAPA GRAFICA
        //-------------------------------------------------------------------
        tvDatosConexion = (TextView)findViewById(R.id.tvDatosConexion);
        tvRutEmpresa = (TextView)findViewById(R.id.tvRutEmpresa);
        tvNumeroSerial = (TextView)findViewById(R.id.tvNumeroSerial);
        edRutEmpresa = (EditText)findViewById(R.id.edRutEmpresa);
        edDatosConexion = (EditText)findViewById(R.id.edDatosConexion);
        btnSolicitarActivacion = (Button)findViewById(R.id.btnSolicitarActivacion);
        btnMenu = (Button)findViewById(R.id.btnMenu);
        //-------------------------------------------------------------------
        edDatosConexion.setText(ipglobal);
        edRutEmpresa.requestFocus();
        tvNumeroSerial.setText("Número de Serie : "+serialDispositivo);

        btnSolicitarActivacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Encriptador encriptador = new Encriptador();
                try {
                    encriptador.encriptar();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                //Activa por web service
                try{
                    InteraccionWebService interac = new InteraccionWebService();
                    interac.execute(encriptador.encriptadoA64, serialDispositivo, edRutEmpresa.getText().toString());
                }catch (Exception e){
                    Log.e(e.toString(), "Error");
                }
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
    public class InteraccionWebService extends AsyncTask<String,Void,Void> {

        private static final String metodo = "registrarLicencia";
        // Namespace definido en el servicio web
        private static final String namespace = "http://Servicio/";
        // namespace + metodo
        private static final String accionSoap = "http://Servicio/validadorLicencias/registrarLicenciaRequest";
        private String url = "http://"+edDatosConexion.getText().toString().trim()+"/ValidadorLicenciasAbicap/validadorLicencias";

        HttpTransportSE transporte;
        SoapObject request;
        SoapSerializationEnvelope sobre;
        SoapPrimitive resultado;

        @Override
        protected Void doInBackground(String... strings) {
            try {
                //respuesta = false;
                respuestaWS = "";
                request = new SoapObject(namespace, metodo);
                request.addProperty("arg0", strings[0]);
                request.addProperty("arg1", strings[1]);
                request.addProperty("arg2", strings[2]);
//                request.addProperty("arg1", strings[1]);
                sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                sobre.setOutputSoapObject(request);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                transporte = new HttpTransportSE(url);
                transporte.call(accionSoap, sobre);
                resultado = (SoapPrimitive) sobre.getResponse();
                //respuesta = Boolean.valueOf(resultado.toString());
                respuestaWS = resultado.toString();

            } catch (Exception e) {
                respuestaWS = "Error WebService";
            }
            return null;
        }


        //desaparece componente grafico
        @Override
        protected  void onPostExecute(Void result){

            if(respuestaWS.equals("OK")){
                forzarActivacion();
                Toast.makeText(ActOnline.this, "ABICAP ACTIVADO", Toast.LENGTH_SHORT).show();
            }else{
                p.dismiss();
                Intent i = getIntent();
                setResult(RESULT_CANCELED, i);
                finish();
                Toast.makeText(ActOnline.this, "ERROR EN ACTIVACIÓN", Toast.LENGTH_SHORT).show();
            }
        }
        //crea componente grafico
        @Override
        protected void onPreExecute(){
            p = ProgressDialog.show(ActOnline.this, "PROCESANDO", "ACTIVANDO LICENCIA", false);
        }
    }

    private boolean sdDesponible, sdAccesoEscritura;

    //metodo obtiene y retorna la IP desde archivo de texto
    public String obtenerDato(){
        verificarEstado();
        String respuesta = "ERROR";
        if (sdAccesoEscritura && sdDesponible) {
            File ruta_sd = Environment.getExternalStoragePublicDirectory("/AbitekConfi/");//Environment.DIRECTORY_DOWNLOADS);
            File f = new File(ruta_sd.getAbsolutePath(), "ipValidador.txt");
            if (f.exists()) {
                try {
                    BufferedReader bufLec =
                            new BufferedReader(new InputStreamReader(new FileInputStream(f)));

                    String tex ;
                    if(!(tex = (bufLec.readLine()).trim()).equals(null)) {
                        respuesta = tex.toString().trim();
                    }else{
                        respuesta = "ERROR";
                    }
                    bufLec.close();

                } catch (Exception ex) {
                    Log.e("Fichero sd", "Error al escribir en la sd");
                    respuesta = "ERROR";
                }

            } else {
                respuesta = "ERROR";
            }
        }
        return respuesta;
    }

    //verifica estado de lectura y escritura
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

    //metodo realiza insercion en base de datos, dando confirmacion a su validacion
    public void forzarActivacion(){
        try{
            SqlHelper sqlHel = new SqlHelper(ActOnline.this);
            SQLiteDatabase db = sqlHel.getWritableDatabase();
            if (db != null) {
                String SQL = "INSERT INTO MAEUBICACION (codigo,descripcion) VALUES ('VALIDADO','VALIDADO')";
                db.execSQL(SQL);
            }
            db.close();
            Intent i = new Intent(ActOnline.this ,MainActivity.class);
            startActivity(i);

            Toast.makeText(this, "ABICAP ACTIVADO", Toast.LENGTH_SHORT).show();
            finish();
        }catch (Exception ex){
            Log.e(ex.toString(), "Error");
        }
    }


}