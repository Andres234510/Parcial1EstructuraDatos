package estructuras;

/**
 * Contrato común de las estructuras propias.
 * Define QUÉ operaciones existen; cada implementación decide CÓMO.
 * Extiende Iterable<T>: toda colección debe poder recorrerse con for-each.
 */
public interface Coleccion<T> extends Iterable<T> {

    void agregar(T elemento);

    boolean eliminar(T elemento);

    T obtener(int posicion);

    int tamanio();

    boolean estaVacia();
}