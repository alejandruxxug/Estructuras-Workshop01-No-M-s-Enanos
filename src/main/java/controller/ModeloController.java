package controller;

import clases.Agencia;
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

public class ModeloController {

    @FXML private Label sectionTitle;
    @FXML private Label sectionSubtitle;
    @FXML private ComboBox<String> filterCategoria;
    @FXML private ComboBox<String> filterDisponibilidad;
    @FXML private TableView<Modelo> table;
    @FXML private TableColumn<Modelo, String> colCodigo;
    @FXML private TableColumn<Modelo, String> colNombre;
    @FXML private TableColumn<Modelo, String> colEstatura;
    @FXML private TableColumn<Modelo, String> colCategoria;
    @FXML private TableColumn<Modelo, String> colDisponibilidad;
    @FXML private TableColumn<Modelo, Void> colAcciones;

    @FXML private VBox formPanel;
    @FXML private Label formTitle;
    @FXML private TextField fieldNombre;
    @FXML private TextField fieldIdentificacion;
    @FXML private TextField fieldContacto;
    @FXML private TextField fieldEstatura;
    @FXML private ComboBox<String> fieldCategoria;
    @FXML private ToggleButton fieldDisponibilidad;

    private Agencia agencia;
    private Modelo editingModelo;

    @FXML
    public void initialize() {
        colCodigo.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getCodigoModelo()));
        colNombre.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNombre()));
        colEstatura.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getEstatura())));
        colCategoria.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getCategoria()));
        colDisponibilidad.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().isDisponibilidad() ? "Disponible" : "No Disponible"));

        colDisponibilidad.setCellFactory(col -> new TableCell<>() {
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
                            "Disponible".equals(item) ? "badge-active" : "badge-inactive");
                    setGraphic(badge);
                }
            }
        });

        setupActionColumn();
        setupFilters();
        setupToggle();

        formPanel.setTranslateX(420);
    }

    private void setupFilters() {
        String[] categorias = {"Todas", "Pasarela", "Comercial", "Editorial", "Fitness", "Glamour"};
        filterCategoria.setItems(FXCollections.observableArrayList(categorias));

        String[] disponibilidades = {"Todas", "Disponible", "No Disponible"};
        filterDisponibilidad.setItems(FXCollections.observableArrayList(disponibilidades));

        fieldCategoria.setItems(FXCollections.observableArrayList(
                "Pasarela", "Comercial", "Editorial", "Fitness", "Glamour"));
    }

    private void setupToggle() {
        fieldDisponibilidad.selectedProperty().addListener((obs, o, n) ->
                fieldDisponibilidad.setText(n ? "Disponible" : "No Disponible"));
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
                    Modelo m = getTableView().getItems().get(getIndex());
                    showInfo(m);
                });
                btnEditar.setOnAction(e -> {
                    Modelo m = getTableView().getItems().get(getIndex());
                    openEditForm(m);
                });
                btnEliminar.setOnAction(e -> {
                    Modelo m = getTableView().getItems().get(getIndex());
                    onDelete(m);
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
        Modelo[] modelos = agencia.getModelos();
        ObservableList<Modelo> list = FXCollections.observableArrayList(modelos);
        table.setItems(list);
        sectionSubtitle.setText(modelos.length + " registrados");
    }

    public void search(String query) {
        if (agencia == null) return;
        Modelo[] all = agencia.getModelos();
        ObservableList<Modelo> filtered = FXCollections.observableArrayList();
        for (Modelo m : all) {
            if (m.getNombre().toLowerCase().contains(query)
                    || m.getCodigoModelo().toLowerCase().contains(query)
                    || m.getIdentificacion().toLowerCase().contains(query)) {
                filtered.add(m);
            }
        }
        table.setItems(filtered);
        sectionSubtitle.setText(filtered.size() + " encontrados");
    }

    @FXML
    private void onFilter() {
        if (agencia == null) return;
        String cat = filterCategoria.getValue();
        String disp = filterDisponibilidad.getValue();
        Modelo[] all = agencia.getModelos();
        ObservableList<Modelo> filtered = FXCollections.observableArrayList();

        for (Modelo m : all) {
            boolean matchCat = cat == null || "Todas".equals(cat)
                    || m.getCategoria().equalsIgnoreCase(cat);
            boolean matchDisp = disp == null || "Todas".equals(disp)
                    || ("Disponible".equals(disp) && m.isDisponibilidad())
                    || ("No Disponible".equals(disp) && !m.isDisponibilidad());
            if (matchCat && matchDisp) filtered.add(m);
        }
        table.setItems(filtered);
        sectionSubtitle.setText(filtered.size() + " encontrados");
    }

    @FXML
    private void onNuevo() {
        editingModelo = null;
        formTitle.setText("Registrar Modelo");
        clearForm();
        fieldIdentificacion.setDisable(false);
        slideIn();
    }

    private void openEditForm(Modelo m) {
        editingModelo = m;
        formTitle.setText("Editar Modelo");
        fieldNombre.setText(m.getNombre());
        fieldIdentificacion.setText(m.getIdentificacion());
        fieldIdentificacion.setDisable(true);
        fieldContacto.setText(m.getContacto());
        fieldEstatura.setText(String.valueOf(m.getEstatura()));
        fieldCategoria.setValue(m.getCategoria());
        fieldDisponibilidad.setSelected(m.isDisponibilidad());
        slideIn();
    }

    @FXML
    private void onSaveForm() {
        try {
            String nombre = fieldNombre.getText().trim();
            String id = fieldIdentificacion.getText().trim();
            String contacto = fieldContacto.getText().trim();
            double estatura = Double.parseDouble(fieldEstatura.getText().trim());
            String categoria = fieldCategoria.getValue();
            boolean disponibilidad = fieldDisponibilidad.isSelected();

            if (nombre.isEmpty() || id.isEmpty() || contacto.isEmpty() || categoria == null) {
                AgenciaController.showError("Campos obligatorios",
                        "Todos los campos son obligatorios.");
                return;
            }

            if (editingModelo == null) {
                Modelo nuevo = new Modelo(nombre, id, contacto, estatura, categoria, disponibilidad);
                agencia.registrarModelo(nuevo);
            } else {
                editingModelo.setNombre(nombre);
                editingModelo.setContacto(contacto);
                editingModelo.setEstatura(estatura);
                editingModelo.setCategoria(categoria);
                editingModelo.setDisponibilidad(disponibilidad);
            }
            AgenciaController.saveData();
            slideOut();
            AgenciaController.refreshAll();
        } catch (NumberFormatException ex) {
            AgenciaController.showError("Error de formato",
                    "La estatura debe ser un número válido.");
        } catch (Exception ex) {
            AgenciaController.showError("Error", ex.getMessage());
        }
    }

    @FXML
    private void onCancelForm() {
        slideOut();
    }

    private void onDelete(Modelo m) {
        AgenciaController.showConfirm("Confirmar eliminación",
                "¿Eliminar a " + m.getNombre() + "?", () -> {
                    agencia.eliminarModelo(m.getIdentificacion());
                    refreshTable();
                });
    }

    private void showInfo(Modelo m) {
        AgenciaController.showInfo("Detalle del Modelo", m.mostrarInformacion());
    }

    private void clearForm() {
        fieldNombre.clear();
        fieldIdentificacion.clear();
        fieldContacto.clear();
        fieldEstatura.clear();
        fieldCategoria.setValue(null);
        fieldDisponibilidad.setSelected(false);
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
