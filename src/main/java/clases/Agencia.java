package clases;

import clases.dominio.eventos.Evento;
import clases.dominio.eventos.Lugar;
import clases.dominio.personas.Fotografo;
import clases.dominio.personas.Modelo;
import clases.dominio.personas.Persona;

import excepciones.EntidadDuplicadaException;

import java.io.Serializable;

public class Agencia implements Serializable {

    // Arreglos que almacenan toda la información de la agencia
    private Persona[] personas = new Persona[0];
    private Lugar[] lugares = new Lugar[0];
    private Evento[] eventos = new Evento[0];

    // ======================== REGISTRAR ========================

    public void registrarModelo(Modelo modelo) {
        // Verificar que no exista otra persona con la misma identificación
        for (Persona p : personas) {
            if (p != null && p.getIdentificacion().equals(modelo.getIdentificacion())) {
                throw new EntidadDuplicadaException(
                        "Ya existe una persona con la identificación: " + modelo.getIdentificacion());
            }
        }

        // Buscar un espacio vacío (null) en el arreglo
        for (int i = 0; i < personas.length; i++) {
            if (personas[i] == null) {
                personas[i] = modelo;
                return;
            }
        }

        // Si no hay espacio, agrandar el arreglo en 1 y colocar al final
        Persona[] newPersonas = new Persona[personas.length + 1];
        for (int i = 0; i < personas.length; i++) {
            newPersonas[i] = personas[i];
        }
        newPersonas[personas.length] = modelo;
        personas = newPersonas;
    }

    public void registrarFotografo(Fotografo fotografo) {
        for (Persona p : personas) {
            if (p != null && p.getIdentificacion().equals(fotografo.getIdentificacion())) {
                throw new EntidadDuplicadaException(
                        "Ya existe una persona con la identificación: " + fotografo.getIdentificacion());
            }
        }

        for (int i = 0; i < personas.length; i++) {
            if (personas[i] == null) {
                personas[i] = fotografo;
                return;
            }
        }

        Persona[] newPersonas = new Persona[personas.length + 1];
        for (int i = 0; i < personas.length; i++) {
            newPersonas[i] = personas[i];
        }
        newPersonas[personas.length] = fotografo;
        personas = newPersonas;
    }

    public void registrarLugar(Lugar lugar) {
        for (Lugar l : lugares) {
            if (l != null && l.getNombre().equalsIgnoreCase(lugar.getNombre())) {
                throw new EntidadDuplicadaException(
                        "Ya existe un lugar con el nombre: " + lugar.getNombre());
            }
        }

        for (int i = 0; i < lugares.length; i++) {
            if (lugares[i] == null) {
                lugares[i] = lugar;
                return;
            }
        }

        Lugar[] newLugares = new Lugar[lugares.length + 1];
        for (int i = 0; i < lugares.length; i++) {
            newLugares[i] = lugares[i];
        }
        newLugares[lugares.length] = lugar;
        lugares = newLugares;
    }

    public void registrarEvento(Evento evento) {
        for (Evento e : eventos) {
            if (e != null && e.getNombre().equalsIgnoreCase(evento.getNombre())) {
                throw new EntidadDuplicadaException(
                        "Ya existe un evento con el nombre: " + evento.getNombre());
            }
        }

        for (int i = 0; i < eventos.length; i++) {
            if (eventos[i] == null) {
                eventos[i] = evento;
                return;
            }
        }

        Evento[] newEventos = new Evento[eventos.length + 1];
        for (int i = 0; i < eventos.length; i++) {
            newEventos[i] = eventos[i];
        }
        newEventos[eventos.length] = evento;
        eventos = newEventos;
    }

    // ======================== ELIMINAR ========================

    public boolean eliminarModelo(String identificacion) {
        for (int i = 0; i < personas.length; i++) {
            if (personas[i] instanceof Modelo
                    && personas[i].getIdentificacion().equals(identificacion)) {
                personas[i] = null; // dejar el espacio libre para reutilizar
                return true;
            }
        }
        return false;
    }

    public boolean eliminarFotografo(String identificacion) {
        for (int i = 0; i < personas.length; i++) {
            if (personas[i] instanceof Fotografo
                    && personas[i].getIdentificacion().equals(identificacion)) {
                personas[i] = null;
                return true;
            }
        }
        return false;
    }

    public boolean eliminarLugar(String nombre) {
        for (int i = 0; i < lugares.length; i++) {
            if (lugares[i] != null && lugares[i].getNombre().equalsIgnoreCase(nombre)) {
                lugares[i] = null;
                return true;
            }
        }
        return false;
    }

