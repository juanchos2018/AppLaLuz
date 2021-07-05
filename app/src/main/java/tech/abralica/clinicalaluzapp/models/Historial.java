package tech.abralica.clinicalaluzapp.models;

public class Historial {

    private String keyhistorial;
    private String idPaciente;
    private String nombrepaciente;
    private String fecha;
    private String hora;
    private String idMedico;
    private String nombreMedico;
    private String idEspecialida;
    private  String especialidad;
    private  String estado;
    private  String fechafinalizacion;
    private String descripcion;

    public Historial(){

    }

    public Historial(String keyhistorial, String idPaciente, String nombrepaciente, String fecha, String hora, String idMedico, String nombreMedico, String idEspecialida, String especialidad, String estado, String fechafinalizacion, String descripcion) {
        this.keyhistorial = keyhistorial;
        this.idPaciente = idPaciente;
        this.nombrepaciente = nombrepaciente;
        this.fecha = fecha;
        this.hora = hora;
        this.idMedico = idMedico;
        this.nombreMedico = nombreMedico;
        this.idEspecialida = idEspecialida;
        this.especialidad = especialidad;
        this.estado = estado;
        this.fechafinalizacion = fechafinalizacion;
        this.descripcion = descripcion;
    }

    public String getKeyhistorial() {
        return keyhistorial;
    }

    public void setKeyhistorial(String keyhistorial) {
        this.keyhistorial = keyhistorial;
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

    public String getFechafinalizacion() {
        return fechafinalizacion;
    }

    public void setFechafinalizacion(String fechafinalizacion) {
        this.fechafinalizacion = fechafinalizacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
