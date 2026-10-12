import algoritmos.BusquedaBinaria;
import algoritmos.DivideVenceras;
import comparadores.ComparadoresPaquete;
import Datos.DatosIniciales;
import estructuras.Coleccion;
import estructuras.ListaSimple;
import modelo.Paquete;
import modelo.Repartidor;
import servicio.CentroDistribucion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

/**
 * Demostración completa de QuindioExpress (Parcial 1).
 *
 * Estrategia:
 *  - Las secciones 1 a 4 y 11 usan el centro grande (30+ paquetes, 6+ repartidores).
 *  - Las demás secciones usan centros pequeños y controlados que reproducen
 *    EXACTAMENTE los ejemplos del enunciado, para que el resultado sea
 *    predecible y coincida con las pruebas de escritorio del PDF.
 *  - Cada resultado esperado se comprueba automáticamente con verificar(...).
 */
public class Main {

    private static int verificaciones = 0;
    private static int fallos = 0;

    public static void main(String[] args) {

        CentroDistribucion centro = new CentroDistribucion();
        List<Paquete> paquetes = DatosIniciales.paquetes();
        List<Repartidor> repartidores = DatosIniciales.repartidores();

        seccion1Carga(centro, paquetes, repartidores);
        seccion2Validaciones(centro, paquetes);
        seccion3Consultas(centro);
        seccion4Municipios(centro, paquetes);
        seccion5OrdenLlegada();
        seccion6DespachoPrioritario();
        seccion7Ordenamientos(centro, paquetes);
        seccion8Recursividad();
        seccion9BusquedaBinaria(centro, paquetes);
        seccion10DivideYVenceras(centro, paquetes);
        seccion11ListaSimple();
        seccion12Historial();
        seccion13Asignacion(centro);

        titulo("RESUMEN");
        System.out.println("  Verificaciones: " + verificaciones + " | Fallos: " + fallos);
        System.out.println(fallos == 0 ? "  Todo correcto." : "  Revisar los casos marcados como FALLO.");
    }

    // =====================================================================
    // 1. CARGA DE DATOS Y MÍNIMOS DEL ENUNCIADO
    // =====================================================================
    private static void seccion1Carga(CentroDistribucion centro, List<Paquete> paquetes,
                                      List<Repartidor> repartidores) {
        titulo("1. Carga de datos iniciales");
        for (Paquete p : paquetes) {
            centro.registrarPaquete(p);
        }
        for (Repartidor r : repartidores) {
            centro.registrarRepartidor(r);
        }
        System.out.println("  Paquetes registrados: " + paquetes.size());
        System.out.println("  Repartidores registrados: " + repartidores.size());

        verificar("Al menos 30 paquetes", true, paquetes.size() >= 30);
        verificar("Al menos 6 repartidores", true, repartidores.size() >= 6);
        verificar("Al menos 5 destinos distintos", true, centro.municipiosUnicos().size() >= 5);
        verificar("Paquetes almacenados = paquetes cargados",
                paquetes.size(), centro.getGestorPaquetes().tamanio());
    }

