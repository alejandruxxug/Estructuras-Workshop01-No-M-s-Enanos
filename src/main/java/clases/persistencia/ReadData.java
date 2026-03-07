package clases.persistencia;

import clases.Agencia;

import  java.io.*;

public class ReadData {

    public static Agencia read(String archivo) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            Agencia p = (Agencia) ois.readObject();
            System.out.println("Agencia read on: " + archivo);
            return p;
        } catch (Exception e) {
            System.out.println("Error reading: " + e.getMessage());
            return null;
        }
    }
}
