package clases.dominio.personas;

// Clase que representa a un fotografo de la agencia.
// Hereda de Persona.
public class Fotografo extends Persona {

    private String especialidad;
    private int aniosExperiencia;

    // Constructor
    public Fotografo(String nombre, String identificacion, String contacto,
                     String especialidad, int aniosExperiencia) {

        super(nombre, identificacion, contacto);

        this.especialidad = especialidad;
        setAniosExperiencia(aniosExperiencia); // validación
    }

    // Getter y Setter

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public int getAniosExperiencia() {
        return aniosExperiencia;
    }

    public void setAniosExperiencia(int aniosExperiencia) {
        if (aniosExperiencia < 0) {
            throw new IllegalArgumentException("Los años de experiencia no pueden ser negativos.");
        }
        this.aniosExperiencia = aniosExperiencia;
    }

    // Implementación del método abstracto
    @Override
    public String mostrarInformacion() {
        return "Fotografo: " + getNombre() +
                "\nID: " + getIdentificacion() +
                "\nContacto: " + getContacto() +
                "\nEspecialidad: " + especialidad +
                "\nAños de experiencia: " + aniosExperiencia;
    }
}