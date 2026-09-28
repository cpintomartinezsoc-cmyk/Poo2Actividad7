package cl.duoc.modelo;

public class PedidoExpress extends Pedido {

    private String tipoCompra;

    public PedidoExpress(int id, String direccionEntrega, String tipoCompra) {
        super(id, direccionEntrega);
        this.tipoCompra = tipoCompra;
    }

    public String getTipoCompra() {
        return tipoCompra;
    }

    @Override
    public String getTipo() {
        return "EXPRESS";
    }

    @Override
    public void mostrarResumen() {
        System.out.println(
                "Pedido express " + id +
                        " | Tipo: " + tipoCompra +
                        " | Dirección: " + direccionEntrega
        );
    }
}