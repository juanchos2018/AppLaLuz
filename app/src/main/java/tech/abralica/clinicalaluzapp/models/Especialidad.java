package tech.abralica.clinicalaluzapp.models;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class Especialidad {
    private String idespecialidad;
    private String nombre;
    private String descripcion;
    private String urlImagen;

    public Especialidad() {
    }

    public Especialidad(String idespecialidad,String nombre, String descripcion, String urlImagen) {
        this.idespecialidad=idespecialidad;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.urlImagen = urlImagen;
    }

    public String getIdespecialidad() {
        return idespecialidad;
    }

    public void setIdespecialidad(String idespecialidad) {
        this.idespecialidad = idespecialidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }


    @NonNull
    @NotNull
    @Override
    public String toString() {
        return this.getNombre();
    }
}