    public boolean eliminarEvento(String nombre) {
        for (int i = 0; i < eventos.length; i++) {
            if (eventos[i] != null && eventos[i].getNombre().equalsIgnoreCase(nombre)) {
                eventos[i] = null;
                return true;
            }
        }
        return false;
    }

    // ======================== BUSCAR ========================

    public Modelo buscarModeloPorId(String identificacion) {
        for (Persona p : personas) {
            if (p instanceof Modelo && p.getIdentificacion().equals(identificacion)) {
                return (Modelo) p;
            }
        }
        return null;
    }

    public Modelo buscarModeloPorCodigo(String codigoModelo) {
        for (Persona p : personas) {
            if (p instanceof Modelo) {
                Modelo m = (Modelo) p;
                if (m.getCodigoModelo().equals(codigoModelo)) {
                    return m;
                }
            }
        }
        return null;
    }

    public Fotografo buscarFotografoPorId(String identificacion) {
        for (Persona p : personas) {
            if (p instanceof Fotografo && p.getIdentificacion().equals(identificacion)) {
                return (Fotografo) p;
            }
        }
        return null;
    }

    public Lugar buscarLugarPorNombre(String nombre) {
        for (Lugar l : lugares) {
            if (l != null && l.getNombre().equalsIgnoreCase(nombre)) {
                return l;
            }
        }
        return null;
    }

    public Evento buscarEventoPorNombre(String nombre) {
        for (Evento e : eventos) {
            if (e != null && e.getNombre().equalsIgnoreCase(nombre)) {
                return e;
            }
        }
        return null;
    }

    // ======================== ASIGNAR A EVENTO ========================

    public void asignarModeloAEvento(Modelo modelo, Evento evento) {
        Modelo[] modelosDelEvento = evento.getModelos();

        // Verificar que no esté ya asignado
        for (Modelo m : modelosDelEvento) {
            if (m != null && m.getIdentificacion().equals(modelo.getIdentificacion())) {
                throw new EntidadDuplicadaException("El modelo ya está asignado a este evento.");
            }
        }

        // Buscar espacio vacío en el arreglo del evento
        for (int i = 0; i < modelosDelEvento.length; i++) {
            if (modelosDelEvento[i] == null) {
                modelosDelEvento[i] = modelo;
                return;
            }
        }

        // Si no hay espacio, agrandar y asignar
        Modelo[] newModelos = new Modelo[modelosDelEvento.length + 1];
        for (int i = 0; i < modelosDelEvento.length; i++) {
            newModelos[i] = modelosDelEvento[i];
        }
        newModelos[modelosDelEvento.length] = modelo;
        evento.setModelos(newModelos);
    }

    public void asignarFotografoAEvento(Fotografo fotografo, Evento evento) {
        Fotografo[] fotografosDelEvento = evento.getFotografos();

        for (Fotografo f : fotografosDelEvento) {
            if (f != null && f.getIdentificacion().equals(fotografo.getIdentificacion())) {
                throw new EntidadDuplicadaException("El fotógrafo ya está asignado a este evento.");
            }
        }

        for (int i = 0; i < fotografosDelEvento.length; i++) {
            if (fotografosDelEvento[i] == null) {
                fotografosDelEvento[i] = fotografo;
                return;
            }
        }

        Fotografo[] newFotografos = new Fotografo[fotografosDelEvento.length + 1];
        for (int i = 0; i < fotografosDelEvento.length; i++) {
            newFotografos[i] = fotografosDelEvento[i];
        }
        newFotografos[fotografosDelEvento.length] = fotografo;
        evento.setFotografos(newFotografos);
    }

    // ======================== GETTERS ========================

    // Filtra solo los Modelos del arreglo de personas =
    public Modelo[] getModelos() {
        int count = 0;
        for (Persona p : personas) {
            if (p instanceof Modelo) count++;
        }

        Modelo[] modelos = new Modelo[count];
        int idx = 0;
        for (Persona p : personas) {
            if (p instanceof Modelo) {
                modelos[idx++] = (Modelo) p;
            }
        }
        return modelos;
    }

    // Filtra solo los Fotografos del arreglo de personas
    public Fotografo[] getFotografos() {
        int count = 0;
        for (Persona p : personas) {
            if (p instanceof Fotografo) count++;
        }

        Fotografo[] fotografos = new Fotografo[count];
        int idx = 0;
        for (Persona p : personas) {
            if (p instanceof Fotografo) {
                fotografos[idx++] = (Fotografo) p;
            }
        }
        return fotografos;
    }

    public Persona[] getPersonas() {
        return personas;
    }

    public Lugar[] getLugares() {
        return lugares;
    }

    public Evento[] getEventos() {
        return eventos;
    }
}
