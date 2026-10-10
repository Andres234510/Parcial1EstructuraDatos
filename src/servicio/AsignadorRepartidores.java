package servicio;

import modelo.Paquete;
import modelo.Repartidor;

public class AsignadorRepartidores {
    //clase auxiliar
    private CentroDistribucion centroDistribucion;

    public AsignadorRepartidores(CentroDistribucion centroDistribucion) {
        this.centroDistribucion = centroDistribucion;
    }
    public Repartidor asignarPaquete(String codigo) {
        Paquete paquete = centroDistribucion.buscarPaquetePorCodigo(codigo);   // O(1)
        if (paquete == null) {
            return null;
        }

        for (Repartidor repartidor : centroDistribucion.getRepartidores()) {
            if (repartidor.isDisponible()
                    && repartidor.getZona().equalsIgnoreCase(paquete.getDestino())) {

                // Si ya no estaba pendiente, ya fue asignado antes
                if (!centroDistribucion.retirarDePendientes(paquete)) {
                    return null;
                }
                repartidor.setDisponible(false);
                centroDistribucion.registrarAsignacion(paquete, repartidor);
                return repartidor;
            }
        }
        return null;
    }

    /** Atención estricta por orden de llegada. */
    public Repartidor asignarSiguientePorLlegada() {
        Paquete siguiente = centroDistribucion.verSiguientePorLlegada();
        return siguiente == null ? null : asignarPaquete(siguiente.getCodigo());
    }

    /** Despacho prioritario: prioridad, tiempo estimado y código. */
    public Repartidor asignarSiguientePrioritario() {
        Paquete siguiente = centroDistribucion.verSiguientePrioritario();
        return siguiente == null ? null : asignarPaquete(siguiente.getCodigo());
    }



    public CentroDistribucion getCentroDistribucion() {
        return centroDistribucion;
    }

    public void setCentroDistribucion(CentroDistribucion centroDistribucion) {
        this.centroDistribucion = centroDistribucion;
    }
}
