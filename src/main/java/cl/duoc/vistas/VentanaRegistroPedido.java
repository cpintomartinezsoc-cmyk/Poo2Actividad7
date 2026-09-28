package cl.duoc.vistas;

import cl.duoc.controlador.PedidoControlador;
import cl.duoc.modelo.Pedido;
import cl.duoc.modelo.PedidoComida;
import cl.duoc.modelo.PedidoEncomienda;
import cl.duoc.modelo.PedidoExpress;

import javax.swing.*;

public class VentanaRegistroPedido extends JFrame {

    private JTextField txtDireccion;
    private JComboBox<String> cmbTipo;
    private JButton btnGuardar;

    private PedidoControlador controlador;

    public VentanaRegistroPedido(PedidoControlador controlador) {

        this.controlador = controlador;

        setTitle("Registrar Pedido");
        setSize(400, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        crearComponentes();
    }

    private void crearComponentes() {

        setLayout(null);


        JLabel lblDireccion = new JLabel("Dirección:");
        lblDireccion.setBounds(30, 30, 100, 30);
        add(lblDireccion);

        txtDireccion = new JTextField();
        txtDireccion.setBounds(130, 30, 200, 30);
        add(txtDireccion);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(30, 70, 100, 30);
        add(lblTipo);

        cmbTipo = new JComboBox<>(
                new String[]{
                        "Comida",
                        "Encomienda",
                        "Express"
                }
        );
        cmbTipo.setBounds(130, 70, 200, 30);
        add(cmbTipo);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(130, 115, 200, 30);
        add(btnGuardar);

        btnGuardar.addActionListener(e -> guardarPedido());
    }

    private void guardarPedido() {

        String direccion = txtDireccion.getText().trim();

        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar la dirección.");
            return;
        }

        String tipo = cmbTipo.getSelectedItem().toString();

        Pedido pedido;

        if (tipo.equals("Comida")) {
            pedido = new PedidoComida(0, direccion, "Restaurante SpeedFast");
        } else if (tipo.equals("Encomienda")) {
            pedido = new PedidoEncomienda(0, direccion, direccion);
        } else {
            pedido = new PedidoExpress(0, direccion, "Compra Express");
        }

        try {

            if (controlador.agregarPedido(pedido)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Pedido registrado correctamente con ID: " + pedido.getId()
                );
                txtDireccion.setText("");
            }

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    this, e.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE
            );
        }
    }
}