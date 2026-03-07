package clases;


import clases.dominio.eventos.Evento;
import clases.dominio.eventos.Lugar;
import clases.dominio.personas.Fotografo;
import clases.dominio.personas.Modelo;
import clases.dominio.personas.Persona;

public class Agencia {

    private Persona[] personas = new Persona[0];
    private Lugar[] lugares = new Lugar[0];
    private Evento[] eventos = new Evento[0];

    public boolean registrarModelo(Modelo modelo) {
        agregarPersona(modelo);
    }



}
