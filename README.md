# 🐭🐱 Algoritmo Minimax – Juego Ratón y Gatos

[![Last commit](https://img.shields.io/github/last-commit/InfiniteVoid-lab/Algoritmo-MiniMax?logo=git&color=0EA5E9)](https://github.com/InfiniteVoid-lab/Algoritmo-MiniMax/commits)
[![Lenguaje](https://img.shields.io/github/languages/top/InfiniteVoid-lab/Algoritmo-MiniMax?logo=java&label=java&color=F59E0B)](./)
[![Issues](https://img.shields.io/github/issues/InfiniteVoid-lab/Algoritmo-MiniMax?color=22C55E)](https://github.com/InfiniteVoid-lab/Algoritmo-MiniMax/issues)
[![License](https://img.shields.io/badge/license-MIT-10B981.svg)](#licencia)

Este proyecto implementa el **algoritmo Minimax con poda alfa–beta** aplicado al clásico juego de **Ratón contra Gatos**, en un tablero similar al de ajedrez.

El objetivo es mostrar cómo la IA puede tomar decisiones óptimas en un juego de suma cero, donde el ratón intenta escapar y los gatos intentan atraparlo.

---

## ✨ Características

- ✅ Implementación en **Java** pura.  
- ⚡ **Minimax** con poda alfa–beta para optimizar la búsqueda.  
- 🎮 Juego **Ratón y Gatos** en tablero cuadriculado.  
- 🧩 Clases organizadas:
  - `TableroAjedrez.java`: representación del tablero.  
  - `Raton_y_gatos.java`: lógica del juego principal.  
  - `MovimientoRaton.java`: movimientos válidos del ratón.  
  - `MinimaxGatos.java`: implementación del algoritmo para los gatos.  
- 📊 Evaluación heurística para decidir jugadas.

---

## 🧠 ¿Cómo funciona el juego?

- **Ratón**: intenta llegar a la parte opuesta del tablero sin ser atrapado.  
- **Gatos**: intentan bloquearlo y acorralarlo.  
- En cada turno, el algoritmo **Minimax** calcula las jugadas óptimas para los gatos, evaluando:
  - Posición actual del ratón.  
  - Movimientos disponibles.  
  - Profundidad de búsqueda.  
  - Heurística definida en `MinimaxGatos`.  

---

## 📦 Requisitos

- **Java 17+** (funciona también en versiones anteriores, ajusta según tu entorno).  

---

## 🚀 Compilación y ejecución

### Compilar
```bash
javac *.java
