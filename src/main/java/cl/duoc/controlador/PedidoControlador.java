package cl.duoc.controlador;

import cl.duoc.dao.EntregaDAO;
import cl.duoc.dao.PedidoDAO;
import cl.duoc.modelo.Entrega;
import cl.duoc.modelo.Pedido;
import cl.duoc.modelo.Repartidor;
import cl.duoc.tareas.TareaEntrega;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PedidoControlador {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final EntregaDAO entregaDAO = new EntregaDAO();

    public boolean agregarPedido(Pedido pedido) {
        return pedidoDAO.guardar(pedido);
    }

    public List<Pedido> listarPedidos() {
        return pedidoDAO.listarTodos();
    }

    public Pedido buscarPorId(int id) {
        return pedidoDAO.buscarPorId(id);
    }

    public void asignarRepartidor(Pedido pedido, Repartidor repartidor) {

        Entrega entrega = new Entrega(
                pedido.getId(),
                repartidor.getId(),
                LocalDate.now(),
                LocalTime.now().withNano(0)
        );

        entregaDAO.guardar(entrega);

        Thread hilo = new Thread(
                new TareaEntrega(repartidor.getNombre(), pedido, pedidoDAO)
        );
        hilo.start();
    }
}