    // =====================================================================
    // 2. VALIDACIONES Y REGLAS DE NEGOCIO
    // =====================================================================
    private static void seccion2Validaciones(CentroDistribucion centro, List<Paquete> paquetes) {
        titulo("2. Validaciones (todas deben ser rechazadas)");
        lanza("Código duplicado (PQ731)", IllegalArgumentException.class,
                () -> centro.registrarPaquete(new Paquete("PQ731", "Armenia", 1.0, 3, 10)));
        lanza("Código nulo", IllegalArgumentException.class,
                () -> new Paquete(null, "Armenia", 1.0, 3, 10));
        lanza("Peso = 0", IllegalArgumentException.class,
                () -> new Paquete("PQX01", "Armenia", 0, 3, 10));
        lanza("Peso negativo", IllegalArgumentException.class,
                () -> new Paquete("PQX02", "Armenia", -2.5, 3, 10));
        lanza("Prioridad 0 (fuera de rango)", IllegalArgumentException.class,
                () -> new Paquete("PQX03", "Armenia", 1.0, 0, 10));
        lanza("Prioridad 6 (fuera de rango)", IllegalArgumentException.class,
                () -> new Paquete("PQX04", "Armenia", 1.0, 6, 10));
        lanza("Tiempo estimado = 0", IllegalArgumentException.class,
                () -> new Paquete("PQX05", "Armenia", 1.0, 3, 0));
        lanza("Destino vacío", IllegalArgumentException.class,
                () -> new Paquete("PQX06", "   ", 1.0, 3, 10));
        lanza("Destino nulo", IllegalArgumentException.class,
                () -> new Paquete("PQX07", null, 1.0, 3, 10));
        lanza("Repartidor con identificación duplicada (R001)", IllegalArgumentException.class,
                () -> centro.registrarRepartidor(new Repartidor("R001", "Otro", "Armenia")));

        verificar("Los rechazos no alteraron la cantidad de paquetes",
                paquetes.size(), centro.getGestorPaquetes().tamanio());

        Paquete porDefecto = new Paquete("PQD01", "Armenia", 1.0, 20);
        verificar("Prioridad por defecto = 5", 5, porDefecto.getPrioridad());
    }

    // =====================================================================
    // 3. CONSULTAS POR CÓDIGO / IDENTIFICACIÓN
    // =====================================================================
    private static void seccion3Consultas(CentroDistribucion centro) {
        titulo("3. Consultas por código");
        Paquete p = centro.consultarPaquete("PQ731");
        System.out.println("  PQ731 -> " + p);
        verificar("PQ731 existe", true, p != null);
        verificar("Destino de PQ731", "Armenia", p == null ? null : p.getDestino());
        verificar("PQ000 no existe", null, centro.consultarPaquete("PQ000"));

        Repartidor r = centro.consultarRepartidor("R003");
        System.out.println("  R003 -> " + r);
        verificar("R003 existe", true, r != null);
        verificar("R999 no existe", null, centro.consultarRepartidor("R999"));
    }

    // =====================================================================
    // 4. MUNICIPIOS: ÚNICOS, ORDENADOS Y AGRUPACIÓN
    // =====================================================================
    private static void seccion4Municipios(CentroDistribucion centro, List<Paquete> paquetes) {
        titulo("4. Municipios (Set, TreeSet y Map)");
        Set<String> unicos = centro.municipiosUnicos();
        Set<String> ordenados = centro.municipiosOrdenados();
        System.out.println("  Únicos (sin orden garantizado): " + unicos);
        System.out.println("  Ordenados alfabéticamente:      " + ordenados);

        List<String> actual = new ArrayList<>(ordenados);
        List<String> esperada = new ArrayList<>(actual);
        Collections.sort(esperada);
        verificar("Municipios en orden alfabético", esperada, actual);
        verificar("Ambos conjuntos tienen los mismos municipios", true, unicos.equals(ordenados));

        System.out.println("  Paquetes por municipio:");
        int suma = 0;
        for (String m : ordenados) {
            List<Paquete> grupo = centro.paquetesDeMunicipio(m);
            suma += grupo.size();
            System.out.println("    " + m + " (" + grupo.size() + ") -> " + codigos(grupo));
        }
        verificar("La suma de los grupos = total de paquetes", paquetes.size(), suma);
        verificar("Municipio sin paquetes devuelve lista vacía",
                0, centro.paquetesDeMunicipio("Pereira").size());
    }

