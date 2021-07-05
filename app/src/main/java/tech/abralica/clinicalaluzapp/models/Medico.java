package tech.abralica.clinicalaluzapp.models;

import java.util.List;

public class Medico {


    private String idusuario;
    private String idespecialidad;
    private String nombres;
    private String apellidos;
    private  String fotoPerfil;
    private String email;

    public  Medico(){

    }

    public Medico(String idusuario, String idespecialidad, String nombres, String apellidos, String fotoPerfil, String email) {
        this.idusuario = idusuario;
        this.idespecialidad = idespecialidad;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fotoPerfil = fotoPerfil;
        this.email = email;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public String getIdespecialidad() {
        return idespecialidad;
    }

    public void setIdespecialidad(String idespecialidad) {
        this.idespecialidad = idespecialidad;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
