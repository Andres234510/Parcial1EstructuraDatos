# QuindíoExpress – Gestión de operaciones de un centro de distribución

Parcial 1 · Estructuras de datos · Universidad del Quindío

Aplicación en Java que organiza la información de una jornada de trabajo de un centro de distribución de paquetes: registro, consulta, clasificación, priorización, asignación a repartidores y registro de entregas. El objetivo del parcial no es solo almacenar datos, sino **elegir la estructura o el algoritmo adecuado para cada necesidad** y poder justificar su costo en tiempo y memoria.

## Integrantes

- Andrés Felipe Gutiérrez Vera
- _[Nombre del compañero/a]_

## Requisitos

- Java 11 o superior (el código usa `String.isBlank()` y `List.of`). Probado con Java 21.
- No requiere librerías externas, archivos ni base de datos: los datos iniciales están definidos en Java.

## Cómo compilar y ejecutar

Desde la raíz del repositorio:

```bash
# Linux / macOS / Git Bash
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out Main
```

```powershell
# Windows PowerShell
mkdir out
javac -d out (Get-ChildItem -Recurse -Filter *.java src).FullName
java -cp out Main
```

La carpeta del paquete de datos se llama `Datos` (con D mayúscula) y debe coincidir con la declaración `package Datos;`; en Linux y macOS las mayúsculas importan.

## Estructura del proyecto

```
src/
├── Main.java                      Demostración completa (13 secciones)
├── modelo/
│   ├── Paquete.java               Comparable por código; valida sus datos
│   └── Repartidor.java            Identificación única; disponible/no disponible
├── estructuras/
│   ├── Coleccion.java             Interfaz genérica (extiende Iterable<T>)
│   ├── ListaSimple.java           Lista simplemente enlazada propia + iterador
│   └── Nodo.java                  Nodo (visible solo dentro del paquete)
├── comparadores/
│   └── ComparadoresPaquete.java   Criterios alternativos (Comparator)
├── servicio/
│   ├── CentroDistribucion.java    Fachada que une servicios y algoritmos
│   ├── GestorPaquetes.java        Estructuras de paquetes (mapas, colas, conjuntos)
│   └── GestorRepartidores.java    Registro y asignación de repartidores
├── algoritmos/
│   ├── AlgoritmosRecursivos.java  Peso total por municipio, conteo por prioridad
│   ├── BusquedaBinaria.java       Versión recursiva e iterativa
│   └── DivideVenceras.java        Paquete de mayor peso
└── Datos/
    └── DatosIniciales.java        30 paquetes y 6 repartidores
```

## Estructuras de datos y por qué se eligieron

| Necesidad | Estructura | Operación dominante | Costo esperado |
|---|---|---|---|
| Consultar un paquete por código y validar que no se repita | `HashMap<String, Paquete>` | `get`, `containsKey` | O(1) promedio |
| Atender por orden de llegada | `ArrayDeque<Paquete>` | `addLast`, `pollFirst` | O(1) |
| Municipios sin repetir | `HashSet<String>` | `add`, `contains` | O(1) promedio |
| Municipios ordenados alfabéticamente y sin repetir | `TreeSet<String>` | `add`, recorrido ordenado | O(log m) por inserción |
| Paquetes de cada municipio | `HashMap<String, List<Paquete>>` | `computeIfAbsent` + `add` | O(1) amortizado |
| Despacho prioritario | `PriorityQueue<Paquete>` con `DESPACHO_PRIORITARIO` | `poll` | O(log n) |
| Repartidores por identificación | `HashMap<String, Repartidor>` | `get`, `containsKey` | O(1) promedio |
| Historial de entregas | `ListaSimple<Paquete>` (propia) | `agregar` al final | O(1) |

Registrar un paquete cuesta O(log n), dominado por la cola de prioridad. Cada `Paquete` se guarda por referencia en varias estructuras, sin copiarse.

### Lista simplemente enlazada propia

- Genérica (`ListaSimple<T>`) e independiente de `Paquete`: en la demostración también se usa con `Integer` y `String`.
- No usa ninguna colección de Java para almacenar sus elementos: solo `Nodo<T>` con un valor y una referencia al siguiente.
- Cumple el contrato `Coleccion<T>`: `agregar`, `eliminar`, `obtener`, `tamanio`, `estaVacia`.
- Mantiene referencias a la cabeza y a la cola, por eso `agregar` es O(1). `eliminar` y `obtener` son O(n).
- Se recorre con `Iterable` e `Iterator`: el código externo nunca ve los nodos. `iterator()` entrega un recorrido nuevo en cada llamada.
- Casos cubiertos: lista vacía, un único elemento, eliminar el primero, uno intermedio, el último y un elemento inexistente (devuelve `false`).

