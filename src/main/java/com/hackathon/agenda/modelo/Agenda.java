package com.hackathon.agenda.modelo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Lógica de negocio de la agenda. No depende de Swing.
 * Las operaciones inválidas lanzan IllegalArgumentException / IllegalStateException
 * con un mensaje descriptivo que el controlador muestra en la interfaz.
 */
public class Agenda {

    public static final int TAMANO_POR_DEFECTO = 10;

    private final List<Contacto> contactos = new ArrayList<>();
    private final int capacidad;

    public Agenda() {
        this(TAMANO_POR_DEFECTO);
    }

    public Agenda(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero.");
        }
        this.capacidad = capacidad;
    }

    public void añadirContacto(Contacto c) {
        if (c == null) {
            throw new IllegalArgumentException("El contacto es obligatorio.");
        }
        if (c.getNombre() == null || c.getNombre().trim().isEmpty()
                || c.getApellido() == null || c.getApellido().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre y apellido son obligatorios.");
        }
        if (agendaLlena()) {
            throw new IllegalStateException(
                    "La agenda está llena (máximo " + capacidad + " contactos).");
        }
        if (existeContacto(c)) {
            throw new IllegalArgumentException(
                    "El contacto " + c.getNombre() + " " + c.getApellido() + " ya existe.");
        }
        contactos.add(c);
    }

    public boolean existeContacto(Contacto c) {
        return c != null && contactos.contains(c);
    }

    /** Devuelve una copia de la lista ordenada alfabéticamente (apellido, luego nombre). */
    public List<Contacto> listarContactos() {
        List<Contacto> copia = new ArrayList<>(contactos);
        copia.sort(Comparator
                .comparing((Contacto c) -> c.getApellido().toLowerCase())
                .thenComparing(c -> c.getNombre().toLowerCase()));
        return copia;
    }

    /** Devuelve el teléfono del contacto, o null si no existe. */
    public String buscarContacto(String nombre, String apellido) {
        Contacto encontrado = localizar(nombre, apellido);
        return encontrado == null ? null : encontrado.getTelefono();
    }

    /** @return true si el contacto existía y fue eliminado. */
    public boolean eliminarContacto(Contacto c) {
        return c != null && contactos.remove(c);
    }

    /**
     * Actualiza el teléfono de un contacto existente.
     *
     * @return true si se modificó; false si el contacto no existe.
     * @throws IllegalArgumentException si el nuevo teléfono no cumple el formato.
     */
    public boolean modificarTelefono(String nombre, String apellido, String nuevoTelefono) {
        if (!Contacto.telefonoValido(nuevoTelefono)) {
            throw new IllegalArgumentException(
                    "Teléfono inválido. Ejemplos válidos: 600000000 o +34 600 000 000.");
        }
        Contacto encontrado = localizar(nombre, apellido);
        if (encontrado == null) {
            return false;
        }
        encontrado.setTelefono(nuevoTelefono);
        return true;
    }

    public boolean agendaLlena() {
        return contactos.size() >= capacidad;
    }

    public int espaciosLibres() {
        return capacidad - contactos.size();
    }

    public int getCapacidad() {
        return capacidad;
    }

    private Contacto localizar(String nombre, String apellido) {
        if (nombre == null || apellido == null) {
            return null;
        }
        for (Contacto c : contactos) {
            if (c.getNombre().equalsIgnoreCase(nombre.trim())
                    && c.getApellido().equalsIgnoreCase(apellido.trim())) {
                return c;
            }
        }
        return null;
    }
}
