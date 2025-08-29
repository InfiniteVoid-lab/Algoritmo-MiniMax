package AlgoritmoMiniMax;

import java.util.ArrayList;
import java.util.List;

public class MinimaxGatos {
    private static final int PROFUNDIDAD_MAX = 8;

    public static int[] mejorMovimiento(int[][] tablero, int[] posRaton) {
        int mejorValor = Integer.MIN_VALUE;
        int[] mejorMov = null;
        for (int[] mov : obtenerMovimientosGatos(tablero)) {
            int[][] nuevoTablero = copiarTablero(tablero);
            nuevoTablero[mov[0]][mov[1]] = 2;
            nuevoTablero[mov[2]][mov[3]] = 0;
            int valor = minimax(nuevoTablero, 0, false, Integer.MIN_VALUE, Integer.MAX_VALUE, posRaton);
            if (valor > mejorValor) {
                mejorValor = valor;
                mejorMov = mov;
            }
        }
        return mejorMov;
    }

	private static int minimax(int[][] tablero, int profundidad, boolean esMax, int alfa, int beta, int[] posRaton) {
		if (profundidad == PROFUNDIDAD_MAX || juegoTerminado(tablero, posRaton)) {
			return evaluarTablero(tablero, posRaton);
		}
		if (esMax) {
			// Turno de los gatos: buscamos maximizar el score.
			int maxEval = Integer.MIN_VALUE;
			for (int[] mov : obtenerMovimientosGatos(tablero)) {
				int[][] nuevoTablero = copiarTablero(tablero);
				// Simular movimiento del gato
				nuevoTablero[mov[0]][mov[1]] = 2;
				nuevoTablero[mov[2]][mov[3]] = 0;
				int eval = minimax(nuevoTablero, profundidad + 1, false, alfa, beta, posRaton);
				maxEval = Math.max(maxEval, eval);
				alfa = Math.max(alfa, eval);
				if (beta <= alfa)
					break; // Poda: no es necesario seguir
			}
			return maxEval;
		} else {
			// Turno del ratón: buscamos minimizar el score.
			int minEval = Integer.MAX_VALUE;
			for (int[] mov : obtenerMovimientosRaton(tablero, posRaton)) {
				int[][] nuevoTablero = copiarTablero(tablero);
				// Simular movimiento del ratón
				nuevoTablero[mov[0]][mov[1]] = 1;
				nuevoTablero[mov[2]][mov[3]] = 0;
				int[] nuevaPosRaton = { mov[0], mov[1] };
				int eval = minimax(nuevoTablero, profundidad + 1, true, alfa, beta, nuevaPosRaton);
				minEval = Math.min(minEval, eval);
				beta = Math.min(beta, eval);
				if (beta <= alfa)
					break; // Poda: descartar rama
			}
			return minEval;
		}
	}

    private static int evaluarTablero(int[][] tablero, int[] posRaton) {
        // Inicialización: rf y rc son la fila y columna actuales del ratón.
        int score = 0, rf = posRaton[0], rc = posRaton[1];
        
        // Estado de fin de juego: si el ratón alcanza la última fila,
        // se considera que ha ganado, lo cual es muy desfavorable para los gatos.
        
        if (rf == 7) return Integer.MIN_VALUE + 10000;
        
        // Penalización por movilidad y avance vertical:
        // Se obtiene la cantidad de movimientos disponibles para el ratón.
        
        int movRaton = obtenerMovimientosRaton(tablero, posRaton).size();
        
        // Se penaliza el estado restando puntos en función del número de movimientos
        
        score -= movRaton * 1000 + rf * 5000;
        
        // Se suma un bono si hay gatos en posiciones diagonales adyacentes.
        
        int bloqueadores = contarGatosAlrededor(tablero, posRaton);
        score += bloqueadores * 10000;
        
        // Bono por cercanía mínima:
        
        // Se calcula la distancia mínima entre el ratón y cualquier gato.
        
        int minDist = Integer.MAX_VALUE;
        for (int f = 0; f < tablero.length; f++) {
            for (int c = 0; c < tablero[0].length; c++) {
                if (tablero[f][c] == 2) {
                    // Se calcula la distancia Manhattan entre el ratón y el gato.
                    minDist = Math.min(minDist, Math.abs(f - rf) + Math.abs(c - rc));
                }
            }
        }
        
        // Si se encontró una distancia, se bonifica en función de cuán cerca esté un gato.
        
        if (minDist != Integer.MAX_VALUE) score += (10 - minDist) * 1000;
        
        // Si el ratón tiene un camino libre hacia la última fila, se penaliza fuertemente.
        
        if (tieneRutaDeEscape(tablero, posRaton)) score -= 100000;
        
        // Si se detecta que un gato puede capturar al ratón en el siguiente movimiento,
        // se retorna un score muy alto, indicando victoria inmediata para los gatos.
        
        for (int[] mov : obtenerMovimientosGatos(tablero)) {
            if (mov[0] == rf && mov[1] == rc) return Integer.MAX_VALUE;
        }
        
        return score;
    }



