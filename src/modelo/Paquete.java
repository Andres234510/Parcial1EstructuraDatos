package modelo;

import java.util.Objects;

/**
 * Este es el modelo para los paquetes
 * Paquete del centro de distribución.
 * Orden natural (Comparable): por código ascendente.
 * Igualdad (equals/hashCode): por código, que es el identificador único.
 */
public class Paquete implements Comparable<Paquete> {

    // NOTA: el enunciado dice 0-10 en la descripción del atributo y 1-5 en las reglas.
    public static final int PRIORIDAD_MINIMA = 1;
    public static final int PRIORIDAD_MAXIMA = 5;
    public static final int PRIORIDAD_POR_DEFECTO = 5;

    private final String codigo;
    private final String destino;
    private final double peso;       // kg
    private final int prioridad;
    private final int tiempoEstimado; // minutos

    public Paquete(String codigo, String destino, double peso, int prioridad, int tiempoEstimado) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código no puede ser nulo ni vacío");
        }
        if (destino == null || destino.isBlank()) {
            throw new IllegalArgumentException("El destino no puede ser nulo ni vacío");
        }
        if (peso <= 0) {
            throw new IllegalArgumentException("El peso debe ser mayor que cero");
        }
        if (prioridad < PRIORIDAD_MINIMA || prioridad > PRIORIDAD_MAXIMA) {
            throw new IllegalArgumentException("La prioridad debe estar entre "
                    + PRIORIDAD_MINIMA + " y " + PRIORIDAD_MAXIMA);
        }
        if (tiempoEstimado <= 0) {
            throw new IllegalArgumentException("El tiempo estimado debe ser mayor que cero");
        }
        this.codigo = codigo;
        this.destino = destino;
        this.peso = peso;
        this.prioridad = prioridad;
        this.tiempoEstimado = tiempoEstimado;
    }

    /** Crea un paquete con la prioridad por defecto (5). */
    public Paquete(String codigo, String destino, double peso, int tiempoEstimado) {
        this(codigo, destino, peso, PRIORIDAD_POR_DEFECTO, tiempoEstimado);
    }

    public String getCodigo() { return codigo; }
    public String getDestino() { return destino; }
    public double getPeso() { return peso; }
    public int getPrioridad() { return prioridad; }
    public int getTiempoEstimado() { return tiempoEstimado; }

    @Override
    public int compareTo(Paquete otro) {
        return this.codigo.compareTo(otro.codigo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paquete)) return false;
        return codigo.equals(((Paquete) o).codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return "Paquete{" + codigo + ", " + destino + ", " + peso + "kg, prio=" + prioridad
                + ", " + tiempoEstimado + "min}";
    }
}