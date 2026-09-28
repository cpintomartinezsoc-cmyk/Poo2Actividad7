package cl.duoc.dao;

import cl.duoc.conexion.ConexionBD;
import cl.duoc.modelo.Repartidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAO {

    public boolean guardar(Repartidor repartidor) {
        String sql = "INSERT INTO repartidor (nombre) VALUES (?)";

        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conexion = ConexionBD.conectar();
            ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, repartidor.getNombre());

            int filas = ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                repartidor.setId(rs.getInt(1));
            }

            return filas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el repartidor: " + e.getMessage(), e);
        } finally {
            ConexionBD.cerrar(rs, ps, conexion);
        }
    }

    public List<Repartidor> listarTodos() {
        List<Repartidor> repartidores = new ArrayList<>();
        String sql = "SELECT id, nombre FROM repartidor ORDER BY id";

        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conexion = ConexionBD.conectar();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                repartidores.add(new Repartidor(
                        rs.getInt("id"),
                        rs.getString("nombre")
                ));
            }
            return repartidores;

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los repartidores: " + e.getMessage(), e);
        } finally {
            ConexionBD.cerrar(rs, ps, conexion);
        }
    }
}