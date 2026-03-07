package clases.dominio.personas;

import java.io.Serializable;

// Clase abstracta que representa una persona dentro de la agencia.
// Sirve como clase base para Modelo y Fotografo.
public abstract class Persona implements Serializable {

    // Atributos comunes a todas las personas
    private String nombre;
    private String identificacion;
    private String contacto;

    // Constructor
    public Persona(String nombre, String identificacion, String contacto) {
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.contacto = contacto;
    }

    // Getters y Setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    // Método abstracto que cada subclase debe implementar
    public abstract String mostrarInformacion();

    @Override
    public String toString() {
        return nombre + " — " + getClass().getSimpleName();
    }
}
