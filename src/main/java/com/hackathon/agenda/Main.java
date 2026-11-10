package com.hackathon.agenda;

import com.hackathon.agenda.controlador.AgendaControlador;
import com.hackathon.agenda.modelo.Agenda;
import com.hackathon.agenda.vista.VentanaPrincipal;

import javax.swing.SwingUtilities;

/** Orquestador: instancia el Modelo, la Vista y los conecta con el Controlador. */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Agenda agenda = new Agenda(); // capacidad por defecto: 10 (configurable: new Agenda(n))
            VentanaPrincipal vista = new VentanaPrincipal();
            new AgendaControlador(agenda, vista);
            vista.setVisible(true);
        });
    }
}
