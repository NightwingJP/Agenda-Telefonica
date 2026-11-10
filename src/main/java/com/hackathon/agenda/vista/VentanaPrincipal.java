package com.hackathon.agenda.vista;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Vista: solo construye y expone los componentes Swing.
 * No decide qué ocurre al hacer clic; eso lo hace el controlador.
 */
public class VentanaPrincipal extends JFrame {

    private static final Color ROJO = new Color(200, 0, 0);
    private static final Color VERDE = new Color(0, 130, 0);

    private final JTextField txtNombre = new JTextField(15);
    private final JTextField txtApellido = new JTextField(15);
    private final JTextField txtTelefono = new JTextField(15);

    private final JButton btnAñadir = new JButton("Añadir");
    private final JButton btnModificar = new JButton("Modificar teléfono");
    private final JButton btnEliminar = new JButton("Eliminar");
    private final JButton btnBuscar = new JButton("Buscar");
    private final JButton btnLimpiar = new JButton("Limpiar");

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[]{"Nombre", "Apellido", "Teléfono"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modeloTabla);

    private final JLabel lblEstado = new JLabel(" ");
    private final JLabel lblCapacidad = new JLabel(" ");

    public VentanaPrincipal() {
        super("Agenda Telefónica");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(construirFormulario(), BorderLayout.NORTH);
        add(construirTabla(), BorderLayout.CENTER);
        add(construirPanelEstado(), BorderLayout.SOUTH);

        setMinimumSize(new Dimension(620, 480));
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel construirFormulario() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Datos del contacto"));

        JPanel campos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.anchor = GridBagConstraints.WEST;

        agregarCampo(campos, gbc, 0, "Nombre:", txtNombre);
        agregarCampo(campos, gbc, 1, "Apellido:", txtApellido);
        agregarCampo(campos, gbc, 2, "Teléfono:", txtTelefono);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        botones.add(btnAñadir);
        botones.add(btnModificar);
        botones.add(btnEliminar);
        botones.add(btnBuscar);
        botones.add(btnLimpiar);

        panel.add(campos, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila,
                              String etiqueta, JTextField campo) {
        gbc.gridy = fila;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(campo, gbc);
        gbc.weightx = 0;
    }

    private JScrollPane construirTabla() {
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(24);
        tabla.getTableHeader().setReorderingAllowed(false);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Contactos (orden alfabético)"));
        scroll.setPreferredSize(new Dimension(600, 240));
        return scroll;
    }

    private JPanel construirPanelEstado() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Panel de estado"));
        lblEstado.setFont(lblEstado.getFont().deriveFont(Font.BOLD));
        panel.add(lblEstado, BorderLayout.CENTER);
        panel.add(lblCapacidad, BorderLayout.EAST);
        return panel;
    }

    // ---- Métodos de apoyo para que el controlador actualice la vista ----

    public void mostrarError(String mensaje) {
        lblEstado.setForeground(ROJO);
        lblEstado.setText(mensaje);
    }

    public void mostrarExito(String mensaje) {
        lblEstado.setForeground(VERDE);
        lblEstado.setText(mensaje);
    }

    public void mostrarCapacidad(int ocupados, int capacidad) {
        lblCapacidad.setText("Contactos: " + ocupados + "/" + capacidad
                + "  |  Libres: " + (capacidad - ocupados));
    }

    public void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        tabla.clearSelection();
    }

    // ---- Getters de componentes ----

    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtApellido() { return txtApellido; }
    public JTextField getTxtTelefono() { return txtTelefono; }
    public JButton getBtnAñadir() { return btnAñadir; }
    public JButton getBtnModificar() { return btnModificar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JTable getTabla() { return tabla; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
}
