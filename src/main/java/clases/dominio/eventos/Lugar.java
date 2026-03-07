package clases.dominio.eventos;
import enums.TipoLugar;
import excepciones.ValorInvalidoException;

import java.io.Serializable;

public class Lugar implements Serializable { // Clase que representa un lugar donde se pueden realizar eventos.

    private String nombre;
    private String direccion;
    private String ciudad;
    private int capacidadMaxima;
    private TipoLugar tipoLugar; // enum

    // Constructor
    public Lugar(String nombre, String direccion, String ciudad, int capacidadMaxima, TipoLugar tipoLugar) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.capacidadMaxima = capacidadMaxima;
        this.tipoLugar = tipoLugar;
    }

    // Getters y Setters

    public String getNombre() {
        return nombre; }

    public void setNombre(String nombre) {
        this.nombre = nombre; }

    public String getDireccion() {
        return direccion; }

    public void setDireccion(String direccion) {
        this.direccion = direccion; }

    public String getCiudad() {
        return ciudad; }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad; }

    public int getCapacidadMaxima() {
        return capacidadMaxima; }

    public void setCapacidadMaxima(int capacidadMaxima) {
        if (capacidadMaxima < 1) { // Validación para asegurar que la capacidad maxima sea valida.
            throw new ValorInvalidoException(
                    "La capacidad debe ser un número positivo.");
        }
        this.capacidadMaxima = capacidadMaxima;
    }

    public TipoLugar getTipoLugar() {
        return tipoLugar; }

    public void setTipoLugar(TipoLugar tipoLugar) {
        this.tipoLugar = tipoLugar; }

    @Override
    public String toString() {
        return nombre + ", " + ciudad + " (" + tipoLugar + ")";
    }
}