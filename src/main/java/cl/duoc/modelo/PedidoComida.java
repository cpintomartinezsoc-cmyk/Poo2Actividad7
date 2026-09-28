package cl.duoc.modelo;

public class PedidoComida extends Pedido {

    private String restaurante;

    public PedidoComida(int id, String direccionEntrega, String restaurante) {
        super(id, direccionEntrega);
        this.restaurante = restaurante;
    }

    public String getRestaurante() {
        return restaurante;
    }

    @Override
    public String getTipo() {
        return "COMIDA";
    }

    @Override
    public void mostrarResumen() {
        System.out.println(
                "Pedido de comida " + id +
                        " | Restaurante: " + restaurante +
                        " | Dirección: " + direccionEntrega
        );
    }
}