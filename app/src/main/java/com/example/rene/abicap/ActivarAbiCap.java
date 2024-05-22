/*package com.example.camilodesarrollo.abicap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.kobjects.util.Strings;
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


public class ActivarAbiCap extends AppCompatActivity {

    private boolean respuesta;
    private EditText edCodigo,edDatCone, edEncriptado;
    private Button btnActivar,btnMenuAct;
    private ProgressDialog p;
    private String ipglobal;
    private TextView tvNumeroSerial2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activar_abi_cap);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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
        edDatCone = (EditText)findViewById(R.id.edDatCone);
        btnActivar = (Button)findViewById(R.id.btnActivar);
        btnMenuAct = (Button)findViewById(R.id.btnMenuAct);
        tvNumeroSerial2 = (TextView)findViewById(R.id.tvNumeroSerial);
        edEncriptado = (EditText) findViewById(R.id.edEncriptado);
        //-------------------------------------------------------------------
        edDatCone.setText(ipglobal);
//        edCodigo.requestFocus();




        tvNumeroSerial2.setText("Número de Serie : "+serialDispositivo);

        btnActivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Activa por web service
                InteraccionWebService interac = new InteraccionWebService();
                interac.execute(serialDispositivo);

                Encriptador encriptador = new Encriptador();
                try {
                    encriptador.encriptar();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                //Toast.makeText(ActivarAbiCap.this, "Encriptacion es: "+encriptador.serialEncriptado, Toast.LENGTH_SHORT).show();

                if(!edEncriptado.getText().toString().trim().equalsIgnoreCase(""))
                {
                    if(edEncriptado.getText().toString().trim().equals(encriptador.encriptadoA64)){
                        //activa el dispositivo por serialencriptada
                        forzarActivacion();

                    }else{
                        Toast.makeText(ActivarAbiCap.this, "Debes Ingresar una licencia valida", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        btnMenuAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }


    public class InteraccionWebService extends AsyncTask<String,Void,Void> {

        private static final String metodo = "registrarInteresV2";
        // Namespace definido en el servicio web
        private static final String namespace = "http://Servicio/";
        // namespace + metodo
        private static final String accionSoap = "http://Servicio/validadorLicencias/registrarInteresV2Request";
        private String url = "http://"+edDatCone.getText().toString().trim()+"/ValidadorLicenciasAbicap/validadorLicencias";

        HttpTransportSE transporte;
        SoapObject request;
        SoapSerializationEnvelope sobre;
        SoapPrimitive resultado;

        @Override
        protected Void doInBackground(String... strings) {
            try {
                respuesta = false;
                request = new SoapObject(namespace, metodo);
                request.addProperty("arg0", strings[0]);
//                request.addProperty("arg1", strings[1]);
                sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                sobre.setOutputSoapObject(request);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                transporte = new HttpTransportSE(url);
                transporte.call(accionSoap, sobre);
                resultado = (SoapPrimitive) sobre.getResponse();
                respuesta = Boolean.valueOf(resultado.toString());

            } catch (Exception e) {
                respuesta = false;
            }
            return null;
        }


        //desaparece componente grafico
        @Override
        protected  void onPostExecute(Void result){
            if(respuesta){
                try{
                    SqlHelper sqlHel = new SqlHelper(ActivarAbiCap.this);
                    SQLiteDatabase db = sqlHel.getWritableDatabase();
                    if (db != null) {
                        String SQL = "INSERT INTO MAEUBICACION (codigo,descripcion) VALUES ('VALIDADO','VALIDADO')";
                        db.execSQL(SQL);
                    }
                    db.close();
                    p.dismiss();
                    Intent i = getIntent();
                    setResult(RESULT_OK, i);
                    finish();
                }catch (Exception ex){
                    Log.e(ex.toString(), "Error");
                }
            }else{
                p.dismiss();
                Intent i = getIntent();
                setResult(RESULT_CANCELED, i);
                finish();
            }

        }

        //crea componente grafico
        @Override
        protected void onPreExecute(){
            p = ProgressDialog.show(ActivarAbiCap.this, "PROCESANDO", "ACTIVANDO LICENCIA", false);
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
            SqlHelper sqlHel = new SqlHelper(ActivarAbiCap.this);
            SQLiteDatabase db = sqlHel.getWritableDatabase();
            if (db != null) {
                String SQL = "INSERT INTO MAEUBICACION (codigo,descripcion) VALUES ('VALIDADO','VALIDADO')";
                db.execSQL(SQL);
            }
            db.close();
            Intent i = getIntent();
            setResult(RESULT_OK, i);
            Toast.makeText(this, "ABICAP ACTIVADO", Toast.LENGTH_SHORT).show();
            finish();
        }catch (Exception ex){
            Log.e(ex.toString(), "Error");
        }
    }
}
*/
package com.example.rene.abicap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.camilodesarrollo.abicap.R;


public class ActivarAbiCap extends AppCompatActivity {

    private EditText edDatCone, edEncriptado;
    private Button btnOnline, btnManual,btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activar_abi_cap);

        //CAPA GRAFICA
        //-------------------------------------------------------------------
        btnOnline = (Button)findViewById(R.id.btnOnline);
        btnManual = (Button)findViewById(R.id.btnManual);
        btnMenu = (Button)findViewById(R.id.btnMenu);
        //-------------------------------------------------------------------


        btnOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(ActivarAbiCap.this, ActOnline.class);
                    startActivity(i);
                }catch (Exception e){
                    //holi
                    Log.e("fallo","lala");
                }

            }
        });

        btnManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivarAbiCap.this ,ActivarAbiCap_Manual.class);
                startActivity(i);

            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

}