package servicio;

import comparadores.ComparadoresPaquete;
import estructuras.ListaSimple;
import modelo.Paquete;
import modelo.Repartidor;

import java.util.*;

public class CentroDistribucion {

    private String nombre;

    // Orden exacto de registro (también se pasa a los algoritmos recursivos)
    private final List<Paquete> paquetesRegistrados;

    // Consulta por código, validación de duplicados y código -> Paquete
    private final Map<String, Paquete> paquetesPorCodigo;

    // Municipios sin repetir
    private final Set<String> municipios;

    // Municipios en orden alfabético y sin repetir
    private final Set<String> municipiosOrdenados;

    // Municipio -> códigos de sus paquetes
    private final Map<String, Set<String>> codigosPorMunicipio;

    // Atención por orden de llegada
    private final Queue<Paquete> colaLlegada;

    // Despacho prioritario: prioridad, tiempo y código
    private final PriorityQueue<Paquete> colaDespacho;

    // Repartidores por identificación (única)
    private final Map<String, Repartidor> repartidores;

    // Código del paquete en camino -> repartidor que lo lleva
    private final Map<String, Repartidor> asignaciones;

    // Historial de entregas (lista propia)
    private final ListaSimple<Paquete> historialEntrega;

    public CentroDistribucion(String nombre) {
        this.nombre = nombre;
        this.paquetesRegistrados = new ArrayList<>();
        this.paquetesPorCodigo = new HashMap<>();
        this.municipios = new HashSet<>();
        this.municipiosOrdenados = new TreeSet<>();
        this.codigosPorMunicipio = new HashMap<>();
        this.colaLlegada = new ArrayDeque<>();
        this.colaDespacho = new PriorityQueue<>(ComparadoresPaquete.DESPACHO_PRIORITARIO);
        this.repartidores = new LinkedHashMap<>();
        this.asignaciones = new HashMap<>();
        this.historialEntrega = new ListaSimple<>();
    }

    // ---------------- Registro ----------------

    /** @return false si el paquete es nulo o el código ya existe. */
    public boolean registrarPaquete(Paquete paquete) {
        if (paquete == null || paquetesPorCodigo.containsKey(paquete.getCodigo())) {
            return false;
        }
        paquetesRegistrados.add(paquete);
        paquetesPorCodigo.put(paquete.getCodigo(), paquete);

        String destino = paquete.getDestino();
        municipios.add(destino);
        municipiosOrdenados.add(destino);
        codigosPorMunicipio
                .computeIfAbsent(destino, k -> new LinkedHashSet<>())
                .add(paquete.getCodigo());

        colaLlegada.offer(paquete);
        colaDespacho.offer(paquete);
        return true;
    }

    /** @return false si es nulo o la identificación ya existe. */
    public boolean registrarRepartidor(Repartidor repartidor) {
        if (repartidor == null || repartidores.containsKey(repartidor.getIdentificacion())) {
            return false;
        }
        repartidores.put(repartidor.getIdentificacion(), repartidor);
        return true;
    }

    // ---------------- Consultas ----------------

    /** O(1) promedio gracias al HashMap. */
    public Paquete buscarPaquetePorCodigo(String codigo) {
        return paquetesPorCodigo.get(codigo);
    }

    public Set<String> getCodigosDeMunicipio(String municipio) {
        Set<String> codigos = codigosPorMunicipio.get(municipio);
        return codigos == null ? Collections.emptySet() : Collections.unmodifiableSet(codigos);
    }

    public Set<String> getMunicipios() {
        return Collections.unmodifiableSet(municipios);
    }

    public Set<String> getMunicipiosOrdenados() {
        return Collections.unmodifiableSet(municipiosOrdenados);
    }

    /** Orden de registro original. */
    public List<Paquete> getPaquetesRegistrados() {
        return Collections.unmodifiableList(paquetesRegistrados);
    }

    /** Copia ordenada por código (orden natural). Precondición de la búsqueda binaria. */
    public List<Paquete> getPaquetesOrdenadosPorCodigo() {
        List<Paquete> copia = new ArrayList<>(paquetesRegistrados);
        Collections.sort(copia);
        return copia;
    }

    /** Copia ordenada con un criterio alternativo (Comparator). */
    public List<Paquete> getPaquetesOrdenados(Comparator<Paquete> criterio) {
        List<Paquete> copia = new ArrayList<>(paquetesRegistrados);
        copia.sort(criterio);
        return copia;
    }

    public Collection<Repartidor> getRepartidores() {
        return Collections.unmodifiableCollection(repartidores.values());
    }

    public ListaSimple<Paquete> getHistorialEntrega() {
        return historialEntrega;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // ---------------- Atención ----------------

    /** Siguiente por orden de llegada, sin retirarlo. */
    public Paquete verSiguientePorLlegada() {
        return colaLlegada.peek();
    }

    /** Siguiente por prioridad, tiempo y código, sin retirarlo. */
    public Paquete verSiguientePrioritario() {
        return colaDespacho.peek();
    }

    /** Retira el paquete de ambas colas. @return false si no estaba pendiente. */
    boolean retirarDePendientes(Paquete paquete) {
        boolean estaba = colaLlegada.remove(paquete);   // O(n)
        colaDespacho.remove(paquete);                   // O(n)
        return estaba;
    }

    void registrarAsignacion(Paquete paquete, Repartidor repartidor) {
        asignaciones.put(paquete.getCodigo(), repartidor);
    }

    // ---------------- Entrega ----------------

    /** Pasa un paquete en camino al historial y libera al repartidor. */
    public boolean registrarEntrega(String codigo) {
        Repartidor repartidor = asignaciones.remove(codigo);
        if (repartidor == null) {
            return false;                               // no estaba asignado
        }
        historialEntrega.agregar(paquetesPorCodigo.get(codigo));
        repartidor.setDisponible(true);
        return true;
    }
}