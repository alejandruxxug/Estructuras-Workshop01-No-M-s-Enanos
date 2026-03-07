# NoMasEnanos - Sistema de Gestión de Agencia de Modelaje

Aplicación de escritorio desarrollada en Java con JavaFX para la gestión integral de una agencia de modelaje. Permite administrar modelos, fotógrafos, lugares y eventos a través de una interfaz gráfica con navegación por pestañas, formularios deslizantes, búsqueda global y persistencia automática de datos.

## Requisitos

- Java 17+
- Maven 3.8+

## Ejecución

```bash
mvn javafx:run
```

## Funcionalidades de la aplicación

### Interfaz principal

- **Navegación por pestañas**: cuatro secciones accesibles desde la barra superior — Modelos, Fotógrafos, Eventos y Lugares. Cada pestaña muestra su contenido con una transición de fundido (fade).
- **Búsqueda global**: campo de texto en la barra superior que filtra los registros de la pestaña activa por nombre, identificación, código u otros campos relevantes.
- **Splash screen**: pantalla de carga al iniciar la aplicación que intenta cargar datos previos desde `agencia.dat` y muestra el estado del proceso.
- **Ventana maximizada automáticamente** al abrir la aplicación.
- **Sistema de modales in-app**: diálogos de información, error y confirmación que aparecen con animación de fundido, utilizados en toda la aplicación para notificaciones y confirmaciones de eliminación.

### Gestión de Modelos

Cada modelo tiene: nombre, identificación (única), contacto, estatura, categoría, disponibilidad y un código autogenerado (`M-0`, `M-1`, ...).

- **Registrar modelo**: formulario deslizante lateral con campos para nombre, identificación, contacto, estatura (numérica), categoría (combo) y disponibilidad (toggle).
- **Editar modelo**: mismo formulario precargado con los datos actuales; la identificación queda deshabilitada para evitar duplicados.
- **Eliminar modelo**: confirmación mediante modal antes de eliminar.
- **Ver detalle**: modal con la información completa del modelo.
- **Filtrar por categoría**: Pasarela, Comercial, Editorial, Fitness, Glamour.
- **Filtrar por disponibilidad**: Disponible / No Disponible.
- **Buscar**: por nombre, código de modelo o identificación.
- **Tabla interactiva**: columnas para código, nombre, estatura, categoría, disponibilidad (badge con estilo visual) y acciones (Ver, Editar, Eliminar).
- **Validación de estatura mínima**: no se permite registrar modelos con estatura inferior a 1.50 m (`EstaturaNoAceptadaException`).
- **Validación de duplicados**: no se pueden registrar dos personas con la misma identificación (`EntidadDuplicadaException`).

### Gestión de Fotógrafos

Cada fotógrafo tiene: nombre, identificación (única), contacto, especialidad y años de experiencia.

- **Registrar fotógrafo**: formulario deslizante con campos para nombre, identificación, contacto, especialidad (combo) y años de experiencia (numérico).
- **Editar fotógrafo**: formulario precargado; identificación deshabilitada.
- **Eliminar fotógrafo**: con confirmación por modal.
- **Ver detalle**: modal con información completa.
- **Filtrar por especialidad**: Retrato, Moda, Producto, Eventos, Naturaleza, Deportes.
- **Buscar**: por nombre, identificación o especialidad.
- **Tabla interactiva**: columnas para identificación, nombre, especialidad, experiencia, contacto y acciones.
- **Validación de experiencia**: los años de experiencia no pueden ser negativos (`ValorInvalidoException`).
- **Validación de duplicados**: misma restricción que modelos por identificación.

### Gestión de Lugares

Cada lugar tiene: nombre (único), dirección, ciudad, capacidad máxima y tipo de lugar.

- **Registrar lugar**: formulario con campos para nombre, dirección, ciudad, capacidad máxima (numérico) y tipo de lugar (combo).
- **Editar lugar**: formulario precargado; nombre deshabilitado.
- **Eliminar lugar**: con confirmación por modal.
- **Ver detalle**: modal con dirección, ciudad, capacidad y tipo.
- **Filtrar por tipo de lugar**: Hotel, Pasarela, Estudio, Salón, Otro.
- **Filtrar por ciudad**: filtro dinámico que se actualiza automáticamente con las ciudades registradas.
- **Buscar**: por nombre, ciudad o dirección.
- **Tabla interactiva**: columnas para nombre, ciudad, dirección, capacidad, tipo (badge naranja) y acciones.
- **Validación de capacidad**: debe ser un número positivo (`ValorInvalidoException`).
- **Validación de duplicados**: no se pueden registrar dos lugares con el mismo nombre (`EntidadDuplicadaException`).

### Gestión de Eventos

Existen dos tipos de eventos que heredan de una clase base común:

#### Evento Público
- Atributos adicionales: capacidad y patrocinador.
- Validación: la capacidad debe ser un número positivo.

#### Evento Privado
- Atributos adicionales: cliente y nivel de confidencialidad (1 a 5).

#### Funcionalidades comunes a ambos tipos

