package estructuras;

/**
 * Nodo de la lista simplemente enlazada.
 * Es visible solo dentro del paquete 'estructuras': el código externo
 * no puede ver ni manipular las conexiones internas.
 */
class Nodo<T> {

    T valor;
    Nodo<T> siguiente;

    Nodo(T valor) {
        this.valor = valor;
        this.siguiente = null;
    }
}