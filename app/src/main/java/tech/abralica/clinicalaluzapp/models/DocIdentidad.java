package tech.abralica.clinicalaluzapp.models;

import java.io.Serializable;

import tech.abralica.clinicalaluzapp.models.enums.TipoDocEnum;

public class DocIdentidad implements Serializable {
    private int id;
    private TipoDocEnum tipo;
    private String codigo;

    public DocIdentidad(){

    }


    public DocIdentidad(int id, TipoDocEnum tipo, String codigo) {
        this.id = id;
        this.tipo = tipo;
        this.codigo = codigo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoDocEnum getTipo() {
        return tipo;
    }

    public void setTipo(TipoDocEnum tipo) {
        this.tipo = tipo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
