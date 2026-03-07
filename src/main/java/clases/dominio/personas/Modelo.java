package clases.dominio.personas;

import excepciones.EstaturaNoAceptadaException;

// Clase que representa a un modelo afiliado a la agencia.
// Hereda de Persona e incluye validación de estatura mínima.
public class Modelo extends Persona {

    private static final double estaturaMinima = 1.50;
    private static int counter = 0;
    private String codigoModelo;
    private double estatura;
    private String categoria;
    private boolean disponibilidad;

    // Constructor
    public Modelo(String nombre, String identificacion, String contacto,
                  double estatura,
                  String categoria, boolean disponibilidad) {

        super(nombre, identificacion, contacto);

        this.codigoModelo = "M-" + counter++; // Generamos un código único para cada modelo
        setEstatura(estatura); // usamos setter para validar
        this.categoria = categoria;
        this.disponibilidad = disponibilidad;
    }

    // Getter y Setter con validación
    public double getEstatura() {
        return estatura;
    }

    public void setEstatura(double estatura) {
        if (estatura < estaturaMinima) {
            throw new EstaturaNoAceptadaException(
                    "La estatura es inferior a la permitida por la agencia (minimo " + estaturaMinima + ".)."
            );
        }
        this.estatura = estatura;
    }

    public String getCodigoModelo() {
        return codigoModelo;
    }

    public void setCodigoModelo(String codigoModelo) {
        this.codigoModelo = codigoModelo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public boolean isDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    @Override
    public String mostrarInformacion() {
        return "Modelo: " + getNombre() +
                "\nID: " + getIdentificacion() +
                "\nContacto: " + getContacto() +
                "\nCodigo: " + codigoModelo +
                "\nEstatura: " + estatura +
                "\nCategoria: " + categoria +
                "\nDisponible: " + (disponibilidad ? "Si" : "No");
    }
}
