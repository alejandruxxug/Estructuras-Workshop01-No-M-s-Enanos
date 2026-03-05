package clases.dominio.eventos;
import java.time.LocalDate;
import clases.dominio.personas.Fotografo;
import clases.dominio.personas.Modelo;

// Hereda de Evento e incluye atributos específicos para eventos privados.
public class EventoPrivado extends Evento {

    private String cliente;
    private int nivelDeConfidencialidad; // poner la restriccion de N a M en el setter

    // Constructor
    public EventoPrivado(String nombre, LocalDate fecha, Lugar lugar, Modelo[] modelos,
                         Fotografo[] fotografos, String cliente, int nivelDeConfidencialidad) {

        super(nombre, fecha, lugar, modelos, fotografos);
        this.cliente = cliente;
        this.nivelDeConfidencialidad = nivelDeConfidencialidad;
    }

    // Getters y Setters
    public int getNivelDeConfidencialidad() {
        return nivelDeConfidencialidad; }

    public void setNivelDeConfidencialidad(int nivelDeConfidencialidad) {
        this.nivelDeConfidencialidad = nivelDeConfidencialidad; }

    public String getCliente() {
        return cliente; }

    public void setCliente(String cliente) {
        this.cliente = cliente; }

    @Override
    public String tipoEvento() {
        return "Evento Privado";
    }

    @Override
    public String mostrarDetalles() {
        return "Evento Privado: " + getNombre() +
                "\nLugar: " + getLugar() +
                "\nCliente: " + cliente +
                "\nNivel de Confidencialidad: " + nivelDeConfidencialidad;
    }
}