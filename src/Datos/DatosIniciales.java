package Datos;

import modelo.Paquete;
import modelo.Repartidor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Datos iniciales del sistema QuindioExpress.
 *
 * Usa java.util.Random para generar 23 paquetes adicionales con
 * códigos, destinos, pesos, prioridades y tiempos aleatorios.
 * Los 7 paquetes manuales son los del ejemplo del enunciado.
 *
 * Cumple los mínimos:
 *  - 30 paquetes
 *  - 6 destinos diferentes
 *  - 6 repartidores
 *  - prioridades variadas (1 a 5)
 *  - pesos variados (> 0)
 *  - códigos no consecutivos
 *  - varios paquetes por municipio
 */
public class DatosIniciales {

    private static final String[] MUNICIPIOS = {
            "Armenia", "Salento", "Calarcá", "Montenegro", "Quimbaya", "Circasia"
    };

    private static final Random RANDOM = new Random();

    private DatosIniciales() { }

    public static List<Paquete> paquetes() {
        List<Paquete> lista = new ArrayList<>();

        // ---------- 7 paquetes representativos (del ejemplo del enunciado) ----------
        lista.add(new Paquete("PQ731", "Armenia",    5.0, 5, 45));
        lista.add(new Paquete("PQ105", "Salento",    3.0, 5, 25));
        lista.add(new Paquete("PQ942", "Armenia",    4.0, 4, 20));
        lista.add(new Paquete("PQ318", "Calarcá",    6.5, 3, 30));
        lista.add(new Paquete("PQ567", "Salento",    2.3, 2, 40));
        lista.add(new Paquete("PQ829", "Armenia",    7.2, 4, 15));
        lista.add(new Paquete("PQ420", "Montenegro", 1.8, 3, 35));

        // ---------- 23 paquetes generados con Random ----------
        for (int i = 0; i < 23; i++) {
            String codigo = "PQ" + (100 + RANDOM.nextInt(900)); // 3 dígitos: 100 a 999
            String destino = MUNICIPIOS[RANDOM.nextInt(MUNICIPIOS.length)];
            double peso = 0.5 + RANDOM.nextDouble() * 19.5;      // 0.5 a 20.0 kg
            int prioridad = 1 + RANDOM.nextInt(5);               // 1 a 5
            int tiempo = 5 + RANDOM.nextInt(60);                 // 5 a 64 min

            // Evitar códigos duplicados
            boolean duplicado = false;
            for (Paquete p : lista) {
                if (p.getCodigo().equals(codigo)) {
                    duplicado = true;
                    break;
                }
            }
            if (duplicado) {
                i--; // repetir este intento
                continue;
            }

            lista.add(new Paquete(codigo, destino, peso, prioridad, tiempo));
        }

        return lista;
    }

    public static List<Repartidor> repartidores() {
        List<Repartidor> lista = new ArrayList<>();

        lista.add(new Repartidor("R001", "Ana Gómez",   "Armenia"));
        lista.add(new Repartidor("R002", "Luis Pérez",  "Salento"));
        lista.add(new Repartidor("R003", "Marta Ruiz",  "Calarcá"));
        lista.add(new Repartidor("R004", "Carlos Díaz", "Montenegro"));
        lista.add(new Repartidor("R005", "Sofía Mora",  "Quimbaya"));
        lista.add(new Repartidor("R006", "Pedro León",  "Circasia"));

        return lista;
    }
}