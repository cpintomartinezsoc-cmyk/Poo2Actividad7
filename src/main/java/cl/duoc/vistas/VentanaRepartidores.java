package cl.duoc.vistas;

import cl.duoc.controlador.RepartidorControlador;
import cl.duoc.modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VentanaRepartidores extends JFrame {

    private JTextField txtNombre;
    private JButton btnGuardar;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private RepartidorControlador controlador;

    public VentanaRepartidores(RepartidorControlador controlador) {

        this.controlador = controlador;

        setTitle("Repartidores");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        crearComponentes();
        cargarDatos();
    }

    private void crearComponentes() {

        setLayout(null);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(30, 20, 80, 30);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(100, 20, 180, 30);
        add(txtNombre);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(290, 20, 110, 30);
        add(btnGuardar);

        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");

        tabla = new JTable(modeloTabla);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(30, 70, 370, 210);
        add(scroll);

        btnGuardar.addActionListener(e -> guardarRepartidor());
    }

    private void guardarRepartidor() {

        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el nombre.");
            return;
        }

        try {

            Repartidor repartidor = new Repartidor(nombre);

            if (controlador.registrarRepartidor(repartidor)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Repartidor registrado con ID: " + repartidor.getId()
                );
                txtNombre.setText("");
                cargarDatos();
            }

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    this, e.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cargarDatos() {

        modeloTabla.setRowCount(0);

        try {

            for (Repartidor r : controlador.listarRepartidores()) {
                modeloTabla.addRow(new Object[]{r.getId(), r.getNombre()});
            }

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    this, e.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE
            );
        }
    }
}