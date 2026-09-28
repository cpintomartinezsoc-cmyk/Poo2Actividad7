package cl.duoc.controlador;

import cl.duoc.dao.RepartidorDAO;
import cl.duoc.modelo.Repartidor;

import java.util.List;

public class RepartidorControlador {

    private final RepartidorDAO repartidorDAO = new RepartidorDAO();

    public boolean registrarRepartidor(Repartidor repartidor) {
        return repartidorDAO.guardar(repartidor);
    }

    public List<Repartidor> listarRepartidores() {
        return repartidorDAO.listarTodos();
    }
}