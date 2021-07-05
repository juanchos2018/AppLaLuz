package tech.abralica.clinicalaluzapp.models;

public class ClsCita {

    private String keycita;
    private String idPaciente;
    private String nombrepaciente;
    private String fecha;
    private String hora;
    private String idMedico;
    private String nombreMedico;
    private String idEspecialida;
    private  String especialidad;
    private  String estado;


    public ClsCita(){

    }
    public ClsCita(String keycita, String idPaciente, String nombrepaciente, String fecha, String hora, String idMedico, String nombreMedico, String idEspecialida, String especialidad, String estado) {
        this.keycita = keycita;
        this.idPaciente = idPaciente;
        this.nombrepaciente = nombrepaciente;
        this.fecha = fecha;
        this.hora = hora;
        this.idMedico = idMedico;
        this.nombreMedico = nombreMedico;
        this.idEspecialida = idEspecialida;
        this.especialidad = especialidad;
        this.estado = estado;
    }

    public String getKeycita() {
        return keycita;
    }

    public void setKeycita(String keycita) {
        this.keycita = keycita;
    }

    public String getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(String idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getNombrepaciente() {
        return nombrepaciente;
    }

    public void setNombrepaciente(String nombrepaciente) {
        this.nombrepaciente = nombrepaciente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(String idMedico) {
        this.idMedico = idMedico;
    }

    public String getNombreMedico() {
        return nombreMedico;
    }

    public void setNombreMedico(String nombreMedico) {
        this.nombreMedico = nombreMedico;
    }

    public String getIdEspecialida() {
        return idEspecialida;
    }

    public void setIdEspecialida(String idEspecialida) {
        this.idEspecialida = idEspecialida;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
