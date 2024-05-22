package com.example.rene.abicap;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.camilodesarrollo.abicap.R;

import java.util.List;

/**
 * Created by Miguel on 25/04/2018.
 */

public class Adaptador extends BaseAdapter{
    Context contexto;
    List<Producto> ListaObjetos;

    public Adaptador(Context contexto, List<Producto> listaObjetos){
        this.contexto = contexto;
        ListaObjetos = listaObjetos;
    }

    public int getCount(){
        return ListaObjetos.size();
    }

    public Object getItem(int position){
        return ListaObjetos.get(position);
    }

    public long getItemId(int position){
        return ListaObjetos.get(position).getId();
    }

    public View getView(int position , View convertView, ViewGroup parent){
        View vista = convertView;
        LayoutInflater inflate = LayoutInflater.from(contexto);

        vista = inflate.inflate(R.layout.itemlistview, null);

        TextView descripcion = (TextView) vista.findViewById(R.id.tvDescripcion);
        TextView codigo = (TextView) vista.findViewById(R.id.tvCodigo);
        TextView cantidad = (TextView) vista.findViewById(R.id.tvCantidad);


        //establezco color intercalado de cada producto en listView
        if((ListaObjetos.get(position).getId())% 2 == 1){
        //    vista.setBackgroundColor(Color.parseColor("#AF7AC5"));
            vista.setBackgroundColor(Color.parseColor("#DBA901"));
        }else{
           // vista.setBackgroundColor(Color.parseColor("#D5DBDB"));
        }

        descripcion.setText(ListaObjetos.get(position).getDescripcion().toString());
        codigo.setText(ListaObjetos.get(position).getCodigo().toString());
        cantidad.setText(ListaObjetos.get(position).getCantidad().toString());

        return vista;

    }


}
