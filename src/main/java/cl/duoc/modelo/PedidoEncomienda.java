package cl.duoc.modelo;

public class PedidoEncomienda extends Pedido {

    private String destino;

    public PedidoEncomienda(int id, String direccionEntrega, String destino) {
        super(id, direccionEntrega);
        this.destino = destino;
    }

    public String getDestino() {
        return destino;
    }

    @Override
    public String getTipo() {
        return "ENCOMIENDA";
    }

    @Override
    public void mostrarResumen() {
        System.out.println(
                "Pedido de encomienda " + id +
                        " | Destino: " + destino +
                        " | Dirección: " + direccionEntrega
        );
    }
}