package AlgoritmoMiniMax;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;

public class TableroAjedrez extends JFrame {
    private static final int FILAS = 8;
    private static final int COLUMNAS = 8;
    private static final int TAMANO_CASILLA = 100;

    private JPanel[][] casillas = new JPanel[FILAS][COLUMNAS];
    private int[][] tableroEstado;
    private int ratonFila, ratonColumna;

    private static final String RUTA_RATON = "C:\\Users\\Usuario\\Desktop\\Gatoraton\\raton.png";
    private static final String RUTA_GATO = "C:\\Users\\Usuario\\Desktop\\Gatoraton\\gato.png";

    public TableroAjedrez() {
        setTitle("Ratón y Gatos - Minimax");
        setSize(FILAS * TAMANO_CASILLA, COLUMNAS * TAMANO_CASILLA);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(FILAS, COLUMNAS));

        tableroEstado = new int[FILAS][COLUMNAS];

        int[][] posicionesGatos = { {7, 1}, {7, 3}, {7, 5}, {7, 7} };
        int[] posicionRaton = {0, 4};
        ratonFila = posicionRaton[0];
        ratonColumna = posicionRaton[1];
        tableroEstado[ratonFila][ratonColumna] = 1;

        for (int[] posGato : posicionesGatos) {
            tableroEstado[posGato[0]][posGato[1]] = 2;
        }

        inicializarTablero();
        setVisible(true);
    }

    private void inicializarTablero() {
        for (int fila = 0; fila < FILAS; fila++) {
            for (int columna = 0; columna < COLUMNAS; columna++) {
                JPanel casilla = new JPanel(new BorderLayout());
                casilla.setBackground((fila + columna) % 2 == 0 ? Color.WHITE : Color.BLACK);

                final int filaActual = fila;
                final int columnaActual = columna;
                casilla.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        manejarClic(filaActual, columnaActual);
                    }
                });
                casillas[fila][columna] = casilla;
                add(casilla);
            }
        }
        actualizarTablero();
    }

    private void manejarClic(int fila, int columna) {
        if (Math.abs(ratonFila - fila) == 1 && Math.abs(ratonColumna - columna) == 1 &&
            tableroEstado[fila][columna] == 0) {

            tableroEstado[ratonFila][ratonColumna] = 0;
            ratonFila = fila;
            ratonColumna = columna;
            tableroEstado[ratonFila][ratonColumna] = 1;

            if (estadoDelJuego(tableroEstado, new int[]{ratonFila, ratonColumna}) == 1) {
                JOptionPane.showMessageDialog(this, "¡El ratón ha escapado! Gana el ratón.");
                System.exit(0);
            }

            moverGatos();
            actualizarTablero();
        }
    }

    private void moverGatos() {
        int[] mejorMovimiento = MinimaxGatos.mejorMovimiento(tableroEstado, new int[]{ratonFila, ratonColumna});

        if (mejorMovimiento != null) {
            int filaNueva = mejorMovimiento[0];
            int columnaNueva = mejorMovimiento[1];
            int filaAnterior = mejorMovimiento[2];
            int columnaAnterior = mejorMovimiento[3];

            tableroEstado[filaAnterior][columnaAnterior] = 0;
            tableroEstado[filaNueva][columnaNueva] = 2;
        }

        int estado = estadoDelJuego(tableroEstado, new int[]{ratonFila, ratonColumna});
        if (estado == 1) { 
            JOptionPane.showMessageDialog(this, "¡El ratón ha escapado! Gana el ratón.");
            System.exit(0);
        } else if (estado == 2) {
            JOptionPane.showMessageDialog(this, "¡El ratón ha sido atrapado! Ganan los gatos.");
            System.exit(0);
        }

        actualizarTablero();
    }

    public static int estadoDelJuego(int[][] tablero, int[] posicionRaton) {
        int ratonFila = posicionRaton[0];

        // 1. El ratón gana si llega a la última fila (7)
        if (ratonFila == 7) return 1; 

        // 2. Verificar si todos los gatos están en filas superiores al ratón
        boolean todosGatosArriba = true;
        for (int fila = 0; fila < tablero.length; fila++) {
            for (int columna = 0; columna < tablero[0].length; columna++) {
                if (tablero[fila][columna] == 2) {
                    if (fila >= ratonFila) {
                        todosGatosArriba = false;
                    }
                }
            }
        }

        if (todosGatosArriba) return 1; // El ratón gana si todos los gatos están arriba

        // 3. Si el ratón no tiene movimientos válidos, pierde (ganan los gatos)
        if (obtenerMovimientosRaton(tablero, posicionRaton).isEmpty()) return 2;

        return 0; // El juego sigue
    }

    private static List<int[]> obtenerMovimientosRaton(int[][] tablero, int[] posicionRaton) {
        List<int[]> movimientos = new ArrayList<>();
        int[][] direcciones = { {-1, -1}, {-1, 1}, {1, -1}, {1, 1} };

        for (int[] dir : direcciones) {
            int nuevaFila = posicionRaton[0] + dir[0];
            int nuevaColumna = posicionRaton[1] + dir[1];

            if (esMovimientoValido(tablero, nuevaFila, nuevaColumna)) {
                movimientos.add(new int[]{nuevaFila, nuevaColumna, posicionRaton[0], posicionRaton[1]});
            }
        }
        return movimientos;
    }

    private static boolean esMovimientoValido(int[][] tablero, int fila, int columna) {
        return fila >= 0 && fila < tablero.length &&
               columna >= 0 && columna < tablero[0].length &&
               tablero[fila][columna] == 0;
    }

    private void actualizarTablero() {
        for (int fila = 0; fila < FILAS; fila++) {
            for (int columna = 0; columna < COLUMNAS; columna++) {
                casillas[fila][columna].removeAll();
                casillas[fila][columna].setBackground((fila + columna) % 2 == 0 ? Color.WHITE : Color.BLACK);

                if (tableroEstado[fila][columna] == 1) {
                    casillas[fila][columna].add(crearLabelImagen(RUTA_RATON));
                } else if (tableroEstado[fila][columna] == 2) {
                    casillas[fila][columna].add(crearLabelImagen(RUTA_GATO));
                }
                casillas[fila][columna].revalidate();
                casillas[fila][columna].repaint();
            }
        }
    }

    private JLabel crearLabelImagen(String ruta) {
        ImageIcon icono = new ImageIcon(ruta);
        Image img = icono.getImage().getScaledInstance(TAMANO_CASILLA, TAMANO_CASILLA, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(img));
    }
}