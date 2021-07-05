package tech.abralica.clinicalaluzapp.models;

public class ClsComentario {

    String key;
    String nombre;
    String mensaje;
    String foto;

    public ClsComentario(){

    }
    public ClsComentario(String key, String nombre, String mensaje, String foto) {
        this.key = key;
        this.nombre = nombre;
        this.mensaje = mensaje;
        this.foto = foto;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
