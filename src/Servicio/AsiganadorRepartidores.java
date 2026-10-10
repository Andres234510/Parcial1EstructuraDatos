package Servicio;

public class AsiganadorRepartidores {
    //clase auxiliar
    private CentroDistribucion centroDistribucion;

    public AsiganadorRepartidores(CentroDistribucion centroDistribucion) {
        this.centroDistribucion = centroDistribucion;
    }

    public CentroDistribucion getCentroDistribucion() {
        return centroDistribucion;
    }

    public void setCentroDistribucion(CentroDistribucion centroDistribucion) {
        this.centroDistribucion = centroDistribucion;
    }
}
