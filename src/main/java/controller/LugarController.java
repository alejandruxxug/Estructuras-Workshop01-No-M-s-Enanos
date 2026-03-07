package controller;

import clases.Agencia;
import clases.dominio.eventos.Lugar;
import enums.TipoLugar;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class LugarController {

    @FXML private Label sectionTitle;
    @FXML private Label sectionSubtitle;
    @FXML private ComboBox<String> filterTipoLugar;
    @FXML private ComboBox<String> filterCiudad;
    @FXML private TableView<Lugar> table;
    @FXML private TableColumn<Lugar, String> colNombre;
    @FXML private TableColumn<Lugar, String> colCiudad;
    @FXML private TableColumn<Lugar, String> colDireccion;
    @FXML private TableColumn<Lugar, String> colCapacidad;
    @FXML private TableColumn<Lugar, String> colTipo;
    @FXML private TableColumn<Lugar, Void> colAcciones;

    @FXML private VBox formPanel;
    @FXML private Label formTitle;
    @FXML private TextField fieldNombre;
    @FXML private TextField fieldDireccion;
    @FXML private TextField fieldCiudad;
    @FXML private TextField fieldCapacidad;
    @FXML private ComboBox<TipoLugar> fieldTipoLugar;

    private Agencia agencia;
    private Lugar editingLugar;

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNombre()));
        colCiudad.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getCiudad()));
        colDireccion.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDireccion()));
        colCapacidad.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getCapacidadMaxima())));
        colTipo.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getTipoLugar().name()));

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
                    badge.getStyleClass().add("badge-orange");
                    setGraphic(badge);
                }
            }
        });

        setupActionColumn();
        setupFilters();
        formPanel.setTranslateX(420);
    }

    private void setupFilters() {
        ObservableList<String> tipos = FXCollections.observableArrayList("Todos");
        for (TipoLugar tl : TipoLugar.values()) tipos.add(tl.name());
        filterTipoLugar.setItems(tipos);

        filterCiudad.setItems(FXCollections.observableArrayList("Todas"));

        fieldTipoLugar.setItems(FXCollections.observableArrayList(TipoLugar.values()));
    }

    private void rebuildCiudadFilter() {
        ObservableList<String> ciudades = FXCollections.observableArrayList("Todas");
        if (agencia != null) {
            for (Lugar l : agencia.getLugares()) {
                if (l != null && !ciudades.contains(l.getCiudad())) {
                    ciudades.add(l.getCiudad());
                }
            }
        }
        filterCiudad.setItems(ciudades);
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
                    Lugar l = getTableView().getItems().get(getIndex());
                    showInfo(l);
                });
                btnEditar.setOnAction(e -> {
                    Lugar l = getTableView().getItems().get(getIndex());
                    openEditForm(l);
                });
                btnEliminar.setOnAction(e -> {
                    Lugar l = getTableView().getItems().get(getIndex());
                    onDelete(l);
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
        Lugar[] lugares = agencia.getLugares();
        ObservableList<Lugar> list = FXCollections.observableArrayList();
        for (Lugar l : lugares) {
            if (l != null) list.add(l);
        }
        table.setItems(list);
        sectionSubtitle.setText(list.size() + " registrados");
        rebuildCiudadFilter();
    }

    public void search(String query) {
        if (agencia == null) return;
        Lugar[] all = agencia.getLugares();
        ObservableList<Lugar> filtered = FXCollections.observableArrayList();
        for (Lugar l : all) {
            if (l != null && (l.getNombre().toLowerCase().contains(query)
                    || l.getCiudad().toLowerCase().contains(query)
                    || l.getDireccion().toLowerCase().contains(query))) {
                filtered.add(l);
            }
        }
        table.setItems(filtered);
        sectionSubtitle.setText(filtered.size() + " encontrados");
    }

    @FXML
    private void onFilter() {
        if (agencia == null) return;
        String tipo = filterTipoLugar.getValue();
        String ciudad = filterCiudad.getValue();
        Lugar[] all = agencia.getLugares();
        ObservableList<Lugar> filtered = FXCollections.observableArrayList();

        for (Lugar l : all) {
            if (l == null) continue;
            boolean matchTipo = tipo == null || "Todos".equals(tipo)
                    || l.getTipoLugar().name().equalsIgnoreCase(tipo);
            boolean matchCiudad = ciudad == null || "Todas".equals(ciudad)
                    || l.getCiudad().equalsIgnoreCase(ciudad);
            if (matchTipo && matchCiudad) filtered.add(l);
        }
        table.setItems(filtered);
        sectionSubtitle.setText(filtered.size() + " encontrados");
    }

    @FXML
    private void onNuevo() {
        editingLugar = null;
        formTitle.setText("Registrar Lugar");
        clearForm();
        fieldNombre.setDisable(false);
        slideIn();
    }

    private void openEditForm(Lugar l) {
        editingLugar = l;
        formTitle.setText("Editar Lugar");
        fieldNombre.setText(l.getNombre());
        fieldNombre.setDisable(true);
        fieldDireccion.setText(l.getDireccion());
        fieldCiudad.setText(l.getCiudad());
        fieldCapacidad.setText(String.valueOf(l.getCapacidadMaxima()));
        fieldTipoLugar.setValue(l.getTipoLugar());
        slideIn();
    }

    @FXML
    private void onSaveForm() {
        try {
            String nombre = fieldNombre.getText().trim();
            String direccion = fieldDireccion.getText().trim();
            String ciudad = fieldCiudad.getText().trim();
            int capacidad = Integer.parseInt(fieldCapacidad.getText().trim());
            TipoLugar tipoLugar = fieldTipoLugar.getValue();

            if (nombre.isEmpty() || direccion.isEmpty() || ciudad.isEmpty() || tipoLugar == null) {
                AgenciaController.showError("Campos obligatorios",
                        "Todos los campos son obligatorios.");
                return;
            }

            if (editingLugar == null) {
                Lugar nuevo = new Lugar(nombre, direccion, ciudad, capacidad, tipoLugar);
                agencia.registrarLugar(nuevo);
            } else {
                editingLugar.setDireccion(direccion);
                editingLugar.setCiudad(ciudad);
                editingLugar.setCapacidadMaxima(capacidad);
                editingLugar.setTipoLugar(tipoLugar);
            }
            AgenciaController.saveData();
            slideOut();
            AgenciaController.refreshAll();
        } catch (NumberFormatException ex) {
            AgenciaController.showError("Error de formato",
                    "La capacidad debe ser un número entero válido.");
        } catch (Exception ex) {
            AgenciaController.showError("Error", ex.getMessage());
        }
    }

    @FXML
    private void onCancelForm() {
        slideOut();
    }

    private void onDelete(Lugar l) {
        AgenciaController.showConfirm("Confirmar eliminación",
                "¿Eliminar lugar " + l.getNombre() + "?", () -> {
                    agencia.eliminarLugar(l.getNombre());
                    refreshTable();
                });
    }

    private void showInfo(Lugar l) {
        AgenciaController.showInfo("Detalle del Lugar",
                "Dirección: " + l.getDireccion()
                        + "\nCiudad: " + l.getCiudad()
                        + "\nCapacidad: " + l.getCapacidadMaxima()
                        + "\nTipo: " + l.getTipoLugar());
    }

    private void clearForm() {
        fieldNombre.clear();
        fieldDireccion.clear();
        fieldCiudad.clear();
        fieldCapacidad.clear();
        fieldTipoLugar.setValue(null);
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
