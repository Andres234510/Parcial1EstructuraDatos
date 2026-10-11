package servicio;

import comparadores.ComparadoresPaquete;
import estructuras.ListaSimple;
import modelo.Paquete;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

/**
 * Servicio que gestiona los paquetes del centro de distribución.
 *
 * Estructuras usadas (Parte A.1):
 *  - HashMap<String, Paquete>      → consulta por código (O(1))
 *  - ArrayDeque<Paquete>           → orden de llegada (FIFO)
 *  - HashSet<String>               → municipios únicos
 *  - TreeSet<String>               → municipios ordenados alfabéticamente
 *  - Map<String, List<Paquete>>    → agrupación por municipio
 *  - PriorityQueue<Paquete>        → despacho prioritario
 *  - ListaSimple<Paquete>          → historial de entregas (propia)
 */
public class GestorPaquetes {

    private final Map<String, Paquete> porCodigo = new HashMap<>();
    private final Deque<Paquete> colaLlegada = new ArrayDeque<>();
    private final Set<String> municipiosUnicos = new HashSet<>();
    private final Set<String> municipiosOrdenados = new TreeSet<>();
    private final Map<String, List<Paquete>> porMunicipio = new HashMap<>();
    private final PriorityQueue<Paquete> colaPrioridad =
            new PriorityQueue<>(ComparadoresPaquete.DESPACHO_PRIORITARIO);
    private final ListaSimple<Paquete> historial = new ListaSimple<>();

    /** Registra un paquete. Valida que el código no exista previamente. */
    public void registrar(Paquete p) {
        if (p == null) {
            throw new IllegalArgumentException("El paquete no puede ser nulo");
        }
        if (porCodigo.containsKey(p.getCodigo())) {
            throw new IllegalArgumentException("Código duplicado: " + p.getCodigo());
        }
        porCodigo.put(p.getCodigo(), p);
        colaLlegada.addLast(p);
        colaPrioridad.add(p);
        municipiosUnicos.add(p.getDestino());
        municipiosOrdenados.add(p.getDestino());
        porMunicipio.computeIfAbsent(p.getDestino(), k -> new ArrayList<>()).add(p);
    }

    /** Consulta un paquete por su código. Retorna null si no existe. */
    public Paquete consultar(String codigo) {
        return porCodigo.get(codigo);
    }

    /** Atiende el siguiente paquete según orden de llegada (FIFO). */
    public Paquete atenderPorLlegada() {
        return colaLlegada.pollFirst();
    }

    /** Despacha el siguiente paquete según prioridad, tiempo y código. */
    public Paquete despacharPrioritario() {
        return colaPrioridad.poll();
    }

    /** Registra una entrega en el historial (lista propia). */
    public void registrarEntrega(Paquete p) {
        historial.agregar(p);
    }

    // ---------------- Getters ----------------

    public Set<String> getMunicipiosUnicos() { return municipiosUnicos; }
    public Set<String> getMunicipiosOrdenados() { return municipiosOrdenados; }

    public List<Paquete> getPaquetesPorMunicipio(String municipio) {
        return porMunicipio.getOrDefault(municipio, Collections.emptyList());
    }

    public ListaSimple<Paquete> getHistorial() { return historial; }
    public Map<String, Paquete> getPorCodigo() { return porCodigo; }
    public int tamanio() { return porCodigo.size(); }
}