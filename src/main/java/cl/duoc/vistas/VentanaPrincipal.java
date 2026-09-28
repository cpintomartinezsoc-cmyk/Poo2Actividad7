package cl.duoc.vistas;

import cl.duoc.controlador.PedidoControlador;
import cl.duoc.controlador.RepartidorControlador;
import cl.duoc.modelo.EstadoPedido;
import cl.duoc.modelo.Pedido;
import cl.duoc.modelo.Repartidor;

import javax.swing.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private JButton btnRegistrar;
    private JButton btnListar;
    private JButton btnRepartidores;
    private JButton btnEntrega;

    private PedidoControlador controlador;
    private RepartidorControlador repartidorControlador;

    public VentanaPrincipal() {

        setTitle("SpeedFast");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        controlador = new PedidoControlador();
        repartidorControlador = new RepartidorControlador();

        crearComponentes();
    }

    private void crearComponentes() {

        setLayout(null);

        JLabel titulo = new JLabel("Sistema de Gestión SpeedFast");
        titulo.setBounds(100, 20, 250, 30);
        add(titulo);

        btnRegistrar = new JButton("Registrar Pedido");
        btnRegistrar.setBounds(100, 60, 200, 30);
        add(btnRegistrar);

        btnListar = new JButton("Listar Pedidos");
        btnListar.setBounds(100, 100, 200, 30);
        add(btnListar);

        btnRepartidores = new JButton("Repartidores");
        btnRepartidores.setBounds(100, 140, 200, 30);
        add(btnRepartidores);

        btnEntrega = new JButton("Asignar Repartidor");
        btnEntrega.setBounds(100, 180, 200, 30);
        add(btnEntrega);

        btnRegistrar.addActionListener(e -> {
            VentanaRegistroPedido ventana = new VentanaRegistroPedido(controlador);
            ventana.setVisible(true);
        });

        btnListar.addActionListener(e -> {
            VentanaListaPedidos ventana = new VentanaListaPedidos(controlador);
            ventana.setVisible(true);
        });

        btnRepartidores.addActionListener(e -> {
            VentanaRepartidores ventana = new VentanaRepartidores(repartidorControlador);
            ventana.setVisible(true);
        });

        btnEntrega.addActionListener(e -> iniciarEntrega());
    }

    private void iniciarEntrega() {

        String idTexto = JOptionPane.showInputDialog(
                this,
                "Ingrese el ID del pedido:"
        );

        if (idTexto == null) {
            return;
        }

        try {

            int id = Integer.parseInt(idTexto.trim());

            Pedido pedido = controlador.buscarPorId(id);

            if (pedido == null) {
                JOptionPane.showMessageDialog(this, "No existe un pedido con ese ID.");
                return;
            }

            if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
                JOptionPane.showMessageDialog(
                        this,
                        "El pedido ya fue asignado. Estado actual: " + pedido.getEstado()
                );
                return;
            }

            List<Repartidor> repartidores = repartidorControlador.listarRepartidores();

            if (repartidores.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No hay repartidores registrados. Registre uno primero."
                );
                return;
            }

            Repartidor seleccionado = (Repartidor) JOptionPane.showInputDialog(
                    this,
                    "Seleccione el repartidor:",
                    "Asignar Repartidor",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    repartidores.toArray(),
                    repartidores.get(0)
            );

            if (seleccionado == null) {
                return;
            }

            controlador.asignarRepartidor(pedido, seleccionado);

            JOptionPane.showMessageDialog(
                    this,
                    "Entrega registrada para el pedido " + id +
                            " con " + seleccionado.getNombre()
            );

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número.");
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    this, e.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE
            );
        }
    }
}