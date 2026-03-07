package clases.dominio.eventos;
import java.time.LocalDate;
import clases.dominio.personas.Fotografo;
import clases.dominio.personas.Modelo;
import excepciones.ValorInvalidoException;

// Hereda de Evento e incluye atributos específicos para eventos públicos, como capacidad y patrocinador.
public class EventoPublico extends Evento {

    private int capacidad;
    private String patrocinador;


    // Constructor
    public EventoPublico(String nombre, LocalDate fecha, Lugar lugar, Modelo[] modelos,
                         Fotografo[] fotografos, int capacidad, String patrocinador) {
        super(nombre, fecha, lugar, modelos, fotografos);
        this.capacidad = capacidad;
        this.patrocinador = patrocinador;
    }


    // Getters y Setters
    public int getCapacidad() {
        return capacidad; }

    public void setCapacidad(int capacidad) {
        if (capacidad < 1) { // Validación para asegurar que la capacidad sea valida.
            throw new ValorInvalidoException(
                    "La capacidad debe ser un número positivo."
            );
        }
        this.capacidad = capacidad;
    }

    public String getPatrocinador() {
        return patrocinador; }

    public void setPatrocinador(String patrocinador) {
        this.patrocinador = patrocinador; }


    @Override
    public String tipoEvento() {
        return "Evento Público";
    }

    @Override
    public String mostrarDetalles() {
        return "Evento Público: " + getNombre() +
                "\nLugar: " + getLugar() +
                "\nCapacidad: " + capacidad +
                "\nPatrocinador: " + patrocinador;
    }
}