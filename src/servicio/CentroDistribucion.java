package servicio;

import algoritmos.AlgoritmosRecursivos;
import algoritmos.BusquedaBinaria;
import algoritmos.DivideVenceras;
import comparadores.ComparadoresPaquete;
import estructuras.ListaSimple;
import modelo.Paquete;
import modelo.Repartidor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Coordinador principal del sistema QuindioExpress.
 *
 * Une los servicios (GestorPaquetes, GestorRepartidores) y los algoritmos
 * (recursivos, búsqueda binaria, divide y vencerás).
 */
public class CentroDistribucion {

    private final GestorPaquetes gestorPaquetes;
    private final GestorRepartidores gestorRepartidores;

    public CentroDistribucion() {
        this.gestorPaquetes = new GestorPaquetes();
        this.gestorRepartidores = new GestorRepartidores();
    }

    // ---------------- REGISTRO ----------------

    public void registrarPaquete(Paquete p) {
        gestorPaquetes.registrar(p);
    }

    public void registrarRepartidor(Repartidor r) {
        gestorRepartidores.registrar(r);
    }

    // ---------------- CONSULTAS ----------------

    public Paquete consultarPaquete(String codigo) {
        return gestorPaquetes.consultar(codigo);
    }

    public Repartidor consultarRepartidor(String identificacion) {
        return gestorRepartidores.consultar(identificacion);
    }

    // ---------------- MUNICIPIOS ----------------

    public Set<String> municipiosUnicos() {
        return gestorPaquetes.getMunicipiosUnicos();
    }

    public Set<String> municipiosOrdenados() {
        return gestorPaquetes.getMunicipiosOrdenados();
    }

    public List<Paquete> paquetesDeMunicipio(String municipio) {
        return gestorPaquetes.getPaquetesPorMunicipio(municipio);
    }

    // ---------------- ATENCIÓN ----------------

    public Paquete atenderPorLlegada() {
        return gestorPaquetes.atenderPorLlegada();
    }

    public Paquete despacharPrioritario() {
        return gestorPaquetes.despacharPrioritario();
    }

    // ---------------- HISTORIAL ----------------

    public void registrarEntrega(Paquete p) {
        gestorPaquetes.registrarEntrega(p);
    }

    public ListaSimple<Paquete> getHistorial() {
        return gestorPaquetes.getHistorial();
    }

    public void mostrarHistorial() {
        System.out.println("Historial de entregas:");
        for (Paquete p : gestorPaquetes.getHistorial()) {
            System.out.println("  " + p);
        }
    }

    // ---------------- ASIGNACIÓN ----------------

    public boolean asignarPaquete(String codigo, String identificacionRepartidor) {
        Paquete p = gestorPaquetes.consultar(codigo);
        Repartidor r = gestorRepartidores.consultar(identificacionRepartidor);
        return gestorRepartidores.asignar(p, r);
    }

    public Repartidor asignarAutomatico(String codigo) {
        Paquete p = gestorPaquetes.consultar(codigo);
        return gestorRepartidores.asignarAutomatico(p);
    }

    // ---------------- ORDENAMIENTOS ----------------

    public List<Paquete> ordenarPorPrioridad(List<Paquete> paquetes) {
        List<Paquete> copia = new ArrayList<>(paquetes);
        copia.sort(ComparadoresPaquete.POR_PRIORIDAD_DESC);
        return copia;
    }

    public List<Paquete> ordenarPorPeso(List<Paquete> paquetes) {
        List<Paquete> copia = new ArrayList<>(paquetes);
        copia.sort(ComparadoresPaquete.POR_PESO_DESC);
        return copia;
    }

    public List<Paquete> ordenarPorTiempo(List<Paquete> paquetes) {
        List<Paquete> copia = new ArrayList<>(paquetes);
        copia.sort(ComparadoresPaquete.POR_TIEMPO_ASC);
        return copia;
    }

    // ---------------- ALGORITMOS ----------------

    public double pesoTotalPorMunicipio(String municipio) {
        List<Paquete> lista = gestorPaquetes.getPaquetesPorMunicipio(municipio);
        return AlgoritmosRecursivos.pesoTotal(lista, 0);
    }
    public int contarPaquetesConPrioridadMinima(int prioridadMinima) {
        List<Paquete> lista = new ArrayList<>(gestorPaquetes.getPorCodigo().values());
        return AlgoritmosRecursivos.contarPorPrioridad(lista, prioridadMinima, 0);
    }

    public Paquete buscarBinario(String codigo) {
        List<Paquete> ordenados = new ArrayList<>(gestorPaquetes.getPorCodigo().values());
        Collections.sort(ordenados);
        return BusquedaBinaria.buscar(ordenados, codigo);
    }

    public Paquete paqueteMayorPeso() {
        List<Paquete> lista = new ArrayList<>(gestorPaquetes.getPorCodigo().values());
        if (lista.isEmpty()) return null;
        return DivideVenceras.mayorPeso(lista, 0, lista.size() - 1);
    }

    // ---------------- GETTERS ----------------

    public GestorPaquetes getGestorPaquetes() { return gestorPaquetes; }
    public GestorRepartidores getGestorRepartidores() { return gestorRepartidores; }
}