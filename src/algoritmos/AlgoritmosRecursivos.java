package algoritmos;

import modelo.Paquete;

import java.util.List;

/**
 * Algoritmos recursivos exigidos por el enunciado.
 *
 * Regla: NO se permite reemplazar estos métodos por ciclos for/while/do-while.
 *
 * Cada método debe poder analizarse:
 *  - caso base
 *  - caso recursivo
 *  - avance hacia el caso base
 *  - parámetros de cada llamada
 *  - orden de los retornos
 *  - valor que retorna cada llamada
 */
public class AlgoritmosRecursivos {

    private AlgoritmosRecursivos() { }

    // =====================================================================
    // 1. PESO TOTAL DE PAQUETES HACIA UN MUNICIPIO
    // =====================================================================

    /**
     * Suma recursivamente el peso de todos los paquetes de la lista.
     *
     * @param paquetes lista de paquetes (todos del mismo municipio)
     * @param indice   posición actual del recorrido
     * @return suma de los pesos desde 'indice' hasta el final
     */
    public static double pesoTotal(List<Paquete> paquetes, int indice) {
        // CASO BASE: ya no hay más elementos que sumar
        if (indice == paquetes.size()) {
            return 0.0;
        }
        // CASO RECURSIVO: peso actual + peso del resto
        double pesoActual = paquetes.get(indice).getPeso();
        double pesoResto = pesoTotal(paquetes, indice + 1);
        return pesoActual + pesoResto;
    }

    // =====================================================================
    // 2. CONTEO DE PAQUETES CON PRIORIDAD >= MÍNIMA
    // =====================================================================

    /**
     * Cuenta recursivamente cuántos paquetes tienen prioridad >= prioridadMinima.
     *
     * @param paquetes        lista de paquetes
     * @param prioridadMinima umbral de prioridad
     * @param indice          posición actual del recorrido
     * @return cantidad de paquetes que cumplen la condición desde 'indice'
     */
    public static int contarPorPrioridad(List<Paquete> paquetes,
                                         int prioridadMinima,
                                         int indice) {
        // CASO BASE: ya no hay más elementos que revisar
        if (indice == paquetes.size()) {
            return 0;
        }
        // CASO RECURSIVO: 1 si cumple + lo que cuente el resto
        int aporteActual = paquetes.get(indice).getPrioridad() >= prioridadMinima ? 1 : 0;
        int aporteResto = contarPorPrioridad(paquetes, prioridadMinima, indice + 1);
        return aporteActual + aporteResto;
    }

    // =====================================================================
    // 3. MÉTODOS DE APOYO (sobrecarga sin índice, más cómodos de usar)
    // =====================================================================

    /** Sobrecarga: inicia el recorrido en 0. */
    public static double pesoTotal(List<Paquete> paquetes) {
        return pesoTotal(paquetes, 0);
    }

    /** Sobrecarga: inicia el recorrido en 0. */
    public static int contarPorPrioridad(List<Paquete> paquetes, int prioridadMinima) {
        return contarPorPrioridad(paquetes, prioridadMinima, 0);
    }
}