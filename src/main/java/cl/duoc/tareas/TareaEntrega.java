package cl.duoc.tareas;

import cl.duoc.dao.PedidoDAO;
import cl.duoc.modelo.EstadoPedido;
import cl.duoc.modelo.Pedido;

public class TareaEntrega implements Runnable {

    private String nombreRepartidor;
    private Pedido pedido;
    private PedidoDAO pedidoDAO;

    public TareaEntrega(String nombreRepartidor, Pedido pedido, PedidoDAO pedidoDAO) {
        this.nombreRepartidor = nombreRepartidor;
        this.pedido = pedido;
        this.pedidoDAO = pedidoDAO;
    }

    @Override
    public void run() {

        try {
            pedido.setRepartidor(nombreRepartidor);
            pedido.despachar(); // estado = EN_REPARTO
            pedidoDAO.actualizarEstado(pedido.getId(), pedido.getEstado());

            System.out.println(
                    nombreRepartidor + " comenzó la entrega del pedido " + pedido.getId()
            );

            Thread.sleep(3000);

            pedido.setEstado(EstadoPedido.ENTREGADO);
            pedidoDAO.actualizarEstado(pedido.getId(), pedido.getEstado());

            System.out.println(
                    nombreRepartidor + " entregó el pedido " + pedido.getId()
            );

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }
}