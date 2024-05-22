package com.example.rene.abicap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.camilodesarrollo.abicap.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private Button btnMiscelaneos,btnPorBarrido, btnActivarLicencia,cargaMaestro,btnNuevaUbicacion,btnPorLotes,btnAbrirConf,btnRevision,btnSalidaMercaderia,btnSalidaPorLote,btnBorrarRegistro,btnBorrarInventarioMen,btnGenerarArchivoInventario,btnEditarCantidad,btnIngresarProducto,btnImprimir,btnAuditoria, btnGenerarArchivoAuditoria;
    private String lecturaRAW;
    private boolean sdDesponible, sdAccesoEscritura;
    private ProgressDialog p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);//1.9



        setContentView(R.layout.activity_main);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN );

        //CONTROLES GRAFICOS INICIALIZACION
        //--------------------------------------------------------------------------------------
        btnPorBarrido = (Button) findViewById(R.id.btnPorBarrido);
        cargaMaestro = (Button) findViewById(R.id.btnCargarMaestro);
        btnNuevaUbicacion = (Button)findViewById(R.id.btnNuevaUbicacion);
        btnPorLotes = (Button)findViewById(R.id.btnPorLotes);
        btnAbrirConf = (Button)findViewById(R.id.btnAbrirConf);
        btnRevision = (Button)findViewById(R.id.btnRevision);
        btnSalidaMercaderia = (Button)findViewById(R.id.btnSalidaMercaderia);
        btnSalidaPorLote = (Button) findViewById(R.id.btnSalidaPorLote);
        btnBorrarRegistro = (Button)findViewById(R.id.btnBorrarRegistro);
        btnBorrarInventarioMen = (Button)findViewById(R.id.btnBorrarInventarioMen);
        btnEditarCantidad = (Button)findViewById(R.id.btnCantidadFisica);
        btnActivarLicencia = (Button)findViewById(R.id.btnActivarLicencia);
        btnMiscelaneos = (Button)findViewById(R.id.btnMiscelaneos);
     //   btnConfigurarImpresora = (Button)findViewById(R.id.btnConfigurarImpresora);
        btnIngresarProducto = (Button)findViewById(R.id.btnIngreoProducto);
        btnImprimir = (Button)findViewById(R.id.btnImprimir);
        btnAuditoria = (Button)findViewById(R.id.btnAuditoria);
        btnGenerarArchivoInventario = (Button)findViewById(R.id.btnGenerarArchivoInventario);
        btnGenerarArchivoAuditoria = (Button)findViewById(R.id.btnGenerarArchivoAuditoria);


        //--------------------------------------------------------------------------------------

        CheckearPermisos c = new CheckearPermisos();
        c.checkear(this);

        CrearArchivosIniciales a = new CrearArchivosIniciales(this);
        a.execute();

        //VALIDAR LICENCIA
        //------------------------------------------------------------------------
        //Boolean licenciado = new ObtenerLicencia().obtenerLicencia(this);
        Boolean licenciado = true;
        if(licenciado){
            desbloquearInterfaz();
        }else{
            bloquearApp();
        }
        //------------------------------------------------------------------------

        btnPorBarrido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this ,LecturaBarrido.class);
                startActivity(i);
                }
            }
        );

        cargaMaestro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this ,Login.class);
                startActivityForResult(i,1);
            }
        });

        btnNuevaUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this ,Ubicacion.class);
                startActivity(i);
            }
        });
        btnPorLotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this ,LecturaLoteo.class);
                startActivity(i);
            }
        });
        btnAbrirConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this ,Login.class);
                startActivityForResult(i,2);

            }
        });

        btnRevision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this ,Revision.class);
                startActivity(i);
            }
        });

        btnSalidaMercaderia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this ,SalidaMercaderia.class);
                startActivity(i);
            }
        });
        btnSalidaPorLote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this ,SalidaMercaderiaPorLote.class);
                startActivity(i);
            }
        });

        btnBorrarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this ,BorrarRegistro.class);
                startActivity(i);
            }
        });

        btnBorrarInventarioMen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this ,Login.class);
                startActivityForResult(i,3);
            }
        });

        btnGenerarArchivoInventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this ,GenerarArchivo.class);
                startActivity(i);
            }
        });

        btnEditarCantidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this ,EditarCantidad.class);
                startActivity(i);
            }
        });

        btnActivarLicencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this ,ActivarAbiCap.class);
                startActivityForResult(i,4);
            }
        });
        btnMiscelaneos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this ,Login.class);
                startActivityForResult(i,5);
            }
        });
        /*
        btnConfigurarImpresora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this ,Login.class);
                startActivityForResult(i,6);
            }
        });
       */
        btnIngresarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this ,Ingresar_Producto.class);
                startActivity(i);
            }
        });

        btnImprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this ,OpcionesImprimir.class);
                startActivity(i);
            }
        });

        btnAuditoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this ,Auditoria.class);
                startActivity(i);
            }
        });

        btnGenerarArchivoAuditoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this ,GenerarArchivoAuditoria.class);
                startActivity(i);
            }
        });


    }

    public void crearArchivo() {
        verificarEstado();
        try {
            InputStream lec =
                    getResources().openRawResource(R.raw.conf);
            BufferedReader bufLec =
                    new BufferedReader(new InputStreamReader(lec));
            lecturaRAW = bufLec.readLine();
            lec.close();

        } catch (Exception ex) {
            Log.e("Ficheros", "Error al leer fichero desde recurso raw");
        }
    }

    private class cargarMaestro extends AsyncTask<String,Void,Void> {
    @Override
    protected Void doInBackground(String... params) {
        limpiarProductos();
        return null;
    }
        @Override
        protected  void onPostExecute(Void result){
           p.dismiss();
        }
        @Override
        protected void onPreExecute(){
            p = ProgressDialog.show(MainActivity.this, "PROCESANDO SOLICITUD", "PROCESANDO", false);
        }
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

    public void limpiarProductos(){
        SqlHelper sqlHel = new SqlHelper(this);
        SQLiteDatabase db = sqlHel.getWritableDatabase();
        if(db!= null){
                String SQL = "DELETE FROM MAEPRODUCTOS";
                db.execSQL(SQL);
            }
            db.close();
        }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Comprobamos si el resultado de la segunda actividad es "RESULT_CANCELED".
        if (resultCode == RESULT_OK && requestCode == 1) {
            Intent i = new Intent(MainActivity.this ,CargaMaestro.class);
            startActivity(i);
            Toast.makeText(this, "Login Correcto", Toast.LENGTH_SHORT).show();
        }
        if (resultCode == RESULT_OK && requestCode == 2) {
            Intent dialogIntent = new Intent(Settings.ACTION_SETTINGS);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(dialogIntent);
            Toast.makeText(this, "Login Correcto", Toast.LENGTH_SHORT).show();
        }
        if (resultCode == RESULT_OK && requestCode == 3) {
            Intent i = new Intent(MainActivity.this ,BorrarInventario.class);
            startActivity(i);
            Toast.makeText(this, "Login Correcto", Toast.LENGTH_SHORT).show();
        }
        if (resultCode == RESULT_OK && requestCode == 4) {
            Toast.makeText(this, "Activacion Lista", Toast.LENGTH_SHORT).show();
            desbloquearInterfaz();
        }
        if (resultCode == RESULT_CANCELED && requestCode == 4) {
            Toast.makeText(this, "NO SE PUDO OBTENER DATO IP", Toast.LENGTH_SHORT).show();
        }
        if (resultCode == RESULT_OK && requestCode == 5) {
            Intent i = new Intent(MainActivity.this ,Miscelaneos.class);
            startActivity(i);
            Toast.makeText(this, "Login Correcto", Toast.LENGTH_SHORT).show();
        }
        if (resultCode == RESULT_OK && requestCode == 6) {
            Intent i = new Intent(MainActivity.this ,ConfigurarImpresora.class);
            startActivity(i);
            Toast.makeText(this, "Login Correcto", Toast.LENGTH_SHORT).show();
        }
    }

    public void bloquearApp(){
        btnNuevaUbicacion.setVisibility(View.GONE);
        btnPorLotes.setVisibility(View.GONE);
        btnBorrarInventarioMen.setVisibility(View.GONE);
        btnBorrarRegistro.setVisibility(View.GONE);
        btnEditarCantidad.setVisibility(View.GONE);
        btnGenerarArchivoInventario.setVisibility(View.GONE);
        btnPorBarrido.setVisibility(View.GONE);
        btnRevision.setVisibility(View.GONE);
        // btnConfigurarImpresora.setVisibility(View.GONE);
        btnImprimir.setVisibility(View.GONE);
        btnAuditoria.setVisibility(View.GONE);
        btnGenerarArchivoAuditoria.setVisibility(View.GONE);
        btnSalidaMercaderia.setVisibility(View.GONE);
        btnSalidaPorLote.setVisibility(View.GONE);
        cargaMaestro.setVisibility(View.GONE);
        btnMiscelaneos.setVisibility(View.GONE);
        btnIngresarProducto.setVisibility(View.GONE);
        btnActivarLicencia.setVisibility(View.VISIBLE);
    }
    public void desbloquearInterfaz(){
        btnNuevaUbicacion.setVisibility(View.VISIBLE);
        btnPorLotes.setVisibility(View.VISIBLE);
        btnBorrarInventarioMen.setVisibility(View.VISIBLE);
        btnBorrarRegistro.setVisibility(View.VISIBLE);
        btnEditarCantidad.setVisibility(View.VISIBLE);
        btnGenerarArchivoInventario.setVisibility(View.VISIBLE);
        btnPorBarrido.setVisibility(View.VISIBLE);
        btnRevision.setVisibility(View.VISIBLE);
        //btnConfigurarImpresora.setVisibility(View.VISIBLE);
        btnImprimir.setVisibility(View.VISIBLE);
        btnAuditoria.setVisibility(View.VISIBLE);
        btnGenerarArchivoAuditoria.setVisibility(View.VISIBLE);
        btnSalidaMercaderia.setVisibility(View.VISIBLE);
        btnSalidaPorLote.setVisibility(View.VISIBLE);
        cargaMaestro.setVisibility(View.VISIBLE);
        btnMiscelaneos.setVisibility(View.VISIBLE);
        btnIngresarProducto.setVisibility(View.VISIBLE);
        btnActivarLicencia.setVisibility(View.GONE);
    }
}
