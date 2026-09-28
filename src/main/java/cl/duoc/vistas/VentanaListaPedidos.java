package cl.duoc.vistas;

import cl.duoc.controlador.PedidoControlador;
import cl.duoc.modelo.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VentanaListaPedidos extends JFrame {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JButton btnActualizar;

    private PedidoControlador controlador;

    public VentanaListaPedidos(PedidoControlador controlador) {

        this.controlador = controlador;

        setTitle("Lista de Pedidos");
        setSize(650, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        crearComponentes();
        cargarDatos();
    }

    private void crearComponentes() {

        setLayout(null);

        // Tabla de solo lectura
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Dirección");
        modeloTabla.addColumn("Tipo");
        modeloTabla.addColumn("Estado");
        modeloTabla.addColumn("Repartidor");

        tabla = new JTable(modeloTabla);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20, 20, 590, 150);
        add(scroll);

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setBounds(240, 190, 150, 30);
        add(btnActualizar);

        btnActualizar.addActionListener(e -> cargarDatos());
    }

    private void cargarDatos() {

        modeloTabla.setRowCount(0);

        try {

            for (Pedido pedido : controlador.listarPedidos()) {
                modeloTabla.addRow(
                        new Object[]{
                                pedido.getId(),
                                pedido.getDireccionEntrega(),
                                pedido.getTipo(),
                                pedido.getEstado(),
                                pedido.getRepartidor()
                        }
                );
            }

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    this, e.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE
            );
        }
    }
}