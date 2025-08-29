package AlgoritmoMiniMax;

public class MovimientoRaton {
    private int ratonFila, ratonColumna;

    public MovimientoRaton(int filaInicial, int columnaInicial) {
        this.ratonFila = filaInicial;
        this.ratonColumna = columnaInicial;
    }

    public boolean moverRaton(int[][] tablero) {
        int[] mov = MinimaxGatos.mejorMovimiento(tablero, new int[]{ratonFila, ratonColumna});
        if (mov != null) {
            tablero[ratonFila][ratonColumna] = 0;
            ratonFila = mov[0];
            ratonColumna = mov[1];
            tablero[ratonFila][ratonColumna] = 1;
            return true;
        }
        return false;
    }

    public int getFila() { return ratonFila; }
    public int getColumna() { return ratonColumna; }
}
