package com.example.rene.abicap;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camilodesarrollo.abicap.R;

public class Miscelaneos extends AppCompatActivity {
    private CheckBox chkPorDuplicado,chkContraArchivo,chkMostrarDescripcion;
    private Button btnCambiarID, btnMenuPrincipalMiscelaneos;
    private TextView tvModelo;
    private EditText edIdCapturador;
    String modeloDispositivo = Build.MODEL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miscelaneos);
        //INTERFAZ GRAFICA
        //--------------------------------------------------------------------------------------------------
        chkPorDuplicado = (CheckBox)findViewById(R.id.chkPorDuplicado);
        chkContraArchivo = (CheckBox)findViewById(R.id.chkContraArchivo);
        chkMostrarDescripcion = (CheckBox)findViewById(R.id.chkMostrarDescripcion);

        btnCambiarID = (Button)findViewById(R.id.btnCambiarID);
        btnMenuPrincipalMiscelaneos = (Button)findViewById(R.id.btnMenuPrincipalMiscelaneos);

        edIdCapturador = (EditText)findViewById(R.id.edIdCapturador);
        tvModelo = (TextView)findViewById(R.id.tvModelo);
        //--------------------------------------------------------------------------------------------------

        tvModelo.setText("Modelo: "+modeloDispositivo);

        limpiarInterfaz();

        Boolean mostrarDes = new verificarCheck().checkearDescripcion(this);
        if(mostrarDes){
            chkMostrarDescripcion.setChecked(true);
        }
        Boolean contraArch = new verificarCheck().checkearContraArchivo(this);
        if(contraArch){
            chkContraArchivo.setChecked(true);
        }
        Boolean dupli = new verificarCheck().checkearDuplicado(this);
        if(dupli){
            chkPorDuplicado.setChecked(true);
        }

        edIdCapturador.setText(new verificarCheck().obtenerIdCapt(this));

        chkMostrarDescripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkMostrarDescripcion.isChecked()){
                   Boolean resp = new verificarCheck().sobreescribirCheckeo("true","mostrarDescripcion.txt");
                }else{
                   Boolean resp = new verificarCheck().sobreescribirCheckeo("false","mostrarDescripcion.txt");
                }
            }
        });

        chkContraArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkContraArchivo.isChecked()){
                    Boolean resp = new verificarCheck().sobreescribirCheckeo("true","validarContraArchivo.txt");
                }else{
                    Boolean resp = new verificarCheck().sobreescribirCheckeo("false","validarContraArchivo.txt");
                }
            }
        });

        chkPorDuplicado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkPorDuplicado.isChecked()){
                    Boolean resp = new verificarCheck().sobreescribirCheckeo("true","chequeoDuplicado.txt");
                }else{
                    Boolean resp = new verificarCheck().sobreescribirCheckeo("false","chequeoDuplicado.txt");
                }
            }
        });

        btnMenuPrincipalMiscelaneos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnCambiarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dato = edIdCapturador.getText().toString().trim();
                if(dato.equalsIgnoreCase("")){
                    Toast.makeText(Miscelaneos.this, "Favor Ingresar Algun Dato", Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Boolean respuesta = new verificarCheck().editarIdCapt(dato);
                    if(respuesta) {
                        Toast.makeText(Miscelaneos.this, "Grabado con Exito", Toast.LENGTH_SHORT)
                                .show();
                    }else{
                        Toast.makeText(Miscelaneos.this, "Problemas al Grabar", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }
        });
    }

    public void limpiarInterfaz(){
        chkMostrarDescripcion.setChecked(false);
        chkContraArchivo.setChecked(false);
        chkPorDuplicado.setChecked(false);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        return false;
    }
}