- **Registrar evento**: formulario deslizante donde se selecciona el tipo (Público / Privado) mediante toggle buttons. Según el tipo, se muestran campos específicos. Se selecciona un lugar de los registrados y se asignan modelos y fotógrafos mediante listas de selección múltiple.
- **Editar evento**: formulario precargado con los datos actuales; el tipo de evento queda bloqueado. Los participantes previamente asignados aparecen preseleccionados.
- **Eliminar evento**: con confirmación por modal.
- **Ver detalle**: modal con los detalles completos del evento.
- **Filtrar por tipo**: Todos, Público, Privado (mediante toggle buttons).
- **Buscar**: por nombre del evento.
- **Tabla interactiva**: columnas para nombre, fecha, tipo (badge con color según tipo), lugar, cantidad de participantes y acciones.
- **Asignación de participantes**: selección múltiple de modelos y fotógrafos registrados en la agencia para asignarlos al evento.
- **Selección de fecha**: mediante un DatePicker.
- **Validación de duplicados**: no se pueden registrar dos eventos con el mismo nombre.

### Persistencia de datos

- **Guardar**: serializa todo el estado de la agencia (personas, lugares y eventos) en el archivo `agencia.dat` mediante `ObjectOutputStream`. Se activa desde la interfaz con el botón "Guardar".
- **Cargar**: deserializa los datos desde `agencia.dat` mediante `ObjectInputStream`. Se activa con el botón "Cargar" o automáticamente al iniciar la aplicación.
- **Almacenamiento con arreglos dinámicos**: toda la información se almacena en arreglos primitivos de Java que crecen dinámicamente con `Arrays.copyOf` y reutilizan espacios nulos dejados por eliminaciones.

### Validaciones y excepciones personalizadas

| Excepción | Cuándo se lanza |
|---|---|
| `EstaturaNoAceptadaException` | Al registrar un modelo con estatura menor a 1.50 m |
| `EntidadDuplicadaException` | Al registrar una persona, lugar o evento con identificador/nombre ya existente |
| `ValorInvalidoException` | Al ingresar años de experiencia negativos o capacidad no positiva |

### Animaciones de la interfaz

- **Slide-in / slide-out** en los paneles de formularios (250 ms).
- **Fade-in** al cambiar de pestaña (150 ms).
- **Fade-in / fade-out** en los modales de información, error y confirmación.
- **Fade-out** del splash screen al terminar la carga inicial (500 ms).

## Estructura del proyecto

```
src/main/java/
├── Main.java                                  # Punto de entrada (JavaFX Application)
├── controller/
│   ├── AgenciaController.java                 # Controlador principal: navegación, búsqueda, persistencia, modales
│   ├── ModeloController.java                  # CRUD y filtros de modelos
│   ├── FotografoController.java               # CRUD y filtros de fotógrafos
│   ├── LugarController.java                   # CRUD y filtros de lugares
│   ├── EventoController.java                  # CRUD y filtros de eventos (públicos y privados)
│   └── MainController.java                    # Controlador base (inicialización)
├── clases/
│   ├── Agencia.java                           # Lógica de negocio central (registrar, eliminar, buscar, asignar)
│   ├── dominio/
│   │   ├── eventos/
│   │   │   ├── Evento.java                    # Clase abstracta base para eventos
│   │   │   ├── EventoPublico.java             # Evento con capacidad y patrocinador
│   │   │   ├── EventoPrivado.java             # Evento con cliente y confidencialidad
│   │   │   └── Lugar.java                     # Locación con dirección, ciudad, capacidad y tipo
│   │   └── personas/
│   │       ├── Persona.java                   # Clase abstracta base (nombre, identificación, contacto)
│   │       ├── Modelo.java                    # Modelo con estatura, categoría, disponibilidad y código
│   │       └── Fotografo.java                 # Fotógrafo con especialidad y años de experiencia
│   └── persistencia/
│       ├── SaveData.java                      # Serialización de la agencia a disco
│       └── ReadData.java                      # Deserialización de la agencia desde disco
├── enums/
│   └── TipoLugar.java                        # HOTEL, PASARELA, ESTUDIO, SALON, OTRO
└── excepciones/
    ├── EntidadDuplicadaException.java         # Identificador o nombre duplicado
    ├── EstaturaNoAceptadaException.java       # Estatura por debajo del mínimo
    └── ValorInvalidoException.java            # Valor numérico fuera de rango

src/main/resources/
├── app.css                                    # Estilos de la interfaz (badges, botones, modales)
└── views/
    ├── main-view.fxml                         # Layout principal con splash, tabs y modales
    ├── ModelosView.fxml                       # Vista de modelos (tabla + formulario)
    ├── FotografosView.fxml                    # Vista de fotógrafos (tabla + formulario)
    ├── LugaresView.fxml                       # Vista de lugares (tabla + formulario)
    └── EventosView.fxml                       # Vista de eventos (tabla + formulario)
```

## Tecnologías

| Tecnología | Uso |
|---|---|
| **Java 17** | Lenguaje principal |
| **JavaFX 21** | Framework de interfaz gráfica |
| **ControlsFX 11** | Componentes de UI extendidos |
| **FormsFX 11** | Formularios declarativos |
| **ValidatorFX 0.6** | Validación de campos |
| **BootstrapFX 0.4** | Estilos CSS tipo Bootstrap |
| **TilesFX 21** | Componentes de dashboard |
| **Maven** | Gestión de dependencias y build |

## Hecho por 

  - Arango Espinosa Samuel
  - Muñoz Cuartas Miguel Angel
  - Zuluaga Gonzalez Mariana
  - Urrego Giraldo Alejandro