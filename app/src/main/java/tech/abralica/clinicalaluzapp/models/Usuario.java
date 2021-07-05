package tech.abralica.clinicalaluzapp.models;

public class Usuario extends Persona {
    private String email;
    private String contrasena;
    private String fotoPerfil;


    public Usuario(DocIdentidad docIdentidad, String nombres, String apellidos, String celular, String email, String contrasena, String fotoPerfil) {
        super(docIdentidad, nombres, apellidos, celular);
        this.email = email;
        this.contrasena = contrasena;
        this.fotoPerfil = fotoPerfil;
    }

    public Usuario() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
}
