package tech.abralica.clinicalaluzapp.models;

public class Paciente extends Usuario {
    private String pais;
    private String ciudad;

    public Paciente() {
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
