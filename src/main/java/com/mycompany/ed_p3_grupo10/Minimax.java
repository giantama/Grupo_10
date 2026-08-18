/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ed_p3_grupo10;

/**
 *
 * @author gianl
 */
public class Minimax {

    // Método principal que recibe el tablero real y devuelve el tablero con la mejor jugada
    public static Tablero obtenerMejorJugada(Tablero actual, char pc, char humano) {
        //Se crea la raíz del árbol (el estado actual del juego)
        Nodo raiz = new Nodo(actual);
        Arbol arbol = new Arbol(raiz);
        
        //nivel 1: Genera todos los posibles movimientos de la PC (Maximizador)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (actual.getCasilla(i, j) == ' ') {
                    Tablero tableroHijo = new Tablero(actual); // se copia el tablero
                    tableroHijo.hacerJugada(i, j, pc);         // la compu simula jugar ahí
                    Nodo nodoHijo = new Nodo(tableroHijo);
                    raiz.agregarHijo(nodoHijo);
                    
                    //nivel 2: Generar todas las posibles respuestas del Humano (Minimizador)
                    for (int x = 0; x < 3; x++) {
                        for (int y = 0; y < 3; y++) {
                            if (tableroHijo.getCasilla(x, y) == ' ') {
                                Tablero tableroNieto = new Tablero(tableroHijo);
                                tableroNieto.hacerJugada(x, y, humano); // jugador simula responder
                                Nodo nodoNieto = new Nodo(tableroNieto);
                                
                                //Se evalua la utilidad en esta hoja del árbol
                                int utilidad = tableroNieto.calcularUtilidad(pc);
                                nodoNieto.setUtilidad(utilidad);
                                
                                nodoHijo.agregarHijo(nodoNieto); // Se conecta el nieto al hijo
                            }
                        }
                    }
                }
            }
        }
        
        // Si el tablero ya está lleno y no hay hijos, devolvemos el mismo
        if (raiz.getHijos().isEmpty()) {
            return actual;
        }
        
        // 4. APLICAR MINIMAX (Elegir el mejor camino)
        int mejorValorPc = Integer.MIN_VALUE;
        Nodo mejorMovimiento = null;
        
        for (Nodo hijo : raiz.getHijos()) {
            // El humano elegirá la jugada que MÁS perjudique a la PC (el valor MÍNIMO de los nietos)
            int peorValorParaPc = Integer.MAX_VALUE;
            
            if (hijo.getHijos().isEmpty()) {
                // Si el juego termina en el nivel 1 (ej. la PC gana directamente sin dar turno al humano)
                peorValorParaPc = hijo.getTablero().calcularUtilidad(pc);
            } else {
                for (Nodo nieto : hijo.getHijos()) {
                    if (nieto.getUtilidad() < peorValorParaPc) {
                        peorValorParaPc = nieto.getUtilidad();
                    }
                }
            }
            
            hijo.setUtilidad(peorValorParaPc);
            
            // La PC elegirá la jugada que MÁS le beneficie (el valor MÁXIMO entre los hijos)
            if (hijo.getUtilidad() > mejorValorPc) {
                mejorValorPc = hijo.getUtilidad();
                mejorMovimiento = hijo;
            }
        }
        
        return mejorMovimiento != null ? mejorMovimiento.getTablero() : actual;
    }
}