    // =====================================================================
    // 5. ATENCIÓN POR ORDEN DE LLEGADA (FIFO) - ejemplo del enunciado
    // =====================================================================
    private static void seccion5OrdenLlegada() {
        titulo("5. Atención por orden de llegada (FIFO)");
        CentroDistribucion c = new CentroDistribucion();
        String[] llegada = {"PQ731", "PQ105", "PQ942", "PQ318"};
        for (String codigo : llegada) {
            c.registrarPaquete(p(codigo, "Armenia", 2.0, 3, 20));
        }
        List<String> atendidos = new ArrayList<>();
        Paquete siguiente;
        while ((siguiente = c.atenderPorLlegada()) != null) {
            atendidos.add(siguiente.getCodigo());
        }
        System.out.println("  Llegada:  PQ731, PQ105, PQ942, PQ318");
        System.out.println("  Atención: " + unir(atendidos));
        verificar("Orden de atención = orden de llegada",
                "PQ731, PQ105, PQ942, PQ318", unir(atendidos));
        verificar("Sin paquetes pendientes devuelve null", null, c.atenderPorLlegada());
    }

    // =====================================================================
    // 6. DESPACHO PRIORITARIO (PriorityQueue) - ejemplo del enunciado
    // =====================================================================
    private static void seccion6DespachoPrioritario() {
        titulo("6. Despacho prioritario (prioridad, tiempo, código)");
        CentroDistribucion c = new CentroDistribucion();
        c.registrarPaquete(p("PQ420", "Armenia", 2.0, 3, 30));
        c.registrarPaquete(p("PQ731", "Armenia", 2.0, 5, 45));
        c.registrarPaquete(p("PQ105", "Armenia", 2.0, 5, 25));
        c.registrarPaquete(p("PQ942", "Armenia", 2.0, 4, 20));

        List<String> orden = new ArrayList<>();
        Paquete siguiente;
        while ((siguiente = c.despacharPrioritario()) != null) {
            orden.add(siguiente.getCodigo());
        }
        System.out.println("  Orden de despacho: " + unir(orden));
        verificar("Mayor prioridad primero, luego menor tiempo",
                "PQ105, PQ731, PQ942, PQ420", unir(orden));

        // Tercer criterio: empate en prioridad y tiempo -> código alfabético
        CentroDistribucion d = new CentroDistribucion();
        d.registrarPaquete(p("PQ900", "Armenia", 2.0, 5, 25));
        d.registrarPaquete(p("PQ105", "Armenia", 2.0, 5, 25));
        List<String> desempate = new ArrayList<>();
        while ((siguiente = d.despacharPrioritario()) != null) {
            desempate.add(siguiente.getCodigo());
        }
        verificar("Empate total: gana el código alfabéticamente menor",
                "PQ105, PQ900", unir(desempate));
    }

    // =====================================================================
    // 7. ORDEN NATURAL (Comparable) Y CRITERIOS (Comparator)
    // =====================================================================
    private static void seccion7Ordenamientos(CentroDistribucion centro, List<Paquete> paquetes) {
        titulo("7. Ordenamientos (Comparable y Comparator)");
        List<Paquete> porPrioridad = centro.ordenarPorPrioridad(paquetes);
        List<Paquete> porPeso = centro.ordenarPorPeso(paquetes);
        List<Paquete> porTiempo = centro.ordenarPorTiempo(paquetes);

        mostrar("Top 5 por prioridad (mayor a menor)", porPrioridad, 5);
        mostrar("Top 5 por peso (mayor a menor)", porPeso, 5);
        mostrar("Top 5 por tiempo (menor a mayor)", porTiempo, 5);

        verificar("Prioridad descendente", true,
                estaOrdenada(porPrioridad, ComparadoresPaquete.POR_PRIORIDAD_DESC));
        verificar("Peso descendente", true,
                estaOrdenada(porPeso, ComparadoresPaquete.POR_PESO_DESC));
        verificar("Tiempo ascendente", true,
                estaOrdenada(porTiempo, ComparadoresPaquete.POR_TIEMPO_ASC));

        // El orden natural no cambia al ordenar con otro criterio
        List<Paquete> muestra = new ArrayList<>();
        muestra.add(p("PQ731", "Armenia", 5.0, 5, 45));
        muestra.add(p("PQ105", "Salento", 3.0, 5, 25));
        muestra.add(p("PQ942", "Armenia", 9.0, 4, 20));
        List<Paquete> copiaPorPeso = centro.ordenarPorPeso(muestra);
        verificar("Orden por peso", "PQ942, PQ731, PQ105", codigos(copiaPorPeso));
        verificar("La lista original no se modificó",
                "PQ731, PQ105, PQ942", codigos(muestra));
        Collections.sort(copiaPorPeso); // usa Comparable (código ascendente)
        verificar("El orden natural sigue siendo por código",
                "PQ105, PQ731, PQ942", codigos(copiaPorPeso));
    }

