package algoritmos;

import modelo.Paquete;

import java.util.List;

public class DivideVenceras {

    public static Paquete mayorPeso(List<Paquete> paquetes) {
        if (paquetes == null || paquetes.isEmpty()) {
            throw new IllegalArgumentException("La lista de paquetes esta vacia");
        }
        return mayorPeso(paquetes, 0, paquetes.size() - 1);
    }

    private static Paquete mayorPeso(List<Paquete> paquetes, int inicio, int fin) {
        // CASO BASE: un solo elemento, el es el mayor
        if (inicio == fin) {
            return paquetes.get(inicio);
        }

        // DIVISION: se parte el rango en dos mitades
        int medio = inicio + (fin - inicio) / 2;

        // SUBPROBLEMA IZQUIERDO
        Paquete izquierdo = mayorPeso(paquetes, inicio, medio);

        // SUBPROBLEMA DERECHO
        Paquete derecho = mayorPeso(paquetes, medio + 1, fin);

        // COMBINACION: se queda con el de mayor peso
        Paquete resultado = (izquierdo.getPeso() >= derecho.getPeso()) ? izquierdo : derecho;

        // RESULTADO de este rango
        return resultado;
    }
}
