import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import clases.dominio.eventos.Evento;
import clases.dominio.eventos.EventoPrivado;
import clases.dominio.eventos.EventoPublico;
import clases.dominio.eventos.Lugar;
import clases.dominio.personas.Fotografo;
import clases.dominio.personas.Modelo;
import clases.dominio.personas.Persona;
import enums.TipoLugar;

import java.time.LocalDate;
import java.util.List;

public class Main extends Application {

    private final List<Lugar> lugares = createLugares();
    private final List<Modelo> modelos = createModelos();
    private final List<Fotografo> fotografos = createFotografos();
    private final List<Evento> eventos = createEventos();

    @Override
    public void start(Stage stage) {
        TabPane tabs = new TabPane();

        // Tab 1: Eventos
        ListView<Evento> eventosList = new ListView<>();
        eventosList.getItems().addAll(eventos);

        TextArea eventoDetail = new TextArea();
        eventoDetail.setEditable(false);
        eventoDetail.setPromptText("Selecciona un evento para ver detalles.");
        eventosList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, evento) -> {
            if (evento != null) {
                eventoDetail.setText(evento.tipoEvento() + "\n\n" + evento.mostrarDetalles());
            } else {
                eventoDetail.clear();
            }
        });

        BorderPane eventosPane = new BorderPane();
        eventosPane.setLeft(eventosList);
        eventosPane.setCenter(eventoDetail);
        eventosPane.setPadding(new Insets(10));
        eventosList.setPrefWidth(220);
        Tab tabEventos = new Tab("Eventos", eventosPane);
        tabEventos.setClosable(false);

        // Tab 2: Modelos y fotógrafos (compartido)
        ListView<Persona> personasList = new ListView<>();
        modelos.forEach(p -> personasList.getItems().add(p));
        fotografos.forEach(p -> personasList.getItems().add(p));

        TextArea personaDetail = new TextArea();
        personaDetail.setEditable(false);
        personaDetail.setPromptText("Selecciona una persona para ver su información.");
        personasList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, persona) -> {
            if (persona != null) {
                personaDetail.setText(persona.mostrarInformacion());
            } else {
                personaDetail.clear();
            }
        });

        BorderPane personasPane = new BorderPane();
        personasPane.setLeft(personasList);
        personasPane.setCenter(personaDetail);
        personasPane.setPadding(new Insets(10));
        personasList.setPrefWidth(220);
        Tab tabPersonas = new Tab("Modelos y fotógrafos", personasPane);
        tabPersonas.setClosable(false);

        // Tab 3: Lugares
        ListView<Lugar> lugaresList = new ListView<>();
        lugaresList.getItems().addAll(lugares);

        TextArea lugarDetail = new TextArea();
        lugarDetail.setEditable(false);
        lugarDetail.setPromptText("Selecciona un lugar para ver detalles.");
        lugaresList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, lugar) -> {
            if (lugar != null) {
                lugarDetail.setText("Lugar: " + lugar.getNombre() + "\n" +
                        "Dirección: " + lugar.getDireccion() + "\n" +
                        "Ciudad: " + lugar.getCiudad() + "\n" +
                        "Capacidad máxima: " + lugar.getCapacidadMaxima() + "\n" +
                        "Tipo: " + lugar.getTipoLugar());
            } else {
                lugarDetail.clear();
            }
        });

        BorderPane lugaresPane = new BorderPane();
        lugaresPane.setLeft(lugaresList);
        lugaresPane.setCenter(lugarDetail);
        lugaresPane.setPadding(new Insets(10));
        lugaresList.setPrefWidth(220);
        Tab tabLugares = new Tab("Lugares", lugaresPane);
        tabLugares.setClosable(false);

        tabs.getTabs().addAll(tabEventos, tabPersonas, tabLugares);

        VBox root = new VBox(10, new Label("Agencia — Eventos y personal"), tabs);
        root.setPadding(new Insets(15));

        Scene scene = new Scene(root, 620, 420);
        stage.setTitle("Agencia — Eventos");
        stage.setScene(scene);
        stage.show();
    }

    private List<Lugar> createLugares() {
        return List.of(
                new Lugar("Sala Principal", "Calle Gran Vía 1", "Madrid", 500, TipoLugar.SALON),
                new Lugar("Estudio Norte", "Polígono Industrial 5", "Barcelona", 80, TipoLugar.ESTUDIO),
                new Lugar("Hotel Plaza", "Av. Diagonal 100", "Barcelona", 200, TipoLugar.HOTEL)
        );
    }

    private List<Modelo> createModelos() {
        return List.of(
                new Modelo("Laura M.", "ID-001", "laura@mail.com", "MOD-01", 1.75, "Alta costura", true),
                new Modelo("Carlos R.", "ID-002", "carlos@mail.com", "MOD-02", 1.82, "Comercial", false),
                new Modelo("Sofia L.", "ID-003", "sofia@mail.com", "MOD-03", 1.70, "Editorial", true)
        );
    }

    private List<Fotografo> createFotografos() {
        return List.of(
                new Fotografo("Pablo F.", "FOT-01", "pablo@studio.com", "Moda", 8),
                new Fotografo("Ana G.", "FOT-02", "ana@studio.com", "Retrato", 5)
        );
    }

    private List<Evento> createEventos() {
        Modelo[] modelosArr = modelos.toArray(new Modelo[0]);
        Fotografo[] fotosArr = fotografos.toArray(new Fotografo[0]);

        return List.of(
                new EventoPublico(
                        "Desfile Primavera 2025",
                        LocalDate.of(2025, 4, 15),
                        lugares.get(0),
                        new Modelo[]{modelos.get(0), modelos.get(2)},
                        fotosArr,
                        400,
                        "Marca Fashion"
                ),
                new EventoPrivado(
                        "Sesión exclusiva cliente",
                        LocalDate.of(2025, 3, 20),
                        lugares.get(1),
                        new Modelo[]{modelos.get(0)},
                        new Fotografo[]{fotografos.get(0)},
                        "Cliente Anónimo S.L.",
                        3
                ),
                new EventoPublico(
                        "Presentación colección",
                        LocalDate.of(2025, 5, 1),
                        lugares.get(2),
                        modelosArr,
                        fotosArr,
                        150,
                        "Hotel Plaza"
                )
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}
