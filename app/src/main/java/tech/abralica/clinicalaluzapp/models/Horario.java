package tech.abralica.clinicalaluzapp.models;

import com.google.firebase.Timestamp;

public class Horario {


    private String idhorario;
    private String idespecialidad;
    private String horaInicio;
    private String horaFin;
    private String especialidad;


    public  Horario(){

    }
    public Horario(String idhorario, String idespecialidad, String horaInicio, String horaFin,String especialidad) {
        this.idhorario = idhorario;
        this.idespecialidad = idespecialidad;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.especialidad=especialidad;
    }

    public String getIdhorario() {
        return idhorario;
    }

    public void setIdhorario(String idhorario) {
        this.idhorario = idhorario;
    }

    public String getIdespecialidad() {
        return idespecialidad;
    }

    public void setIdespecialidad(String idespecialidad) {
        this.idespecialidad = idespecialidad;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}