    // =====================================================================
    // 8. RECURSIVIDAD - ejemplos del enunciado
    // =====================================================================
    private static void seccion8Recursividad() {
        titulo("8. Recursividad");

        // Peso total por municipio (enunciado: Armenia = 14.0 kg)
        CentroDistribucion c = new CentroDistribucion();
        c.registrarPaquete(p("PQ101", "Armenia", 4.5, 3, 20));
        c.registrarPaquete(p("PQ203", "Salento", 3.0, 3, 20));
        c.registrarPaquete(p("PQ415", "Armenia", 7.2, 3, 20));
        c.registrarPaquete(p("PQ730", "Armenia", 2.3, 3, 20));
        System.out.println("  Peso total Armenia: " + kg(c.pesoTotalPorMunicipio("Armenia")) + " kg");
        verificar("Peso total Armenia", "14.0", kg(c.pesoTotalPorMunicipio("Armenia")));
        verificar("Peso total Salento", "3.0", kg(c.pesoTotalPorMunicipio("Salento")));
        verificar("Municipio sin paquetes", "0.0", kg(c.pesoTotalPorMunicipio("Pereira")));

        // Conteo por prioridad (enunciado: 5,2,4,1,5,3,4 con mínimo 4 = 4 paquetes)
        CentroDistribucion d = new CentroDistribucion();
        int[] prioridades = {5, 2, 4, 1, 5, 3, 4};
        for (int i = 0; i < prioridades.length; i++) {
            d.registrarPaquete(p("PR" + (i + 1), "Armenia", 1.0, prioridades[i], 10));
        }
        System.out.println("  Prioridades 5,2,4,1,5,3,4 con mínimo 4: "
                + d.contarPaquetesConPrioridadMinima(4) + " paquetes");
        verificar("Prioridad >= 4", 4, d.contarPaquetesConPrioridadMinima(4));
        verificar("Prioridad >= 5", 2, d.contarPaquetesConPrioridadMinima(5));
        verificar("Prioridad >= 1 (todos)", 7, d.contarPaquetesConPrioridadMinima(1));
    }

    // =====================================================================
    // 9. BÚSQUEDA BINARIA Y SU PRECONDICIÓN
    // =====================================================================
    private static void seccion9BusquedaBinaria(CentroDistribucion centro, List<Paquete> paquetes) {
        titulo("9. Búsqueda binaria");

        // Colección ordenada del enunciado
        String[] codigos = {"PQ105", "PQ214", "PQ318", "PQ421", "PQ567", "PQ731", "PQ842", "PQ942"};
        List<Paquete> ordenados = new ArrayList<>();
        for (String codigo : codigos) {
            ordenados.add(p(codigo, "Armenia", 1.0, 3, 10));
        }
        Paquete hallado = BusquedaBinaria.buscar(ordenados, "PQ731");
        System.out.println("  Buscar PQ731 (recursiva): " + hallado);
        verificar("PQ731 encontrado (recursiva)", "PQ731", hallado == null ? null : hallado.getCodigo());
        Paquete halladoIt = BusquedaBinaria.buscarIterativo(ordenados, "PQ731");
        verificar("PQ731 encontrado (iterativa)", "PQ731", halladoIt == null ? null : halladoIt.getCodigo());
        verificar("Primer elemento (PQ105)", "PQ105",
                BusquedaBinaria.buscar(ordenados, "PQ105").getCodigo());
        verificar("Último elemento (PQ942)", "PQ942",
                BusquedaBinaria.buscar(ordenados, "PQ942").getCodigo());
        verificar("Código inexistente (PQ500)", null, BusquedaBinaria.buscar(ordenados, "PQ500"));
        verificar("Lista vacía", null, BusquedaBinaria.buscar(new ArrayList<>(), "PQ105"));

        // Precondición: sobre datos NO ordenados el resultado es incorrecto
        List<Paquete> desordenados = new ArrayList<>();
        desordenados.add(p("PQ942", "Armenia", 1.0, 3, 10));
        desordenados.add(p("PQ105", "Armenia", 1.0, 3, 10));
        desordenados.add(p("PQ318", "Armenia", 1.0, 3, 10));
        Paquete falso = BusquedaBinaria.buscar(desordenados, "PQ942");
        System.out.println("  Sin ordenar, PQ942 existe pero se obtiene: " + falso);
        verificar("Precondición incumplida: no encuentra un código que sí existe", null, falso);

        // Con el centro grande: ordenar UNA vez y comparar con el HashMap
        List<Paquete> copia = new ArrayList<>(paquetes);
        Collections.sort(copia);
        boolean coincide = true;
        for (Paquete p : paquetes) {
            if (BusquedaBinaria.buscar(copia, p.getCodigo()) != centro.consultarPaquete(p.getCodigo())) {
                coincide = false;
            }
        }
        verificar("Binaria y HashMap devuelven el mismo paquete para todos los códigos", true, coincide);
    }

