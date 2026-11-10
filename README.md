# Agenda Telefónica MVC

Aplicación de escritorio en **Java Swing** para gestionar una agenda telefónica, construida con **Gradle** y siguiendo estrictamente el patrón **MVC**.

## Ejecución

Requiere JDK 17 o superior.

```bash
./gradlew run        # Linux / macOS
gradlew.bat run      # Windows
```

En IntelliJ: *Open* → seleccionar la carpeta del proyecto → ejecutar la tarea `application > run` de Gradle (o `Main.java`).

## Arquitectura

```
com.hackathon.agenda
├── modelo/        Lógica pura, sin imports de javax.swing
│   ├── Contacto   Entidad: nombre, apellido, teléfono (validación + equals/hashCode)
│   └── Agenda     Reglas de negocio y almacenamiento (List<Contacto>)
├── vista/
│   └── VentanaPrincipal   JFrame: construye y expone componentes; no decide acciones
├── controlador/
│   └── AgendaControlador  ActionListeners y selección de tabla; coordina modelo y vista
└── Main           Instancia modelo, vista y controlador y los conecta
```

### Decisiones técnicas

- **Igualdad de contactos:** `equals()` y `hashCode()` usan nombre y apellido ignorando mayúsculas/minúsculas; el teléfono no interviene.
- **Validación de teléfono (Regex):** `^(\+\d{1,3}[ -]?)?\d{3}[ -]?\d{3}[ -]?\d{3,4}$`. Admite `600000000`, `600 000 000`, `+34 600 000 000`, `3001234567`.
- **Nombre y apellido:** no pueden estar vacíos ni ser solo espacios (se valida en el constructor y en los setters de `Contacto`).
- **Capacidad:** 10 contactos por defecto; configurable con `new Agenda(n)`.
- **Orden:** `listarContactos()` devuelve una copia ordenada alfabéticamente (apellido y luego nombre, sin distinguir mayúsculas).
- **Manejo de errores:** el modelo lanza excepciones con mensajes descriptivos; el controlador las captura y las muestra en el Panel de Estado (rojo = error, verde = éxito). Nada se imprime en la consola.
- **Tabla:** `DefaultTableModel` no editable; al seleccionar una fila se rellenan los campos del formulario.

## API de `Agenda`

| Método | Descripción |
|---|---|
| `añadirContacto(Contacto)` | Valida campos, espacio disponible y duplicados antes de insertar |
| `existeContacto(Contacto)` | Comprueba por nombre y apellido |
| `listarContactos()` | Lista ordenada alfabéticamente |
| `buscarContacto(nombre, apellido)` | Devuelve el teléfono (o `null`) |
| `eliminarContacto(Contacto)` | Elimina si existe |
| `modificarTelefono(nombre, apellido, nuevo)` | Actualiza tras validar con Regex |
| `agendaLlena()` / `espaciosLibres()` | Estado de capacidad |

## Uso de la interfaz

1. Complete nombre, apellido y teléfono y pulse **Añadir**.
2. Seleccione una fila para editarla: use **Modificar teléfono** o **Eliminar**.
3. **Buscar** muestra el teléfono según nombre y apellido.
4. **Limpiar** vacía el formulario.

## Casos para el control de calidad cruzado

- Campos vacíos o solo espacios.
- Teléfono con letras, muy corto o con formato raro.
- Duplicado con distinta capitalización (`ana lópez` vs `ANA LÓPEZ`).
- Añadir un contacto 11 con la agenda llena.
- Modificar/eliminar/buscar un contacto inexistente.
