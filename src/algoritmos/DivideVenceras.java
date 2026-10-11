package algoritmos;

import modelo.Paquete;

import java.util.List;

/**
 * Estrategia Divide y Vencerás para encontrar el paquete de mayor peso.
 *
 * Etapas:
 *  - CASO BASE: un solo elemento (o ninguno).
 *  - DIVISIÓN: partir el rango en dos mitades.
 *  - SUBPROBLEMA IZQUIERDO: mayor peso en la mitad izquierda.
 *  - SUBPROBLEMA DERECHO: mayor peso en la mitad derecha.
 *  - COMBINACIÓN: comparar los dos resultados y quedarse con el mayor.
 *  - RESULTADO FINAL: el paquete de mayor peso de todo el rango.
 *
 * No basta con que sea recursivo: debe verse claramente la división
 * y la combinación. Por eso se separan los pasos en variables.
 *
 * Complejidad: O(n) en tiempo, O(log n) en espacio (pila de recursión).
 */
public class DivideVenceras {

    private DivideVenceras() { }

    /**
     * Encuentra el paquete de mayor peso entre las posiciones inicio y fin (inclusive).
     *
     * @param paquetes lista de paquetes
     * @param inicio   índice izquierdo (inclusive)
     * @param fin      índice derecho (inclusive)
     * @return el paquete con mayor peso en ese rango, o null si el rango es inválido
     */
    public static Paquete mayorPeso(List<Paquete> paquetes, int inicio, int fin) {
        // CASO BASE 1: rango inválido (lista vacía o mal llamada)
        if (inicio > fin || paquetes.isEmpty()) {
            return null;
        }

        // CASO BASE 2: un solo elemento → ese es el mayor de su rango
        if (inicio == fin) {
            return paquetes.get(inicio);
        }

        // ---- DIVISIÓN ----
        int medio = (inicio + fin) / 2;

        // ---- SUBPROBLEMA IZQUIERDO ----
        Paquete mayorIzquierda = mayorPeso(paquetes, inicio, medio);

        // ---- SUBPROBLEMA DERECHO ----
        Paquete mayorDerecha = mayorPeso(paquetes, medio + 1, fin);

        // ---- COMBINACIÓN ----
        if (mayorIzquierda.getPeso() >= mayorDerecha.getPeso()) {
            return mayorIzquierda;
        } else {
            return mayorDerecha;
        }
    }

    /** Sobrecarga cómoda: usa todo el rango de la lista. */
    public static Paquete mayorPeso(List<Paquete> paquetes) {
        return mayorPeso(paquetes, 0, paquetes.size() - 1);
    }
}