## Algoritmos

| Algoritmo | Técnica | Tiempo | Espacio |
|---|---|---|---|
| `pesoTotal` (peso total de un municipio) | Recursividad | O(n) | O(n) por la pila |
| `contarPorPrioridad` (prioridad ≥ mínima) | Recursividad | O(n) | O(n) por la pila |
| `mayorPeso` (paquete más pesado) | Divide y vencerás: caso base, división, subproblemas izquierdo y derecho, combinación | O(n) | O(log n) por la pila |
| `BusquedaBinaria.buscar` | Recursiva | O(log n) | O(log n) por la pila |
| `BusquedaBinaria.buscarIterativo` | Iterativa | O(log n) | O(1) |

La búsqueda binaria exige una **precondición**: la lista debe estar ordenada por código ascendente. Sobre datos desordenados puede responder que un paquete no existe aunque sí esté; la demostración lo comprueba.

Los métodos recursivos no usan `for`, `while` ni `do-while`, como pide el enunciado.

## Orden de los paquetes

- **Orden natural** (`Comparable`): por código ascendente.
- **Criterios alternativos** (`Comparator`, en `ComparadoresPaquete`):
  - `POR_PRIORIDAD_DESC`: prioridad de mayor a menor.
  - `POR_PESO_DESC`: peso de mayor a menor.
  - `POR_TIEMPO_ASC`: tiempo estimado de menor a mayor.
  - `DESPACHO_PRIORITARIO`: mayor prioridad, luego menor tiempo estimado y, si persiste el empate, código alfabético.

Ordenar por un criterio alternativo no altera el orden natural del objeto.

## Reglas de negocio validadas

- No se aceptan dos paquetes con el mismo código ni dos repartidores con la misma identificación.
- El peso y el tiempo estimado deben ser mayores que cero; el destino no puede ser nulo ni vacío.
- La prioridad va de 1 a 5 y vale 5 por defecto. El enunciado menciona 0–10 en la descripción del atributo y 1–5 en las reglas; se adoptó 1–5 y se puede cambiar en las constantes `PRIORIDAD_MINIMA` y `PRIORIDAD_MAXIMA` de `Paquete`.
- Un repartidor no disponible no recibe asignaciones, y su zona debe coincidir con el destino del paquete.
- El historial conserva el orden real de entrega.

## Datos iniciales

`DatosIniciales` carga 30 paquetes (7 fijos, tomados de los ejemplos del enunciado, y 23 generados con `java.util.Random`), 6 municipios y 6 repartidores. Los códigos no son consecutivos y hay varios paquetes por municipio. Como los 23 paquetes restantes son aleatorios, algunos valores impresos cambian entre ejecuciones; las secciones con ejemplos del enunciado usan datos fijos.

## Qué muestra `Main`

`Main` ejecuta una demostración de 13 secciones y **comprueba automáticamente cada resultado** con `verificar(...)`, imprimiendo `[OK]` o `[FALLO]`. Al final muestra un resumen; con el código entregado son 95 verificaciones y 0 fallos.

1. Carga de datos y mínimos del enunciado
2. Validaciones (todas deben ser rechazadas)
3. Consultas por código
4. Municipios únicos, ordenados y agrupación
5. Atención por orden de llegada
6. Despacho prioritario
7. Ordenamientos (`Comparable` y `Comparator`)
8. Recursividad
9. Búsqueda binaria y su precondición
10. Divide y vencerás
11. `ListaSimple`: casos borde
12. Historial con `Iterable` e `Iterator`
13. Asignación a repartidores

## Limitaciones conocidas

- `CentroDistribucion.buscarBinario` copia y ordena los paquetes en cada llamada (O(n log n)). La búsqueda binaria solo compensa sobre una colección que se ordena una vez y se reutiliza, como se hace en la sección 9 de la demostración.
- `atenderPorLlegada` y `despacharPrioritario` sacan el paquete solo de su propia cola; si se usan ambos sobre los mismos paquetes, uno podría atenderse dos veces.
- `pesoTotal` y `contarPorPrioridad` usan O(n) de pila; con una cantidad muy grande de paquetes en un municipio podrían desbordarla.

## Documentación

El informe en PDF contiene el análisis y diseño (Parte A), las pruebas de escritorio, los diagramas de pila y el análisis de complejidad (Parte C), y la bitácora de uso de inteligencia artificial (Parte D).
