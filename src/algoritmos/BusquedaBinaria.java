package algoritmos;

import modelo.Paquete;

import java.util.List;

/**
 * Búsqueda binaria sobre una lista de paquetes ordenada por código.
 *
 * PRECONDICIÓN: la lista debe estar ordenada por código ascendente.
 * Si no está ordenada, el algoritmo puede dar resultados incorrectos.
 *
 * Complejidad: O(log n) en tiempo, O(log n) en espacio por la recursión.
 * (También se podría implementar iterativa para O(1) en espacio.)
 */
public class BusquedaBinaria {

    private BusquedaBinaria() { }

    /**
     * Busca un paquete por código usando búsqueda binaria recursiva.
     *
     * @param paquetes lista ORDENADA por código ascendente
     * @param codigo   código buscado
     * @return el paquete encontrado, o null si no existe
     */
    public static Paquete buscar(List<Paquete> paquetes, String codigo) {
        return buscar(paquetes, codigo, 0, paquetes.size() - 1);
    }

    /**
     * Método recursivo de búsqueda binaria.
     *
     * @param paquetes lista ordenada
     * @param codigo   código buscado
     * @param inicio   índice izquierdo del rango actual
     * @param fin      índice derecho del rango actual
     * @return el paquete encontrado, o null
     */
    private static Paquete buscar(List<Paquete> paquetes, String codigo,
                                  int inicio, int fin) {
        // CASO BASE 1: rango vacío → no existe
        if (inicio > fin) {
            return null;
        }

        // Punto medio
        int medio = (inicio + fin) / 2;
        Paquete candidato = paquetes.get(medio);
        int comparacion = candidato.getCodigo().compareTo(codigo);

        // CASO BASE 2: encontrado
        if (comparacion == 0) {
            return candidato;
        }

        // CASO RECURSIVO: buscar en la mitad izquierda o derecha
        if (comparacion > 0) {
            // El código buscado es menor → va en la mitad izquierda
            return buscar(paquetes, codigo, inicio, medio - 1);
        } else {
            // El código buscado es mayor → va en la mitad derecha
            return buscar(paquetes, codigo, medio + 1, fin);
        }
    }

    /**
     * Versión iterativa (para comparar en la sustentación).
     * O(log n) en tiempo, O(1) en espacio.
     */
    public static Paquete buscarIterativo(List<Paquete> paquetes, String codigo) {
        int inicio = 0;
        int fin = paquetes.size() - 1;

        while (inicio <= fin) {
            int medio = (inicio + fin) / 2;
            Paquete candidato = paquetes.get(medio);
            int comparacion = candidato.getCodigo().compareTo(codigo);

            if (comparacion == 0) {
                return candidato;
            } else if (comparacion > 0) {
                fin = medio - 1;
            } else {
                inicio = medio + 1;
            }
        }
        return null;
    }
}