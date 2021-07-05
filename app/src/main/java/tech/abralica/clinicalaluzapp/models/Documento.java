package tech.abralica.clinicalaluzapp.models;

import tech.abralica.clinicalaluzapp.models.enums.TipoDocEnum;

public class Documento {

    private int id;
    private String codigo;

    public  Documento(){

    }
    public Documento(int id, String codigo) {
        this.id = id;
        this.codigo = codigo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
