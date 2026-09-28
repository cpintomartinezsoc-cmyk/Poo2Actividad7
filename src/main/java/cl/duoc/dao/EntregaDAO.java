package cl.duoc.dao;

import cl.duoc.conexion.ConexionBD;
import cl.duoc.modelo.Entrega;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

public class EntregaDAO {

    public boolean guardar(Entrega entrega) {
        String sql = """
                INSERT INTO entrega (id_pedido, id_repartidor, fecha, hora)
                VALUES (?, ?, ?, ?)
                """;

        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conexion = ConexionBD.conectar();
            ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, entrega.getIdPedido());
            ps.setInt(2, entrega.getIdRepartidor());
            ps.setDate(3, Date.valueOf(entrega.getFecha()));   // LocalDate -> DATE
            ps.setTime(4, Time.valueOf(entrega.getHora()));    // LocalTime -> TIME

            int filas = ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                entrega.setId(rs.getInt(1));
            }

            return filas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la entrega: " + e.getMessage(), e);
        } finally {
            ConexionBD.cerrar(rs, ps, conexion);
        }
    }
}