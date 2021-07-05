package tech.abralica.clinicalaluzapp.models;

import java.util.Date;

import tech.abralica.clinicalaluzapp.models.enums.EstadoCitaMedEnum;
import tech.abralica.clinicalaluzapp.models.enums.TipoCitaMedEnum;

public class CitaMedica {
    private int nroOperacion;
    private Paciente paciente;
    private Medico medico;
    private TipoCitaMedEnum tipo;
    private Date fecha;
    private Horario horario;
    private double precio;
    private EstadoCitaMedEnum estado;
}
