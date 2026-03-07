package clases.persistencia;

import clases.Agencia;

import  java.io.*;

public class SaveData {

    public static void save(String file, Agencia p) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(p);
            System.out.println("Agencia saved on: " + file);
        } catch (Exception e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }
}
