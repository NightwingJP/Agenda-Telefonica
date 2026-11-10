package com.hackathon.agenda.controlador;

import com.hackathon.agenda.modelo.Agenda;
import com.hackathon.agenda.modelo.Contacto;
import com.hackathon.agenda.vista.VentanaPrincipal;

import javax.swing.JTable;
import java.util.List;

/**
 * Controlador: escucha los eventos de la vista y coordina el modelo.
 * Ninguna excepción llega a la consola: todo error se muestra en el panel de estado.
 */
public class AgendaControlador {

    private final Agenda agenda;
    private final VentanaPrincipal vista;
    private boolean actualizandoTabla = false;

    public AgendaControlador(Agenda agenda, VentanaPrincipal vista) {
        this.agenda = agenda;
        this.vista = vista;
        registrarListeners();
        refrescarTabla();
    }

    private void registrarListeners() {
        vista.getBtnAñadir().addActionListener(e -> añadir());
        vista.getBtnModificar().addActionListener(e -> modificarTelefono());
        vista.getBtnEliminar().addActionListener(e -> eliminar());
        vista.getBtnBuscar().addActionListener(e -> buscar());
        vista.getBtnLimpiar().addActionListener(e -> {
            vista.limpiarCampos();
            vista.mostrarExito("Formulario limpiado.");
        });

        // Sincronización: al seleccionar una fila se rellenan los campos del formulario.
        vista.getTabla().getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting() || actualizandoTabla) {
                return;
            }
            int fila = vista.getTabla().getSelectedRow();
            if (fila >= 0) {
                vista.getTxtNombre().setText(valor(fila, 0));
                vista.getTxtApellido().setText(valor(fila, 1));
                vista.getTxtTelefono().setText(valor(fila, 2));
            }
        });
    }

    private String valor(int fila, int columna) {
        JTable tabla = vista.getTabla();
        return String.valueOf(tabla.getValueAt(fila, columna));
    }

    private void añadir() {
        try {
            Contacto c = new Contacto(
                    vista.getTxtNombre().getText(),
                    vista.getTxtApellido().getText(),
                    vista.getTxtTelefono().getText());
            agenda.añadirContacto(c);
            refrescarTabla();
            vista.limpiarCampos();
            vista.mostrarExito("Contacto añadido: " + c.getNombre() + " " + c.getApellido());
        } catch (IllegalArgumentException | IllegalStateException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    private void modificarTelefono() {
        String nombre = vista.getTxtNombre().getText();
        String apellido = vista.getTxtApellido().getText();
        try {
            if (nombre.trim().isEmpty() || apellido.trim().isEmpty()) {
                vista.mostrarError("Indique nombre y apellido del contacto a modificar.");
                return;
            }
            boolean ok = agenda.modificarTelefono(nombre, apellido, vista.getTxtTelefono().getText());
            if (ok) {
                refrescarTabla();
                vista.mostrarExito("Teléfono actualizado para " + nombre.trim() + " " + apellido.trim());
            } else {
                vista.mostrarError("No existe el contacto " + nombre.trim() + " " + apellido.trim() + ".");
            }
        } catch (IllegalArgumentException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    private void eliminar() {
        String nombre = vista.getTxtNombre().getText();
        String apellido = vista.getTxtApellido().getText();
        if (nombre.trim().isEmpty() || apellido.trim().isEmpty()) {
            vista.mostrarError("Seleccione un contacto o indique nombre y apellido para eliminar.");
            return;
        }
        // El teléfono no interviene en la igualdad; se usa uno válido de relleno.
        Contacto objetivo = new Contacto(nombre, apellido, "000000000");
        if (agenda.eliminarContacto(objetivo)) {
            refrescarTabla();
            vista.limpiarCampos();
            vista.mostrarExito("Contacto eliminado: " + nombre.trim() + " " + apellido.trim());
        } else {
            vista.mostrarError("No existe el contacto " + nombre.trim() + " " + apellido.trim() + ".");
        }
    }

    private void buscar() {
        String nombre = vista.getTxtNombre().getText();
        String apellido = vista.getTxtApellido().getText();
        if (nombre.trim().isEmpty() || apellido.trim().isEmpty()) {
            vista.mostrarError("Indique nombre y apellido para buscar.");
            return;
        }
        String telefono = agenda.buscarContacto(nombre, apellido);
        if (telefono == null) {
            vista.mostrarError("No se encontró a " + nombre.trim() + " " + apellido.trim() + ".");
        } else {
            vista.getTxtTelefono().setText(telefono);
            vista.mostrarExito("Teléfono de " + nombre.trim() + " " + apellido.trim() + ": " + telefono);
        }
    }

    private void refrescarTabla() {
        actualizandoTabla = true;
        try {
            vista.getModeloTabla().setRowCount(0);
            List<Contacto> lista = agenda.listarContactos();
            for (Contacto c : lista) {
                vista.getModeloTabla().addRow(
                        new Object[]{c.getNombre(), c.getApellido(), c.getTelefono()});
            }
            vista.mostrarCapacidad(lista.size(), agenda.getCapacidad());
        } finally {
            actualizandoTabla = false;
        }
    }
}
