package com.zentcode.parks.models;

public class Unidad {

    private Integer id;
    private String nombre;

    public Unidad() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Unidad{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}