package comparadores;

import modelo.Paquete;

import java.util.Comparator;

/**
 * Criterios alternativos de ordenamiento, independientes del orden natural
 * de Paquete (código ascendente, definido por Comparable).
 * Son reutilizables en sort, PriorityQueue, TreeSet, etc.
 */
public final class ComparadoresPaquete {

    private ComparadoresPaquete() { }

    public static final Comparator<Paquete> POR_PRIORIDAD_DESC =
            Comparator.comparingInt(Paquete::getPrioridad).reversed();

    public static final Comparator<Paquete> POR_PESO_DESC =
            Comparator.comparingDouble(Paquete::getPeso).reversed();

    public static final Comparator<Paquete> POR_TIEMPO_ASC =
            Comparator.comparingInt(Paquete::getTiempoEstimado);

    /**
     * Despacho prioritario: mayor prioridad, luego menor tiempo estimado,
     * y como último desempate, código alfabético ascendente.
     */
    public static final Comparator<Paquete> DESPACHO_PRIORITARIO =
            POR_PRIORIDAD_DESC
                    .thenComparing(POR_TIEMPO_ASC)
                    .thenComparing(Comparator.naturalOrder());
}