import Datos.DatosIniciales;
import modelo.Paquete;
import modelo.Repartidor;
import servicio.CentroDistribucion;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        CentroDistribucion centro = new CentroDistribucion();

        // 1. Cargar datos
        List<Paquete> paquetes = DatosIniciales.paquetes();
        List<Repartidor> repartidores = DatosIniciales.repartidores();

        for (Paquete p : paquetes) {
            centro.registrarPaquete(p);
        }
        for (Repartidor r : repartidores) {
            centro.registrarRepartidor(r);
        }

        System.out.println("Paquetes registrados: " + paquetes.size());
        System.out.println("Repartidores registrados: " + repartidores.size());

        // 2. Consulta por código
        System.out.println("\n-- Consulta por código --");
        Paquete p = centro.consultarPaquete("PQ731");
        System.out.println(p);

        // 3. Municipios únicos y ordenados
        System.out.println("\n-- Municipios únicos --");
        System.out.println(centro.municipiosUnicos());

        System.out.println("\n-- Municipios ordenados --");
        System.out.println(centro.municipiosOrdenados());

        // 4. Paquetes por municipio
        System.out.println("\n-- Paquetes en Armenia --");
        for (Paquete paq : centro.paquetesDeMunicipio("Armenia")) {
            System.out.println("  " + paq.getCodigo());
        }

        // 5. Atención por orden de llegada
        System.out.println("\n-- Atención por orden de llegada --");
        Paquete atendido1 = centro.atenderPorLlegada();
        Paquete atendido2 = centro.atenderPorLlegada();
        System.out.println("Atendido: " + atendido1.getCodigo());
        System.out.println("Atendido: " + atendido2.getCodigo());

        // 6. Despacho prioritario
        System.out.println("\n-- Despacho prioritario --");
        Paquete urgente1 = centro.despacharPrioritario();
        Paquete urgente2 = centro.despacharPrioritario();
        System.out.println("Despachado: " + urgente1.getCodigo());
        System.out.println("Despachado: " + urgente2.getCodigo());

        // 7. Historial de entregas
        centro.registrarEntrega(urgente1);
        centro.registrarEntrega(urgente2);
        centro.mostrarHistorial();

        // 8. Ordenamientos
        System.out.println("\n-- Por prioridad --");
        List<Paquete> porPrioridad = centro.ordenarPorPrioridad(paquetes);
        for (int i = 0; i < 5; i++) {
            System.out.println("  " + porPrioridad.get(i).getCodigo()
                    + " prioridad " + porPrioridad.get(i).getPrioridad());
        }

        System.out.println("\n-- Por peso --");
        List<Paquete> porPeso = centro.ordenarPorPeso(paquetes);
        for (int i = 0; i < 5; i++) {
            System.out.println("  " + porPeso.get(i).getCodigo()
                    + " peso " + porPeso.get(i).getPeso());
        }

        System.out.println("\n-- Por tiempo --");
        List<Paquete> porTiempo = centro.ordenarPorTiempo(paquetes);
        for (int i = 0; i < 5; i++) {
            System.out.println("  " + porTiempo.get(i).getCodigo()
                    + " tiempo " + porTiempo.get(i).getTiempoEstimado());
        }

        // 9. Recursivos
        System.out.println("\n-- Peso total Armenia (recursivo) --");
        System.out.println(centro.pesoTotalPorMunicipio("Armenia") + " kg");

        System.out.println("\n-- Paquetes con prioridad >= 4 (recursivo) --");
        System.out.println(centro.contarPaquetesConPrioridadMinima(4));

        // 10. Búsqueda binaria
        System.out.println("\n-- Búsqueda binaria PQ567 --");
        Paquete buscado = centro.buscarBinario("PQ567");
        System.out.println(buscado != null ? buscado : "No encontrado");

        // 11. Divide y vencerás
        System.out.println("\n-- Paquete de mayor peso (divide y vencerás) --");
        Paquete mayor = centro.paqueteMayorPeso();
        System.out.println(mayor);
// 12. Asignación automática
        System.out.println("\n-- Asignación automática --");
        Repartidor asignado = centro.asignarAutomatico("PQ731");
        if (asignado != null) {
            System.out.println("Asignado a: " + asignado.getNombre());
        } else {
            System.out.println("No hay repartidor disponible");
        }
    }
}