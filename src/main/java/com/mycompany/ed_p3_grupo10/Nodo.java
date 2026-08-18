/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ed_p3_grupo10;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author gianl
 */
public class Nodo {
    private Tablero tablero; // El estado del juego en este punto
    private List<Nodo> hijos; // Las siguientes jugadas posibles
    private int utilidad;     // El puntaje calculado por Minimax
    
    // Constructor
    public Nodo(Tablero tablero) {
        this.tablero = tablero;
        this.hijos = new ArrayList<>();
        this.utilidad = 0;
    }
    
    // --- Getters y Setters ---

    public Tablero getTablero() {
        return tablero;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public List<Nodo> getHijos() {
        return hijos;
    }

    // Método para conectar un nuevo estado (hijo) a este nodo
    public void agregarHijo(Nodo hijo) {
        this.hijos.add(hijo);
    }

    public int getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(int utilidad) {
        this.utilidad = utilidad;
    }
}
