package com.example.rene.abicap;

/**
 * Created by Miguel on 25/04/2018.
 */

public class Producto {
    private int Id;
    private String descripcion;
    private String codigo;
    private String cantidad;
    private String ubicacion;

    //este constructor es para el modulo que permite tener un resumen de los pistoleado por ubicacion (TotalUbicacion)
    public Producto(int id, String descripcion, String codigo, String cantidad, String ubicacion) {
        Id = id;
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.cantidad = cantidad;
        this.ubicacion = ubicacion;
    }

    //este constructor es para el modulo de imprsesion (EtiquetadoInicial)
    public Producto(Integer contador, String descripcion, String codigo, String ubicacion) {
        Id = contador;
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.ubicacion = ubicacion;
    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
