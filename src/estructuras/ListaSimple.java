package estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Lista simplemente enlazada genérica, sin usar colecciones de Java.
 * Mantiene referencias a la cabeza y a la cola, y un contador de tamaño.
 *
 * Complejidades: agregar O(1), eliminar O(n), obtener O(n),
 * tamanio O(1), estaVacia O(1), recorrido con iterador O(n) en total.
 */
public class ListaSimple<T> implements Coleccion<T> {

    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamanio;

    public ListaSimple() {
        this.cabeza = null;
        this.cola = null;
        this.tamanio = 0;
    }

    /** Agrega al final en O(1) gracias a la referencia a la cola. */
    @Override
    public void agregar(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);
        if (cabeza == null) {          // lista vacía
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.siguiente = nuevo;
            cola = nuevo;
        }
        tamanio++;
    }

    /** Elimina la primera aparición. Retorna false si el elemento no existe (o la lista está vacía). */
    @Override
    public boolean eliminar(T elemento) {
        Nodo<T> anterior = null;
        Nodo<T> actual = cabeza;

        while (actual != null && !Objects.equals(actual.valor, elemento)) {
            anterior = actual;
            actual = actual.siguiente;
        }
        if (actual == null) {          // no encontrado
            return false;
        }
        if (anterior == null) {        // eliminar el primero
            cabeza = actual.siguiente;
        } else {                       // intermedio o último
            anterior.siguiente = actual.siguiente;
        }
        if (actual == cola) {          // se eliminó el último: la cola retrocede
            cola = anterior;           // (queda null si la lista quedó vacía)
        }
        tamanio--;
        return true;
    }

    @Override
    public T obtener(int posicion) {
        if (posicion < 0 || posicion >= tamanio) {
            throw new IndexOutOfBoundsException("Posición " + posicion + " fuera de rango [0, " + tamanio + ")");
        }
        Nodo<T> actual = cabeza;
        for (int i = 0; i < posicion; i++) {
            actual = actual.siguiente;
        }
        return actual.valor;
    }

    @Override
    public int tamanio() {
        return tamanio;
    }

    @Override
    public boolean estaVacia() {
        return tamanio == 0;
    }

    /** Iterable: entrega un NUEVO iterador cada vez que se recorre la lista. */
    @Override
    public Iterator<T> iterator() {
        return new IteradorLista();
    }

    /** Iterator: recuerda por dónde va el recorrido (un solo recorrido por instancia). */
    private class IteradorLista implements Iterator<T> {

        private Nodo<T> actual = cabeza;

        @Override
        public boolean hasNext() {
            return actual != null;
        }

        @Override
        public T next() {
            if (actual == null) {
                throw new NoSuchElementException("No hay más elementos");
            }
            T valor = actual.valor;
            actual = actual.siguiente;
            return valor;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Nodo<T> n = cabeza; n != null; n = n.siguiente) {
            sb.append(n.valor);
            if (n.siguiente != null) sb.append(" -> ");
        }
        return sb.append("]").toString();
    }
}