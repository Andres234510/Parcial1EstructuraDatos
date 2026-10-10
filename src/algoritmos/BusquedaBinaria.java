package algoritmos;

import modelo.Paquete;

import java.util.List;

public class BusquedaBinaria {

    /**
     * @return posicion del paquete en la lista, o -1 si no existe.
     */
    public static int buscarPorCodigo(List<Paquete> ordenados, String codigo) {
        int inicio = 0;
        int fin = ordenados.size() - 1;

        while (inicio <= fin) {
            int medio = inicio + (fin - inicio) / 2;
            int comparacion = ordenados.get(medio).getCodigo().compareTo(codigo);

            if (comparacion == 0) {
                return medio;            // encontrado
            } else if (comparacion < 0) {
                inicio = medio + 1;      // descarta la mitad izquierda
            } else {
                fin = medio - 1;         // descarta la mitad derecha
            }
        }
        return -1;                       // no existe
    }
}
