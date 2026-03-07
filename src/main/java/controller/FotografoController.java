package controller;

import clases.Agencia;
import clases.dominio.personas.Fotografo;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class FotografoController {

    @FXML private Label sectionTitle;
    @FXML private Label sectionSubtitle;
    @FXML private ComboBox<String> filterEspecialidad;
    @FXML private TableView<Fotografo> table;
    @FXML private TableColumn<Fotografo, String> colId;
    @FXML private TableColumn<Fotografo, String> colNombre;
    @FXML private TableColumn<Fotografo, String> colEspecialidad;
    @FXML private TableColumn<Fotografo, String> colExperiencia;
    @FXML private TableColumn<Fotografo, String> colContacto;
    @FXML private TableColumn<Fotografo, Void> colAcciones;

    @FXML private VBox formPanel;
    @FXML private Label formTitle;
    @FXML private TextField fieldNombre;
    @FXML private TextField fieldIdentificacion;
    @FXML private TextField fieldContacto;
    @FXML private ComboBox<String> fieldEspecialidad;
    @FXML private TextField fieldExperiencia;

    private Agencia agencia;
    private Fotografo editingFotografo;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getIdentificacion()));
        colNombre.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNombre()));
        colEspecialidad.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getEspecialidad()));
        colExperiencia.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getAniosExperiencia() + " años"));
        colContacto.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getContacto()));

        setupActionColumn();
        setupFilters();
        formPanel.setTranslateX(420);
    }

    private void setupFilters() {
        String[] especialidades = {"Todas", "Retrato", "Moda", "Producto", "Eventos", "Naturaleza", "Deportes"};
        filterEspecialidad.setItems(FXCollections.observableArrayList(especialidades));

        fieldEspecialidad.setItems(FXCollections.observableArrayList(
                "Retrato", "Moda", "Producto", "Eventos", "Naturaleza", "Deportes"));
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
                    Fotografo f = getTableView().getItems().get(getIndex());
                    showInfo(f);
                });
                btnEditar.setOnAction(e -> {
                    Fotografo f = getTableView().getItems().get(getIndex());
                    openEditForm(f);
                });
                btnEliminar.setOnAction(e -> {
                    Fotografo f = getTableView().getItems().get(getIndex());
                    onDelete(f);
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
        Fotografo[] fotografos = agencia.getFotografos();
        ObservableList<Fotografo> list = FXCollections.observableArrayList(fotografos);
        table.setItems(list);
        sectionSubtitle.setText(fotografos.length + " registrados");
    }

    public void search(String query) {
        if (agencia == null) return;
        Fotografo[] all = agencia.getFotografos();
        ObservableList<Fotografo> filtered = FXCollections.observableArrayList();
        for (Fotografo f : all) {
            if (f.getNombre().toLowerCase().contains(query)
                    || f.getIdentificacion().toLowerCase().contains(query)
                    || f.getEspecialidad().toLowerCase().contains(query)) {
                filtered.add(f);
            }
        }
        table.setItems(filtered);
        sectionSubtitle.setText(filtered.size() + " encontrados");
    }

    @FXML
    private void onFilter() {
        if (agencia == null) return;
        String esp = filterEspecialidad.getValue();
        Fotografo[] all = agencia.getFotografos();
        ObservableList<Fotografo> filtered = FXCollections.observableArrayList();

        for (Fotografo f : all) {
            boolean match = esp == null || "Todas".equals(esp)
                    || f.getEspecialidad().equalsIgnoreCase(esp);
            if (match) filtered.add(f);
        }
        table.setItems(filtered);
        sectionSubtitle.setText(filtered.size() + " encontrados");
    }

    @FXML
    private void onReload() {
        AgenciaController.reloadData();
    }

    @FXML
    private void onNuevo() {
        editingFotografo = null;
        formTitle.setText("Registrar Fotógrafo");
        clearForm();
        fieldIdentificacion.setDisable(false);
        slideIn();
    }

    private void openEditForm(Fotografo f) {
        editingFotografo = f;
        formTitle.setText("Editar Fotógrafo");
        fieldNombre.setText(f.getNombre());
        fieldIdentificacion.setText(f.getIdentificacion());
        fieldIdentificacion.setDisable(true);
        fieldContacto.setText(f.getContacto());
        fieldEspecialidad.setValue(f.getEspecialidad());
        fieldExperiencia.setText(String.valueOf(f.getAniosExperiencia()));
        slideIn();
    }

    @FXML
    private void onSaveForm() {
        try {
            String nombre = fieldNombre.getText().trim();
            String id = fieldIdentificacion.getText().trim();
            String contacto = fieldContacto.getText().trim();
            String especialidad = fieldEspecialidad.getValue();
            int experiencia = Integer.parseInt(fieldExperiencia.getText().trim());

            if (nombre.isEmpty() || id.isEmpty() || contacto.isEmpty() || especialidad == null) {
                AgenciaController.showError("Campos obligatorios",
                        "Todos los campos son obligatorios.");
                return;
            }

            if (editingFotografo == null) {
                Fotografo nuevo = new Fotografo(nombre, id, contacto, especialidad, experiencia);
                agencia.registrarFotografo(nuevo);
            } else {
                editingFotografo.setNombre(nombre);
                editingFotografo.setContacto(contacto);
                editingFotografo.setEspecialidad(especialidad);
                editingFotografo.setAniosExperiencia(experiencia);
            }
            AgenciaController.saveData();
            slideOut();
            AgenciaController.refreshAll();
        } catch (NumberFormatException ex) {
            AgenciaController.showError("Error de formato",
                    "Los años de experiencia deben ser un número entero.");
        } catch (Exception ex) {
            AgenciaController.showError("Error", ex.getMessage());
        }
    }

    @FXML
    private void onCancelForm() {
        slideOut();
    }

    private void onDelete(Fotografo f) {
        AgenciaController.showConfirm("Confirmar eliminación",
                "¿Eliminar a " + f.getNombre() + "?", () -> {
                    agencia.eliminarFotografo(f.getIdentificacion());
                    refreshTable();
                });
    }

    private void showInfo(Fotografo f) {
        AgenciaController.showInfo("Detalle del Fotógrafo", f.mostrarInformacion());
    }

    private void clearForm() {
        fieldNombre.clear();
        fieldIdentificacion.clear();
        fieldContacto.clear();
        fieldEspecialidad.setValue(null);
        fieldExperiencia.clear();
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
