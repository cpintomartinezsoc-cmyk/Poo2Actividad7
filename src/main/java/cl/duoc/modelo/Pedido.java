package cl.duoc.modelo;

import cl.duoc.interfaz.Cancelable;
import cl.duoc.interfaz.Despachable;
import cl.duoc.interfaz.Rastreable;

public abstract class Pedido implements Despachable, Cancelable, Rastreable {

    protected int id;
    protected String direccionEntrega;
    protected EstadoPedido estado;
    protected String repartidor;

    public Pedido(int id, String direccionEntrega) {
        this.id = id;
        this.direccionEntrega = direccionEntrega;
        this.estado = EstadoPedido.PENDIENTE;
        this.repartidor = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public String getRepartidor() {
        return repartidor;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public void setRepartidor(String repartidor) {
        this.repartidor = repartidor;
    }

    public abstract String getTipo();

    public abstract void mostrarResumen();

    @Override
    public void despachar() {
        estado = EstadoPedido.EN_REPARTO;
    }

    @Override
    public void cancelar() {
        estado = EstadoPedido.PENDIENTE;
    }

    @Override
    public void verHistorial() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "Pedido " + id +
                " | Tipo: " + getTipo() +
                " | Dirección: " + direccionEntrega +
                " | Estado: " + estado +
                " | Repartidor: " + repartidor;
    }
}