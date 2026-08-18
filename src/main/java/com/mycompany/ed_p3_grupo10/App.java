package com.mycompany.ed_p3_grupo10;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
/**
 * JavaFX App
 */
public class App extends Application {

    private char jugadorHumano;
    private char jugadorPC;
    private boolean turnoHumano;
    private Tablero tableroLogico;
    private Button[][] botonesTablero;
    private Stage ventanaPrincipal;

    @Override
    public void start(Stage ventana) {
        this.ventanaPrincipal = ventana;
        mostrarMenuConfiguracion();
    }

    // --- PANTALLA 1: MENÚ DE CONFIGURACIÓN ---
    private void mostrarMenuConfiguracion() {
        VBox menuRaiz = new VBox(15);
        menuRaiz.setAlignment(Pos.CENTER);

        Label titulo = new Label("Tres en Raya - IA Minimax");
        titulo.setFont(new Font("Arial", 24));

        Label lblSimbolo = new Label("Elige tu símbolo:");
        ComboBox<String> comboSimbolo = new ComboBox<>();
        comboSimbolo.getItems().addAll("Jugar con X", "Jugar con O");
        comboSimbolo.setValue("Jugar con X");

        Label lblTurno = new Label("¿Quién inicia?");
        ComboBox<String> comboTurno = new ComboBox<>();
        comboTurno.getItems().addAll("Inicio yo (Humano)", "Inicia la Computadora (IA)");
        comboTurno.setValue("Inicio yo (Humano)");

        Button btnEmpezar = new Button("Empezar Juego");
        btnEmpezar.setStyle("-fx-font-size: 16px; -fx-base: #b6e7c9;");

        btnEmpezar.setOnAction(e -> {
            // Se asignan símbolos
            if (comboSimbolo.getValue().contains("X")) {
                jugadorHumano = 'X';
                jugadorPC = 'O';
            } else {
                jugadorHumano = 'O';
                jugadorPC = 'X';
            }
            // Revisamos quién inicia
            turnoHumano = comboTurno.getValue().contains("yo");
            
            iniciarJuego(); //Se llama al tablero
        });

        menuRaiz.getChildren().addAll(titulo, lblSimbolo, comboSimbolo, lblTurno, comboTurno, btnEmpezar);
        Scene escenaMenu = new Scene(menuRaiz, 400, 300);
        ventanaPrincipal.setScene(escenaMenu);
        ventanaPrincipal.setTitle("Grupo 10 - Tres en Raya");
        ventanaPrincipal.show();
    }

    // --- PANTALLA 2: EL TABLERO DE JUEGO ---
    private void iniciarJuego() {
        tableroLogico = new Tablero(); // Se crea un objeto 
        botonesTablero = new Button[3][3];

        GridPane grid = new GridPane(); // GridPane para hacer cuadrículas
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5); // Espacio horizontal entre botones
        grid.setVgap(5); // Espacio vertical

        // Se llena el 3x3 con botones vacíos
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button btn = new Button(" ");
                btn.setFont(new Font("Arial", 40));
                btn.setPrefSize(100, 100);
                
                int fila = i;
                int col = j;
                
                // Si la persona le da clic en un botón llamamos al método
                btn.setOnAction(e -> procesarJugadaHumano(fila, col));
                
                botonesTablero[i][j] = btn;
                grid.add(btn, j, i); //GridPane usa (columna, fila)
            }
        }

        VBox rootJuego = new VBox(20);
        rootJuego.setAlignment(Pos.CENTER);
        Label lblTitulo = new Label("¡Batalla Humano vs IA!");
        lblTitulo.setFont(new Font("Arial", 20));
        
        rootJuego.getChildren().addAll(lblTitulo, grid);
        
        Scene escenaJuego = new Scene(rootJuego, 400, 450);
        ventanaPrincipal.setScene(escenaJuego);

        // Si el usuario eligió que la PC inicie la hacemos jugar inmediatamente
        if (!turnoHumano) {
            hacerJugadaPC();
        }
    }

    // --- LÓGICA DE TURNOS ---
    private void procesarJugadaHumano(int fila, int col) {
        if (!turnoHumano) return; // Si aun no toca ignoramos el clic
        
        if (tableroLogico.getCasilla(fila, col) == ' ') {
            tableroLogico.hacerJugada(fila, col, jugadorHumano);
            actualizarBoton(fila, col, jugadorHumano);
            
            if (revisarFinJuego(jugadorHumano)) return;

            // Le pasa el turno a la compu
            turnoHumano = false;
            hacerJugadaPC();
        }
    }

    private void hacerJugadaPC() {
        // Se conecta la IA
        Tablero mejorTablero = Minimax.obtenerMejorJugada(tableroLogico, jugadorPC, jugadorHumano);
        
        // Se compara el tablero viejo con el nuevo que devolvió Minimax
        // para saber dónde puso la ficha y poder pintar ese botón en la pantalla
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tableroLogico.getCasilla(i, j) != mejorTablero.getCasilla(i, j)) {
                    tableroLogico.hacerJugada(i, j, jugadorPC);
                    actualizarBoton(i, j, jugadorPC);
                    break;
                }
            }
        }
        
        if (revisarFinJuego(jugadorPC)) return;
        
        turnoHumano = true; // Se devuelve el turno al humano
    }

    // Pinta el botón de rojo(X) o azul(O) y lo desactiva para no volver a presionarlo
    private void actualizarBoton(int fila, int col, char simbolo) {
        botonesTablero[fila][col].setText(String.valueOf(simbolo));
        botonesTablero[fila][col].setDisable(true); 
        if(simbolo == 'X') {
            botonesTablero[fila][col].setStyle("-fx-text-fill: red; -fx-opacity: 1;");
        } else {
            botonesTablero[fila][col].setStyle("-fx-text-fill: blue; -fx-opacity: 1;");
        }
    }

    // Revisa si alguien ya ganó o si hubo empate porque se llenó el tablero
    private boolean revisarFinJuego(char jugadorActual) {
        if (tableroLogico.esGanador(jugadorActual)) {
            
            // --- EXTRA: Pintar línea de verde ---
            int[][] linea = tableroLogico.obtenerLineaGanadora(jugadorActual);
            if (linea != null) {
                for (int[] coordenada : linea) {
                    int fila = coordenada[0];
                    int col = coordenada[1];
                    // Le pone fondo verde claro a las casillas ganadoras
                    botonesTablero[fila][col].setStyle("-fx-background-color: #90EE90; -fx-text-fill: black; -fx-opacity: 1; -fx-font-weight: bold;");
                }
            }
            // --------------------------------------------------

            String ganador = (jugadorActual == jugadorHumano) ? "¡Tú ganas (Humano)!" : "¡La Computadora (IA) gana!";
            mostrarAlerta("¡Fin del Juego!", ganador);
            return true;
        }
        if (tableroLogico.estaLleno()) {
            mostrarAlerta("¡Fin del Juego!", "¡Es un empate!");
            return true;
        }
        return false;
    }

    // Muestra un mensaje emergente y reinicia al menú
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait(); // Pausa el juego hasta que se de ok
        
        mostrarMenuConfiguracion(); //Se vuelve a la pantalla inicial para jugar de nuevo
    }

    public static void main(String[] args) {
        launch();
    }
}