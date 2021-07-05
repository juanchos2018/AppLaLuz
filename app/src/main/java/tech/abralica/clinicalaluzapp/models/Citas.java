package tech.abralica.clinicalaluzapp.models;

public class Citas {

    private String key;
    private String idPaciente;
    private String nombre;
    private String fecha;
    private String hora;
    private String idMedico;
    private String nombreMedico;
    private String especialidad;
    private  String estado;


    public  Citas(){

    }
    public Citas(String key,String idPaciente, String nombre, String fecha, String hora, String idMedico, String nombreMedico, String especialidad,String estado) {
        this.key=key;
        this.idPaciente = idPaciente;
        this.nombre = nombre;
        this.fecha = fecha;
        this.hora = hora;
        this.idMedico = idMedico;
        this.nombreMedico=nombreMedico;
        this.especialidad = especialidad;
        this.estado=estado;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(String idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getNombreMedico() {
        return nombreMedico;
    }

    public void setNombreMedico(String nombreMedico) {
        this.nombreMedico = nombreMedico;
    }

}
