package Servicio;

import estructuras.ListaSimple;
import modelo.Paquete;
import modelo.Repartidor;

public class CentroDistribucion {

    private String nombre;
    private ListaSimple<Paquete>paquetesPendientes;
    private ListaSimple<Repartidor> repartidores;
    private ListaSimple<Paquete> historialEntrega;

    public CentroDistribucion(String nombre) {
        this.nombre = nombre;
        this.paquetesPendientes = new ListaSimple<>();
        this.repartidores = new ListaSimple<>();
        this.historialEntrega = new ListaSimple<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ListaSimple<Paquete> getPaquetesPendientes() {
        return paquetesPendientes;
    }

    public void setPaquetesPendientes(ListaSimple<Paquete> paquetesPendientes) {
        this.paquetesPendientes = paquetesPendientes;
    }

    public ListaSimple<Repartidor> getRepartidores() {
        return repartidores;
    }

    public void setRepartidores(ListaSimple<Repartidor> repartidores) {
        this.repartidores = repartidores;
    }

    public ListaSimple<Paquete> getHistorialEntrega() {
        return historialEntrega;
    }

    public void setHistorialEntrega(ListaSimple<Paquete> historialEntrega) {
        this.historialEntrega = historialEntrega;
    }
}
