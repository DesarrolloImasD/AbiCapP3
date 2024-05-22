package com.example.rene.abicap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.camilodesarrollo.abicap.R;

public class OpcionesImprimir extends AppCompatActivity {

    private Button btnConfigurarImpresora,btnEtiquetadoInicial, btnReetiquetar,btnImprimirUbicacion,btnMenuPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_imprimir);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN );

        //CONTROLES GRAFICOS INICIALIZACION
        //--------------------------------------------------------------------------------------
        btnConfigurarImpresora = (Button) findViewById(R.id.btnConfigurarImpresora);
        btnEtiquetadoInicial = (Button) findViewById(R.id.btnEtiquetadoInicial);
        btnReetiquetar = (Button)findViewById(R.id.btnReetiquetar);
        btnImprimirUbicacion = (Button)findViewById(R.id.btnImprimirUbicacion);
        btnMenuPrincipal = (Button)findViewById(R.id.btnMenuPrincipal);


        //--------------------------------------------------------------------------------------


        btnConfigurarImpresora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OpcionesImprimir.this ,Login.class);
                startActivityForResult(i,1);
            }
        });

        btnEtiquetadoInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OpcionesImprimir.this ,EtiquetadoInicial.class);
                startActivity(i);
            }
        });

        btnReetiquetar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OpcionesImprimir.this ,ReImprimirEtiqueta.class);
                startActivity(i);
            }
        });

        btnImprimirUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OpcionesImprimir.this ,ImprimirUbicacion.class);
                startActivity(i);
            }
        });

        btnMenuPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OpcionesImprimir.this ,MainActivity.class);
                startActivity(i);
            }
        });





    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1) {
            Intent i = new Intent(OpcionesImprimir.this ,ConfigurarImpresora.class);
            startActivity(i);
            Toast.makeText(this, "Login Correcto", Toast.LENGTH_SHORT).show();
        }
    }
}