    // =====================================================================
    // 10. DIVIDE Y VENCERÁS - ejemplo del enunciado
    // =====================================================================
    private static void seccion10DivideYVenceras(CentroDistribucion centro, List<Paquete> paquetes) {
        titulo("10. Divide y vencerás: paquete de mayor peso");
        CentroDistribucion c = new CentroDistribucion();
        double[] pesos = {4.5, 12.0, 3.2, 18.7, 7.4, 5.1};
        for (int i = 0; i < pesos.length; i++) {
            c.registrarPaquete(p("W" + (i + 1), "Armenia", pesos[i], 3, 10));
        }
        Paquete mayor = c.paqueteMayorPeso();
        System.out.println("  Mayor peso entre 4.5, 12.0, 3.2, 18.7, 7.4, 5.1: " + mayor);
        verificar("Mayor peso", "18.7", kg(mayor.getPeso()));
        verificar("Lista vacía", null, new CentroDistribucion().paqueteMayorPeso());
        verificar("Un solo elemento", "5.0",
                kg(DivideVenceras.mayorPeso(List.of(p("U1", "Armenia", 5.0, 3, 10))).getPeso()));

        Paquete mayorGrande = centro.paqueteMayorPeso();
        Paquete esperado = centro.ordenarPorPeso(paquetes).get(0);
        verificar("En el centro grande coincide con el primero ordenado por peso",
                esperado.getCodigo(), mayorGrande.getCodigo());
    }

