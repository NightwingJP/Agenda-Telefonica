package com.hackathon.agenda.modelo;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Entidad pura que representa un contacto de la agenda.
 * No depende de ninguna clase de Swing.
 */
public class Contacto {

    /**
     * Formatos admitidos: 600000000, 600 000 000, 600-000-000,
     * +34 600 000 000, +57 300 123 4567, 3001234567.
     */
    private static final Pattern PATRON_TELEFONO =
            Pattern.compile("^(\\+\\d{1,3}[ -]?)?\\d{3}[ -]?\\d{3}[ -]?\\d{3,4}$");

    private String nombre;
    private String apellido;
    private String telefono;

    public Contacto(String nombre, String apellido, String telefono) {
        setNombre(nombre);
        setApellido(apellido);
        setTelefono(telefono);
    }

    public static boolean telefonoValido(String telefono) {
        return telefono != null && PATRON_TELEFONO.matcher(telefono.trim()).matches();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío.");
        }
        this.apellido = apellido.trim();
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        if (!telefonoValido(telefono)) {
            throw new IllegalArgumentException(
                    "Teléfono inválido. Ejemplos válidos: 600000000 o +34 600 000 000.");
        }
        this.telefono = telefono.trim();
    }

    /** Dos contactos son iguales si comparten nombre y apellido (sin distinguir mayúsculas). */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contacto)) {
            return false;
        }
        Contacto otro = (Contacto) o;
        return nombre.equalsIgnoreCase(otro.nombre) && apellido.equalsIgnoreCase(otro.apellido);
    }

    @Override
    public int hashCode() {
        return 31 * nombre.toLowerCase(Locale.ROOT).hashCode()
                + apellido.toLowerCase(Locale.ROOT).hashCode();
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + " (" + telefono + ")";
    }
}
