package tech.abralica.clinicalaluzapp.models;

public class Administrador extends Usuario {


    public Administrador(int id, DocIdentidad docIdentidad, String nombres, String apellidos,
                         String celular, String email, String contrasena, String fotoPerfil) {
        super(docIdentidad, nombres, apellidos, celular, email, contrasena, fotoPerfil);
    }
}
