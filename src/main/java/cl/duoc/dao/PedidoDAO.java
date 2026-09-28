package cl.duoc.dao;

import cl.duoc.conexion.ConexionBD;
import cl.duoc.modelo.EstadoPedido;
import cl.duoc.modelo.Pedido;
import cl.duoc.modelo.PedidoComida;
import cl.duoc.modelo.PedidoEncomienda;
import cl.duoc.modelo.PedidoExpress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    // Trae el pedido y el nombre del último repartidor asignado (si tiene)
    private static final String SELECT_BASE = """
            SELECT p.id, p.direccion, p.tipo, p.estado,
                   (SELECT r.nombre
                      FROM entrega e
                      JOIN repartidor r ON r.id = e.id_repartidor
                     WHERE e.id_pedido = p.id
                     ORDER BY e.id DESC
                     LIMIT 1) AS repartidor
              FROM pedido p
            """;

    public boolean guardar(Pedido pedido) {
        String sql = "INSERT INTO pedido (direccion, tipo, estado) VALUES (?, ?, ?)";

        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conexion = ConexionBD.conectar();
            ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pedido.getDireccionEntrega());
            ps.setString(2, pedido.getTipo());
            ps.setString(3, pedido.getEstado().name());

            int filas = ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                pedido.setId(rs.getInt(1));
            }

            return filas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el pedido: " + e.getMessage(), e);
        } finally {
            ConexionBD.cerrar(rs, ps, conexion);
        }
    }

    public List<Pedido> listarTodos() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = SELECT_BASE + " ORDER BY p.id";

        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conexion = ConexionBD.conectar();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                pedidos.add(mapear(rs));
            }
            return pedidos;

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los pedidos: " + e.getMessage(), e);
        } finally {
            ConexionBD.cerrar(rs, ps, conexion);
        }
    }

    public Pedido buscarPorId(int id) {
        String sql = SELECT_BASE + " WHERE p.id = ?";

        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conexion = ConexionBD.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapear(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el pedido: " + e.getMessage(), e);
        } finally {
            ConexionBD.cerrar(rs, ps, conexion);
        }
    }

    public boolean actualizarEstado(int id, EstadoPedido estado) {
        String sql = "UPDATE pedido SET estado = ? WHERE id = ?";

        Connection conexion = null;
        PreparedStatement ps = null;

        try {
            conexion = ConexionBD.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, estado.name()); // 1er ?  -> estado
            ps.setInt(2, id);               // 2do ?  -> WHERE id
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el estado: " + e.getMessage(), e);
        } finally {
            ConexionBD.cerrar(ps, conexion);
        }
    }

    private Pedido mapear(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String direccion = rs.getString("direccion");
        String tipo = rs.getString("tipo");

        Pedido pedido;

        switch (tipo) {
            case "COMIDA" -> pedido = new PedidoComida(id, direccion, "Restaurante SpeedFast");
            case "ENCOMIENDA" -> pedido = new PedidoEncomienda(id, direccion, direccion);
            default -> pedido = new PedidoExpress(id, direccion, "Compra Express");
        }

        pedido.setEstado(EstadoPedido.valueOf(rs.getString("estado")));

        String repartidor = rs.getString("repartidor");
        pedido.setRepartidor(repartidor != null ? repartidor : "");

        return pedido;
    }
}