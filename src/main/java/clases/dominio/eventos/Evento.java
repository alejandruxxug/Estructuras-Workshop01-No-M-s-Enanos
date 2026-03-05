package clases.dominio.eventos;
import clases.dominio.personas.Fotografo;
import clases.dominio.personas.Modelo;

import java.time.LocalDate;

// Clase abstracta que representa un evento dentro de la agencia.
// Sirve como clase base para Evento Publico y Evento Privado.
public abstract class Evento {

    // Atributos comunes a todos los eventos
    private String nombre;
    private LocalDate fecha;
    private Lugar lugar;
    private Modelo[] modelos;
    private Fotografo[] fotografos;

    // Constructor
    public Evento(String nombre, LocalDate fecha, Lugar lugar, Modelo[] modelos, Fotografo[] fotografos) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.lugar = lugar;
        this.modelos = modelos;
        this.fotografos = fotografos;
    }

    // Getters y Setters
    public Modelo[] getModelos() {
        return modelos; }

    public void setModelos(Modelo[] modelos) {
        this.modelos = modelos; }

    public Fotografo[] getFotografos() {
        return fotografos; }

    public void setFotografos(Fotografo[] fotografos) {
        this.fotografos = fotografos; }

    public Lugar getLugar() {
        return lugar; }

    public void setLugar(Lugar lugar) {
        this.lugar = lugar; }

    public LocalDate getFecha() {
        return fecha; }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha; }

    public String getNombre() {
        return nombre; }

    public void setNombre(String nombre) {
        this.nombre = nombre; }

    // Método abstracto que cada subclase debe implementar
    public abstract String mostrarDetalles();
    public abstract String tipoEvento();

    @Override
    public String toString() {
        return nombre + " (" + getFecha() + ")";
    }
}