    // =====================================================================
    // 11. LISTA SIMPLE PROPIA: CASOS BORDE
    // =====================================================================
    private static void seccion11ListaSimple() {
        titulo("11. ListaSimple propia: casos borde");
        ListaSimple<Integer> lista = new ListaSimple<>();

        verificar("Vacía: estaVacia", true, lista.estaVacia());
        verificar("Vacía: tamaño", 0, lista.tamanio());
        verificar("Vacía: eliminar devuelve false", false, lista.eliminar(7));
        lanza("Vacía: obtener(0)", IndexOutOfBoundsException.class, () -> lista.obtener(0));

        lista.agregar(10);
        verificar("Un único elemento", "[10]", lista.toString());
        verificar("Eliminar el único elemento", true, lista.eliminar(10));
        verificar("Queda vacía otra vez", true, lista.estaVacia());

        for (int i = 1; i <= 5; i++) {
            lista.agregar(i);
        }
        verificar("Cinco elementos en orden", "[1 -> 2 -> 3 -> 4 -> 5]", lista.toString());
        verificar("obtener(0)", 1, lista.obtener(0));
        verificar("obtener(4)", 5, lista.obtener(4));
        lanza("obtener(-1)", IndexOutOfBoundsException.class, () -> lista.obtener(-1));
        lanza("obtener(tamaño)", IndexOutOfBoundsException.class, () -> lista.obtener(5));

        verificar("Eliminar el primero (1)", true, lista.eliminar(1));
        verificar("Resultado", "[2 -> 3 -> 4 -> 5]", lista.toString());
        verificar("Eliminar uno intermedio (3)", true, lista.eliminar(3));
        verificar("Resultado", "[2 -> 4 -> 5]", lista.toString());
        verificar("Eliminar el último (5)", true, lista.eliminar(5));
        verificar("Resultado", "[2 -> 4]", lista.toString());
        verificar("Eliminar inexistente (99)", false, lista.eliminar(99));
        verificar("Tamaño sin cambios", 2, lista.tamanio());
        lista.agregar(6); // comprueba que la cola se actualizó tras eliminar el último
        verificar("Agregar después de eliminar el último", "[2 -> 4 -> 6]", lista.toString());

        // Reutilización con otro tipo de dato (genérica)
        ListaSimple<String> textos = new ListaSimple<>();
        textos.agregar("Armenia");
        textos.agregar("Salento");
        verificar("Lista genérica con String", "[Armenia -> Salento]", textos.toString());
    }

    // =====================================================================
    // 12. HISTORIAL DE ENTREGAS + Iterable / Iterator
    // =====================================================================
    private static void seccion12Historial() {
        titulo("12. Historial de entregas (lista propia, Iterable e Iterator)");
        CentroDistribucion c = new CentroDistribucion();
        Paquete a = p("PQ105", "Salento", 3.0, 5, 25);
        Paquete b = p("PQ942", "Armenia", 4.0, 4, 20);
        Paquete d = p("PQ731", "Armenia", 5.0, 5, 45);
        c.registrarPaquete(a);
        c.registrarPaquete(b);
        c.registrarPaquete(d);

        // Orden real de entrega: PQ105, PQ942, PQ731
        c.registrarEntrega(a);
        c.registrarEntrega(b);
        c.registrarEntrega(d);

        Coleccion<Paquete> historial = c.getHistorial(); // se usa por la interfaz genérica
        c.mostrarHistorial();
        verificar("Cantidad en el historial", 3, historial.tamanio());
        verificar("Historial no vacío", false, historial.estaVacia());
        verificar("Orden exacto de entrega", "PQ105 -> PQ942 -> PQ731", flechas(historial));
        verificar("obtener(0)", "PQ105", historial.obtener(0).getCodigo());
        verificar("obtener(2)", "PQ731", historial.obtener(2).getCodigo());

        // Iterable: for-each. Iterator: recorrido explícito
        Iterator<Paquete> it = historial.iterator();
        int contados = 0;
        while (it.hasNext()) {
            it.next();
            contados++;
        }
        verificar("Recorrido con Iterator", 3, contados);
        lanza("Iterator agotado lanza NoSuchElementException", NoSuchElementException.class, it::next);

        // Cada llamada a iterator() entrega un recorrido nuevo e independiente
        verificar("Un nuevo iterator vuelve a empezar", true, historial.iterator().hasNext());
        verificar("El historial sigue intacto tras recorrerlo", "PQ105 -> PQ942 -> PQ731", flechas(historial));
    }

