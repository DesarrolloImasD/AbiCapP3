package com.example.rene.abicap;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.camilodesarrollo.abicap.R;

public class BorrarInventario extends AppCompatActivity {

    private Button btnBorrarInventario,btnMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrar_inventario);

        btnBorrarInventario = (Button)findViewById(R.id.btnBorrarInventario);
        btnMenu = (Button)findViewById(R.id.btnMenu);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnBorrarInventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BorrarInventario.this);
                builder.setMessage("¿Esta seguro que desea eliminar los datos del inventario?")
                        .setTitle("Advertencia")
                        .setCancelable(false)
                        .setNegativeButton("Cancelar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                        .setPositiveButton("Continuar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        borrarInventario();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void borrarInventario(){
        try {
            SqlHelper sqlHel = new SqlHelper(this);
            SQLiteDatabase db = sqlHel.getWritableDatabase();
            if (db != null) {
                String SQL = "DELETE FROM MAEDATOS";
                db.execSQL(SQL);

                String SQL2 = "UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='MAEDATOS'";
                db.execSQL(SQL2);

                Toast.makeText(this, "Operacion Exitosa", Toast.LENGTH_SHORT)
                        .show();
            }
            db.close();
        }catch (Exception ex){
            Toast toast = Toast.makeText(getApplicationContext(), "ERROR CON LA BD AL LIMPIAR", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        return false;
    }
}
