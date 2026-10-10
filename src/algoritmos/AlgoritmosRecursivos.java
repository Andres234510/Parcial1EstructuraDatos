package algoritmos;


import modelo.Paquete;

import java.util.List;

public class AlgoritmosRecursivos {

    // Peso total de los paquetes de un municipio
    public static double pesoTotalPorDestino(List<Paquete> paquetes, String destino) {
        return pesoTotalPorDestino(paquetes, destino, 0);
    }

    private static double pesoTotalPorDestino(List<Paquete> paquetes, String destino, int indice) {
        // Caso base: ya se revisaron todos los paquetes
        if (indice == paquetes.size()) {
            return 0;
        }

        // Caso recursivo: aporte del paquete actual + resultado del resto
        Paquete actual = paquetes.get(indice);
        double aporte = actual.getDestino().equalsIgnoreCase(destino) ? actual.getPeso() : 0;

        // Se avanza hacia el caso base aumentando el indice
        return aporte + pesoTotalPorDestino(paquetes, destino, indice + 1);
    }

    // Cantidad de paquetes con prioridad >= prioridadMinima
    public static int contarPorPrioridadMinima(List<Paquete> paquetes, int prioridadMinima) {
        return contarPorPrioridadMinima(paquetes, prioridadMinima, 0);
    }

    private static int contarPorPrioridadMinima(List<Paquete> paquetes, int prioridadMinima, int indice) {
        // Caso base
        if (indice == paquetes.size()) {
            return 0;
        }

        // Caso recursivo
        int cuenta = paquetes.get(indice).getPrioridad() >= prioridadMinima ? 1 : 0;

        return cuenta + contarPorPrioridadMinima(paquetes, prioridadMinima, indice + 1);
    }
}