    // =====================================================================
    // 13. ASIGNACIÓN DE PAQUETES A REPARTIDORES
    // =====================================================================
    private static void seccion13Asignacion(CentroDistribucion centro) {
        titulo("13. Asignación a repartidores");
        verificar("Los 6 repartidores inician disponibles", 6,
                centro.getGestorRepartidores().disponibles().size());

        verificar("PQ731 (Armenia) -> R001 (zona Armenia, disponible)", true,
                centro.asignarPaquete("PQ731", "R001"));
        verificar("R001 queda no disponible", false,
                centro.consultarRepartidor("R001").isDisponible());

        verificar("Repartidor no disponible no recibe asignación (R001)", false,
                centro.asignarPaquete("PQ829", "R001"));
        verificar("Zona distinta al destino (PQ731 Armenia -> R002 Salento)", false,
                centro.asignarPaquete("PQ731", "R002"));
        verificar("R002 sigue disponible tras el rechazo", true,
                centro.consultarRepartidor("R002").isDisponible());
        verificar("PQ105 (Salento) -> R002 (zona Salento)", true,
                centro.asignarPaquete("PQ105", "R002"));
        verificar("Paquete inexistente no se asigna", false,
                centro.asignarPaquete("PQ000", "R004"));

        verificar("Automática sin repartidor libre en Armenia", null,
                idDe(centro.asignarAutomatico("PQ829")));
        centro.getGestorRepartidores().liberar("R001");
        verificar("Tras liberar a R001, la automática lo encuentra", "R001",
                idDe(centro.asignarAutomatico("PQ829")));

        centro.getGestorRepartidores().cambiarDisponibilidad("R003", false);
        verificar("R003 (Calarcá) no disponible: PQ318 queda sin repartidor", null,
                idDe(centro.asignarAutomatico("PQ318")));

        System.out.print(centro.getGestorRepartidores());
    }

    // =====================================================================
    // UTILIDADES DE LA DEMOSTRACIÓN
    // =====================================================================

    private static void titulo(String texto) {
        System.out.println("\n=== " + texto + " ===");
    }

    /** Compara esperado y obtenido, imprime el resultado y lo cuenta. */
    private static void verificar(String descripcion, Object esperado, Object obtenido) {
        verificaciones++;
        boolean ok = Objects.equals(esperado, obtenido);
        if (!ok) {
            fallos++;
        }
        System.out.println("  [" + (ok ? "OK" : "FALLO") + "] " + descripcion
                + (ok ? "" : " -> esperado: " + esperado + ", obtenido: " + obtenido));
    }

    /** Comprueba que una acción lance la excepción indicada. */
    private static void lanza(String descripcion, Class<? extends RuntimeException> tipo, Runnable accion) {
        verificaciones++;
        try {
            accion.run();
            fallos++;
            System.out.println("  [FALLO] " + descripcion + " -> no lanzó excepción");
        } catch (RuntimeException e) {
            boolean ok = tipo.isInstance(e);
            if (!ok) {
                fallos++;
            }
            System.out.println("  [" + (ok ? "OK" : "FALLO") + "] " + descripcion
                    + " -> " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private static Paquete p(String codigo, String destino, double peso, int prioridad, int tiempo) {
        return new Paquete(codigo, destino, peso, prioridad, tiempo);
    }

    private static String kg(double valor) {
        return String.format(Locale.US, "%.1f", valor);
    }

    private static String idDe(Repartidor r) {
        return r == null ? null : r.getIdentificacion();
    }

    private static boolean estaOrdenada(List<Paquete> lista, Comparator<Paquete> criterio) {
        for (int i = 1; i < lista.size(); i++) {
            if (criterio.compare(lista.get(i - 1), lista.get(i)) > 0) {
                return false;
            }
        }
        return true;
    }

    private static void mostrar(String titulo, List<Paquete> lista, int cantidad) {
        System.out.println("  " + titulo + ":");
        for (int i = 0; i < cantidad && i < lista.size(); i++) {
            System.out.println("    " + lista.get(i));
        }
    }

    private static String codigos(List<Paquete> lista) {
        List<String> resultado = new ArrayList<>();
        for (Paquete p : lista) {
            resultado.add(p.getCodigo());
        }
        return unir(resultado);
    }

    private static String unir(List<String> elementos) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < elementos.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(elementos.get(i));
        }
        return sb.toString();
    }

    private static String flechas(Iterable<Paquete> historial) {
        StringBuilder sb = new StringBuilder();
        for (Paquete p : historial) {
            if (sb.length() > 0) {
                sb.append(" -> ");
            }
            sb.append(p.getCodigo());
        }
        return sb.toString();
    }
}