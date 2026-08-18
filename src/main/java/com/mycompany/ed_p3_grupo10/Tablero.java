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
public class Tablero {
    private char[][] matriz; // 'X', 'O' o ' '
    
    // Constructor: Crea un tablero vacío de 3x3
    public Tablero() {
        matriz = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matriz[i][j] = ' ';
            }
        }
    }
    
    // Constructor de copia: Útil para que la IA simule jugadas sin alterar el juego real
    public Tablero(Tablero otro) {
        matriz = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.matriz[i][j] = otro.matriz[i][j];
            }
        }
    }

    public char getCasilla(int fila, int col) {
        return matriz[fila][col];
    }

    // Coloca un símbolo ('X' u 'O') si la casilla está libre
    public boolean hacerJugada(int fila, int col, char simbolo) {
        if (fila >= 0 && fila < 3 && col >= 0 && col < 3 && matriz[fila][col] == ' ') {
            matriz[fila][col] = simbolo;
            return true;
        }
        return false;
    }

    // Comprueba si el tablero está lleno
    public boolean estaLleno() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (matriz[i][j] == ' ') return false;
            }
        }
        return true;
    }

    // Verifica si un jugador ha ganado
    public boolean esGanador(char simbolo) {
       
        for (int i = 0; i < 3; i++) {
            if ((matriz[i][0] == simbolo && matriz[i][1] == simbolo && matriz[i][2] == simbolo) ||
                (matriz[0][i] == simbolo && matriz[1][i] == simbolo && matriz[2][i] == simbolo)) {
                return true;
            }
        }
       
        if ((matriz[0][0] == simbolo && matriz[1][1] == simbolo && matriz[2][2] == simbolo) ||
            (matriz[0][2] == simbolo && matriz[1][1] == simbolo && matriz[2][0] == simbolo)) {
            return true;
        }
        return false;
    }
// Calcula cuántas líneas (filas, columnas o diagonales) aún puede ganar un jugador
    private int calcularLineasPosibles(char jugador) {
        int lineas = 0;
        char oponente = (jugador == 'X') ? 'O' : 'X';

        //Revisa las 3 filas
        for (int i = 0; i < 3; i++) {
            if (matriz[i][0] != oponente && matriz[i][1] != oponente && matriz[i][2] != oponente) {
                lineas++;
            }
        }

        //Revisa las 3 columnas
        for (int j = 0; j < 3; j++) {
            if (matriz[0][j] != oponente && matriz[1][j] != oponente && matriz[2][j] != oponente) {
                lineas++;
            }
        }

        //Revisa las 2 diagonales
        if (matriz[0][0] != oponente && matriz[1][1] != oponente && matriz[2][2] != oponente) {
            lineas++;
        }
        if (matriz[0][2] != oponente && matriz[1][1] != oponente && matriz[2][0] != oponente) {
            lineas++;
        }

        return lineas;
    }

    // Calcula la función de utilidad (u = P_jugador - P_oponente)
    public int calcularUtilidad(char jugador) {
        char oponente = (jugador == 'X') ? 'O' : 'X';
        
        // Si el jugador ya ganó en este tablero, le damos una utilidad altísima
        if (esGanador(jugador)) return 100;
        // Si el oponente ya ganó, la utilidad es malísima
        if (esGanador(oponente)) return -100;

        int pJugador = calcularLineasPosibles(jugador);
        int pOponente = calcularLineasPosibles(oponente);
        
        return pJugador - pOponente;
    }






}
