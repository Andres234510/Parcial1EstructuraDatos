
package servicio;

import modelo.Paquete;
import modelo.Repartidor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio que gestiona los repartidores.
 *
 * Responsabilidades:
 *  - CRUD de repartidores (identificación única).
 *  - Asignación de paquetes respetando las reglas:
 *      · El repartidor debe estar disponible.
 *      · La zona del repartidor debe coincidir con el destino del paquete.
 *
 * Estructura: HashMap<String, Repartidor> para búsqueda por ID en O(1).
 */
public class GestorRepartidores {

    private final Map<String, Repartidor> porIdentificacion = new HashMap<>();

    // ---------------- CRUD ----------------

    public void registrar(Repartidor r) {
        if (r == null) {
            throw new IllegalArgumentException("El repartidor no puede ser nulo");
        }
        if (porIdentificacion.containsKey(r.getIdentificacion())) {
            throw new IllegalArgumentException(
                    "Identificación duplicada: " + r.getIdentificacion());
        }
        porIdentificacion.put(r.getIdentificacion(), r);
    }

    public Repartidor consultar(String identificacion) {
        return porIdentificacion.get(identificacion);
    }

    public Collection<Repartidor> todos() {
        return porIdentificacion.values();
    }

    public List<Repartidor> disponibles() {
        List<Repartidor> resultado = new ArrayList<>();
        for (Repartidor r : porIdentificacion.values()) {
            if (r.isDisponible()) resultado.add(r);
        }
        return resultado;
    }

    public void cambiarDisponibilidad(String identificacion, boolean disponible) {
        Repartidor r = porIdentificacion.get(identificacion);
        if (r == null) {
            throw new IllegalArgumentException("Repartidor no encontrado: " + identificacion);
        }
        r.setDisponible(disponible);
    }

    public int tamanio() {
        return porIdentificacion.size();
    }

    // ---------------- ASIGNACIÓN ----------------

    /**
     * Asigna un paquete a un repartidor específico.
     * Reglas:
     *   1. El repartidor debe estar disponible.
     *   2. La zona del repartidor debe coincidir con el destino del paquete.
     */
    public boolean asignar(Paquete paquete, Repartidor repartidor) {
        if (paquete == null || repartidor == null) return false;
        if (!repartidor.isDisponible()) return false;
        if (!repartidor.getZona().equalsIgnoreCase(paquete.getDestino())) return false;
        repartidor.setDisponible(false);
        return true;
    }

    /**
     * Busca automáticamente un repartidor disponible cuya zona coincida
     * con el destino del paquete y le asigna la entrega.
     */
    public Repartidor asignarAutomatico(Paquete paquete) {
        if (paquete == null) return null;
        for (Repartidor r : porIdentificacion.values()) {
            if (r.isDisponible() && r.getZona().equalsIgnoreCase(paquete.getDestino())) {
                r.setDisponible(false);
                return r;
            }
        }
        return null;
    }

    public void liberar(String identificacion) {
        cambiarDisponibilidad(identificacion, true);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Repartidores:\n");
        for (Repartidor r : porIdentificacion.values()) {
            sb.append("  ").append(r).append("\n");
        }
        return sb.toString();
    }
}