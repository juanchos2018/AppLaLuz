package tech.abralica.clinicalaluzapp.models;

import tech.abralica.clinicalaluzapp.models.enums.TipoCodMedEnum;

public class CodigoMedico {
    private int id;
    private TipoCodMedEnum tipo;
    private int numero;

    public CodigoMedico(int id, TipoCodMedEnum tipo, int numero) {
        this.id = id;
        this.tipo = tipo;
        this.numero = numero;
    }
}