    // DFS para detectar camino libre hasta la fila 7.
    private static boolean tieneRutaDeEscape(int[][] tablero, int[] posRaton) {
        boolean[][] visitado = new boolean[tablero.length][tablero[0].length];
        return dfs(tablero, posRaton, visitado);
    }

    private static boolean dfs(int[][] tablero, int[] pos, boolean[][] visitado) {
        int f = pos[0], c = pos[1];
        if (f == 7) return true;
        visitado[f][c] = true;
        for (int[] mov : obtenerMovimientosRaton(tablero, pos)) {
            if (!visitado[mov[0]][mov[1]] && dfs(tablero, new int[]{mov[0], mov[1]}, visitado))
                return true;
        }
        return false;
    }

    private static int[][] copiarTablero(int[][] tablero) {
        int[][] copia = new int[tablero.length][tablero[0].length];
        for (int i = 0; i < tablero.length; i++) {
            System.arraycopy(tablero[i], 0, copia[i], 0, tablero[i].length);
        }
        return copia;
    }

    public static List<int[]> obtenerMovimientosGatos(int[][] tablero) {
        List<int[]> movs = new ArrayList<>();
        for (int f = 0; f < tablero.length; f++) {
            for (int c = 0; c < tablero[0].length; c++) {
                if (tablero[f][c] == 2) {
                    // Los gatos solo se mueven en diagonal hacia adelante.
                    int[][] dirs = { {-1, -1}, {-1, 1} };
                    for (int[] d : dirs) {
                        int nf = f + d[0], nc = c + d[1];
                        if (esMovimientoValido(tablero, nf, nc)) {
                            movs.add(new int[]{nf, nc, f, c});
                        }
                    }
                }
            }
        }
        return movs;
    }

    public static List<int[]> obtenerMovimientosRaton(int[][] tablero, int[] posRaton) {
        List<int[]> movs = new ArrayList<>();
        int[][] dirs = { {-1, -1}, {-1, 1}, {1, -1}, {1, 1} };
        for (int[] d : dirs) {
            int nf = posRaton[0] + d[0], nc = posRaton[1] + d[1];
            if (esMovimientoValido(tablero, nf, nc)) {
                movs.add(new int[]{nf, nc, posRaton[0], posRaton[1]});
            }
        }
        return movs;
    }

    private static boolean esMovimientoValido(int[][] tablero, int f, int c) {
        return f >= 0 && f < tablero.length && c >= 0 && c < tablero[0].length && tablero[f][c] == 0;
    }

    private static int contarGatosAlrededor(int[][] tablero, int[] posRaton) {
        int f = posRaton[0], c = posRaton[1], cont = 0;
        int[][] dirs = { {-1, -1}, {-1, 1}, {1, -1}, {1, 1} };
        for (int[] d : dirs) {
            int nf = f + d[0], nc = c + d[1];
            if (nf >= 0 && nf < tablero.length && nc >= 0 && nc < tablero[0].length && tablero[nf][nc] == 2)
                cont++;
        }
        return cont;
    }
    
    public static boolean juegoTerminado(int[][] tablero, int[] posRaton) {
        // Si el raton ha alcanzado la ultima fila, el juego termina.
        if (posRaton[0] == 7) return true;
        
        // Se verifica si todos los gatos están en filas superiores al ratón.
        boolean todosGatosArriba = true;
        for (int f = 0; f < tablero.length; f++) {
            for (int c = 0; c < tablero[0].length; c++) {
                if (tablero[f][c] == 2 && f >= posRaton[0]) {
                    todosGatosArriba = false;
                }
            }
        }
        if (todosGatosArriba) return true;
        
        // Si el ratón no tiene movimientos válidos a sido bloqueado.
        return obtenerMovimientosRaton(tablero, posRaton).isEmpty();
    }
}