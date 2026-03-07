package controller;

import clases.Agencia;
import clases.dominio.eventos.Evento;
import clases.dominio.eventos.EventoPrivado;
import clases.dominio.eventos.EventoPublico;
import clases.dominio.eventos.Lugar;
import clases.dominio.personas.Fotografo;
import clases.dominio.personas.Modelo;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class EventoController {

    @FXML private Label sectionTitle;
    @FXML private Label sectionSubtitle;
    @FXML private ToggleGroup tipoFilterGroup;
    @FXML private ToggleButton filterTodos;
    @FXML private ToggleButton filterPublico;
    @FXML private ToggleButton filterPrivado;

    @FXML private TableView<Evento> table;
    @FXML private TableColumn<Evento, String> colNombre;
    @FXML private TableColumn<Evento, String> colFecha;
    @FXML private TableColumn<Evento, String> colTipo;
    @FXML private TableColumn<Evento, String> colLugar;
    @FXML private TableColumn<Evento, String> colParticipantes;
    @FXML private TableColumn<Evento, Void> colAcciones;

    @FXML private VBox formPanel;
    @FXML private Label formTitle;
    @FXML private ToggleGroup tipoEventoGroup;
    @FXML private ToggleButton btnTipoPublico;
    @FXML private ToggleButton btnTipoPrivado;
    @FXML private TextField fieldNombre;
    @FXML private DatePicker fieldFecha;
    @FXML private ComboBox<Lugar> fieldLugar;

    @FXML private VBox publicFields;
    @FXML private TextField fieldCapacidad;
    @FXML private TextField fieldPatrocinador;

    @FXML private VBox privateFields;
    @FXML private TextField fieldCliente;
    @FXML private ComboBox<String> fieldConfidencialidad;

    @FXML private ListView<Modelo> fieldModelos;
    @FXML private ListView<Fotografo> fieldFotografos;

    private Agencia agencia;
    private Evento editingEvento;

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNombre()));
        colFecha.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getFecha().toString()));
        colTipo.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().tipoEvento()));
        colLugar.setCellValueFactory(c -> {
            Lugar l = c.getValue().getLugar();
            return new SimpleStringProperty(l != null ? l.getNombre() : "—");
        });
        colParticipantes.setCellValueFactory(c -> {
            int count = 0;
            for (Modelo m : c.getValue().getModelos()) if (m != null) count++;
            for (Fotografo f : c.getValue().getFotografos()) if (f != null) count++;
            return new SimpleStringProperty(String.valueOf(count));
        });

        colTipo.setCellFactory(col -> new TableCell<>() {
            private final Label badge = new Label();
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    badge.setText(item);
                    badge.getStyleClass().clear();
                    badge.getStyleClass().add(
                            item.contains("Público") ? "badge-active" : "badge-orange");
                    setGraphic(badge);
                }
            }
        });

        setupActionColumn();

        String[] niveles = {"1", "2", "3", "4", "5"};
        fieldConfidencialidad.setItems(FXCollections.observableArrayList(niveles));

        fieldModelos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        fieldFotografos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tipoFilterGroup.selectedToggleProperty().addListener((obs, o, n) -> {
            if (n == null) tipoFilterGroup.selectToggle(o);
        });

        formPanel.setTranslateX(420);
    }

    private void setupActionColumn() {
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnVer = new Button("Ver ▶");
            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox box = new HBox(6, btnVer, btnEditar, btnEliminar);

            {
                btnVer.getStyleClass().add("btn-action");
                btnEditar.getStyleClass().add("btn-action");
                btnEliminar.getStyleClass().add("btn-danger");

                btnVer.setOnAction(e -> {
                    Evento ev = getTableView().getItems().get(getIndex());
                    showInfo(ev);
                });
                btnEditar.setOnAction(e -> {
                    Evento ev = getTableView().getItems().get(getIndex());
                    openEditForm(ev);
                });
                btnEliminar.setOnAction(e -> {
                    Evento ev = getTableView().getItems().get(getIndex());
                    onDelete(ev);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    public void setAgencia(Agencia agencia) {
        this.agencia = agencia;
        refreshTable();
    }

    public void refreshTable() {
        if (agencia == null) return;
        Evento[] eventos = agencia.getEventos();
        ObservableList<Evento> list = FXCollections.observableArrayList();
        for (Evento ev : eventos) {
            if (ev != null) list.add(ev);
        }
        table.setItems(list);
        sectionSubtitle.setText(list.size() + " registrados");
    }

    public void search(String query) {
        if (agencia == null) return;
        Evento[] all = agencia.getEventos();
        ObservableList<Evento> filtered = FXCollections.observableArrayList();
        for (Evento ev : all) {
            if (ev != null && ev.getNombre().toLowerCase().contains(query)) {
                filtered.add(ev);
            }
        }
        table.setItems(filtered);
        sectionSubtitle.setText(filtered.size() + " encontrados");
    }

    @FXML
    private void onFilter() {
        if (agencia == null) return;
        Evento[] all = agencia.getEventos();
        ObservableList<Evento> filtered = FXCollections.observableArrayList();
        Toggle selected = tipoFilterGroup.getSelectedToggle();

        for (Evento ev : all) {
            if (ev == null) continue;
            if (selected == filterTodos) {
                filtered.add(ev);
            } else if (selected == filterPublico && ev instanceof EventoPublico) {
                filtered.add(ev);
            } else if (selected == filterPrivado && ev instanceof EventoPrivado) {
                filtered.add(ev);
            }
        }
        table.setItems(filtered);
        sectionSubtitle.setText(filtered.size() + " encontrados");
    }

    @FXML
    private void onTipoChanged() {
        boolean isPublico = btnTipoPublico.isSelected();
        publicFields.setVisible(isPublico);
        publicFields.setManaged(isPublico);
        privateFields.setVisible(!isPublico);
        privateFields.setManaged(!isPublico);
    }

    @FXML
    private void onReload() {
        AgenciaController.reloadData();
    }

    @FXML
    private void onNuevo() {
        editingEvento = null;
        formTitle.setText("Registrar Evento");
        clearForm();
        btnTipoPublico.setSelected(true);
        btnTipoPublico.setDisable(false);
        btnTipoPrivado.setDisable(false);
        onTipoChanged();
        loadFormLists();
        slideIn();
    }

    private void openEditForm(Evento ev) {
        editingEvento = ev;
        formTitle.setText("Editar Evento");
        loadFormLists();

        fieldNombre.setText(ev.getNombre());
        fieldFecha.setValue(ev.getFecha());
        fieldLugar.setValue(ev.getLugar());

        if (ev instanceof EventoPublico pub) {
            btnTipoPublico.setSelected(true);
            fieldCapacidad.setText(String.valueOf(pub.getCapacidad()));
            fieldPatrocinador.setText(pub.getPatrocinador());
        } else if (ev instanceof EventoPrivado priv) {
            btnTipoPrivado.setSelected(true);
            fieldCliente.setText(priv.getCliente());
            fieldConfidencialidad.setValue(
                    String.valueOf(priv.getNivelDeConfidencialidad()));
        }

        btnTipoPublico.setDisable(true);
        btnTipoPrivado.setDisable(true);
        onTipoChanged();

        preselectParticipants(ev);
        slideIn();
    }

    private void preselectParticipants(Evento ev) {
        for (Modelo m : ev.getModelos()) {
            if (m != null) {
                int idx = fieldModelos.getItems().indexOf(m);
                if (idx >= 0) fieldModelos.getSelectionModel().select(idx);
            }
        }
        for (Fotografo f : ev.getFotografos()) {
            if (f != null) {
                int idx = fieldFotografos.getItems().indexOf(f);
                if (idx >= 0) fieldFotografos.getSelectionModel().select(idx);
            }
        }
    }

    private void loadFormLists() {
        if (agencia == null) return;

        ObservableList<Lugar> lugarList = FXCollections.observableArrayList();
        for (Lugar l : agencia.getLugares()) if (l != null) lugarList.add(l);
        fieldLugar.setItems(lugarList);

        ObservableList<Modelo> modeloList = FXCollections.observableArrayList();
        for (Modelo m : agencia.getModelos()) if (m != null) modeloList.add(m);
        fieldModelos.setItems(modeloList);

        ObservableList<Fotografo> fotoList = FXCollections.observableArrayList();
        for (Fotografo f : agencia.getFotografos()) if (f != null) fotoList.add(f);
        fieldFotografos.setItems(fotoList);
    }

    @FXML
    private void onSaveForm() {
        try {
            String nombre = fieldNombre.getText().trim();
            if (nombre.isEmpty() || fieldFecha.getValue() == null || fieldLugar.getValue() == null) {
                AgenciaController.showError("Campos obligatorios",
                        "Nombre, fecha y lugar son obligatorios.");
                return;
            }

            ObservableList<Modelo> selModelos = fieldModelos.getSelectionModel().getSelectedItems();
            for (Modelo m : selModelos) {
                if (!m.isDisponibilidad()) {
                    AgenciaController.showError("Modelo no disponible",
                            m.getNombre() + " no está disponible y no puede ser asignado al evento.");
                    return;
                }
            }

            Modelo[] modArr = new Modelo[selModelos.size()];
            for (int i = 0; i < selModelos.size(); i++) modArr[i] = selModelos.get(i);

            ObservableList<Fotografo> selFotos = fieldFotografos.getSelectionModel().getSelectedItems();
            Fotografo[] fotArr = new Fotografo[selFotos.size()];
            for (int i = 0; i < selFotos.size(); i++) fotArr[i] = selFotos.get(i);

            if (editingEvento == null) {
                if (btnTipoPublico.isSelected()) {
                    int cap = Integer.parseInt(fieldCapacidad.getText().trim());
                    String pat = fieldPatrocinador.getText().trim();
                    EventoPublico ep = new EventoPublico(nombre, fieldFecha.getValue(),
                            fieldLugar.getValue(), modArr, fotArr, cap, pat);
                    agencia.registrarEvento(ep);
                } else {
                    String cliente = fieldCliente.getText().trim();
                    int nivel = Integer.parseInt(fieldConfidencialidad.getValue());
                    EventoPrivado ep = new EventoPrivado(nombre, fieldFecha.getValue(),
                            fieldLugar.getValue(), modArr, fotArr, cliente, nivel);
                    agencia.registrarEvento(ep);
                }
            } else {
                editingEvento.setNombre(nombre);
                editingEvento.setFecha(fieldFecha.getValue());
                editingEvento.setLugar(fieldLugar.getValue());
                editingEvento.setModelos(modArr);
                editingEvento.setFotografos(fotArr);

                if (editingEvento instanceof EventoPublico pub) {
                    pub.setCapacidad(Integer.parseInt(fieldCapacidad.getText().trim()));
                    pub.setPatrocinador(fieldPatrocinador.getText().trim());
                } else if (editingEvento instanceof EventoPrivado priv) {
                    priv.setCliente(fieldCliente.getText().trim());
                    priv.setNivelDeConfidencialidad(
                            Integer.parseInt(fieldConfidencialidad.getValue()));
                }
            }
            AgenciaController.saveData();
            slideOut();
            AgenciaController.refreshAll();
        } catch (NumberFormatException ex) {
            AgenciaController.showError("Error de formato",
                    "Capacidad y nivel deben ser números válidos.");
        } catch (Exception ex) {
            AgenciaController.showError("Error", ex.getMessage());
        }
    }

    @FXML
    private void onCancelForm() {
        slideOut();
    }

    private void onDelete(Evento ev) {
        AgenciaController.showConfirm("Confirmar eliminación",
                "¿Eliminar evento " + ev.getNombre() + "?", () -> {
                    agencia.eliminarEvento(ev.getNombre());
                    refreshTable();
                });
    }

    private void showInfo(Evento ev) {
        AgenciaController.showInfo("Detalle del Evento", ev.mostrarDetalles());
    }

    private void clearForm() {
        fieldNombre.clear();
        fieldFecha.setValue(null);
        fieldLugar.setValue(null);
        fieldCapacidad.clear();
        fieldPatrocinador.clear();
        fieldCliente.clear();
        fieldConfidencialidad.setValue(null);
        fieldModelos.getSelectionModel().clearSelection();
        fieldFotografos.getSelectionModel().clearSelection();
    }

    private void slideIn() {
        formPanel.setVisible(true);
        formPanel.setManaged(true);
        TranslateTransition tt = new TranslateTransition(Duration.millis(250), formPanel);
        tt.setFromX(420);
        tt.setToX(0);
        tt.play();
    }

    private void slideOut() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(250), formPanel);
        tt.setFromX(0);
        tt.setToX(420);
        tt.setOnFinished(e -> {
            formPanel.setVisible(false);
            formPanel.setManaged(false);
        });
        tt.play();
    }
}
