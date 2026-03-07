package controller;

import clases.Agencia;
import clases.persistencia.ReadData;
import clases.persistencia.SaveData;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AgenciaController {

    @FXML private StackPane rootStack;
    @FXML private TextField searchField;
    @FXML private HBox tabBar;
    @FXML private ToggleGroup navGroup;
    @FXML private ToggleButton tabModelos;
    @FXML private ToggleButton tabFotografos;
    @FXML private ToggleButton tabEventos;
    @FXML private ToggleButton tabLugares;
    @FXML private StackPane contentStack;

    @FXML private VBox splashOverlay;
    @FXML private Label splashStatus;

    @FXML private VBox modalOverlay;
    @FXML private Label modalIcon;
    @FXML private Label modalTitle;
    @FXML private Label modalMessage;
    @FXML private HBox modalButtons;

    @FXML private VBox modelosView;
    @FXML private ModeloController modelosViewController;
    @FXML private VBox fotosView;
    @FXML private FotografoController fotosViewController;
    @FXML private VBox eventosView;
    @FXML private EventoController eventosViewController;
    @FXML private VBox lugaresView;
    @FXML private LugarController lugaresViewController;

    private static final String SAVE_PATH =
            System.getProperty("user.dir") + "/agencia.dat";

    private Agencia agencia = new Agencia();

    private static AgenciaController instance;

    @FXML
    public void initialize() {
        instance = this;

        showOnly(modelosView);
        updateActiveTab(tabModelos);
        passAgencia();

        navGroup.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            if (newT == null) {
                navGroup.selectToggle(oldT);
            }
        });

        startSplash();
        maximizeOnShow();
    }

    private void maximizeOnShow() {
        Platform.runLater(() -> {
            Stage stage = (Stage) rootStack.getScene().getWindow();
            stage.setMaximized(true);
        });
    }

    private void startSplash() {
        splashOverlay.setVisible(true);
        splashOverlay.setMouseTransparent(false);

        splashStatus.setText("Cargando datos...");

        PauseTransition loadDelay = new PauseTransition(Duration.seconds(1));
        loadDelay.setOnFinished(e -> {
            Agencia loaded = ReadData.read(SAVE_PATH);
            if (loaded != null) {
                agencia = loaded;
                passAgencia();
                splashStatus.setText("✔ Datos cargados correctamente");
                splashStatus.getStyleClass().add("splash-status-ok");
            } else {
                splashStatus.setText("— Sin datos previos, iniciando vacío");
                splashStatus.getStyleClass().add("splash-status-warn");
            }

            PauseTransition showResult = new PauseTransition(Duration.seconds(1.5));
            showResult.setOnFinished(ev -> {
                FadeTransition fadeOut = new FadeTransition(Duration.millis(500), splashOverlay);
                fadeOut.setFromValue(1);
                fadeOut.setToValue(0);
                fadeOut.setOnFinished(done -> {
                    splashOverlay.setVisible(false);
                    splashOverlay.setManaged(false);
                    splashOverlay.setMouseTransparent(true);
                    refreshCurrentTab();
                });
                fadeOut.play();
            });
            showResult.play();
        });
        loadDelay.play();
    }

    private void passAgencia() {
        modelosViewController.setAgencia(agencia);
        fotosViewController.setAgencia(agencia);
        eventosViewController.setAgencia(agencia);
        lugaresViewController.setAgencia(agencia);
    }

    private void showOnly(Node target) {
        for (Node child : contentStack.getChildren()) {
            boolean active = child == target;
            child.setVisible(active);
            child.setManaged(active);
        }
        target.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.millis(150), target);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    private void updateActiveTab(ToggleButton active) {
        for (Node n : tabBar.getChildren()) {
            if (n instanceof ToggleButton tb) {
                tb.getStyleClass().remove("nav-tab-active");
            }
        }
        active.getStyleClass().add("nav-tab-active");
    }

    // ── Tab handlers ──

    @FXML
    private void onTabModelos() {
        showOnly(modelosView);
        updateActiveTab(tabModelos);
        modelosViewController.refreshTable();
    }

    @FXML
    private void onTabFotografos() {
        showOnly(fotosView);
        updateActiveTab(tabFotografos);
        fotosViewController.refreshTable();
    }

    @FXML
    private void onTabEventos() {
        showOnly(eventosView);
        updateActiveTab(tabEventos);
        eventosViewController.refreshTable();
    }

    @FXML
    private void onTabLugares() {
        showOnly(lugaresView);
        updateActiveTab(tabLugares);
        lugaresViewController.refreshTable();
    }

    // ── Save / Load ──

    @FXML
    private void onGuardar() {
        SaveData.save(SAVE_PATH, agencia);
        showInfo("Guardado", "Datos guardados correctamente.");
    }

    @FXML
    private void onCargar() {
        Agencia loaded = ReadData.read(SAVE_PATH);
        if (loaded != null) {
            agencia = loaded;
            passAgencia();
            refreshCurrentTab();
            showInfo("Cargado", "Datos cargados correctamente.");
        } else {
            showError("Error", "No se encontró archivo de datos.");
        }
    }

    @FXML
    private void onSearch() {
        String query = searchField.getText().trim().toLowerCase();
        ToggleButton selected = (ToggleButton) navGroup.getSelectedToggle();
        if (selected == tabModelos) modelosViewController.search(query);
        else if (selected == tabFotografos) fotosViewController.search(query);
        else if (selected == tabEventos) eventosViewController.search(query);
        else if (selected == tabLugares) lugaresViewController.search(query);
    }

    private void refreshCurrentTab() {
        ToggleButton selected = (ToggleButton) navGroup.getSelectedToggle();
        if (selected == tabModelos) modelosViewController.refreshTable();
        else if (selected == tabFotografos) fotosViewController.refreshTable();
        else if (selected == tabEventos) eventosViewController.refreshTable();
        else if (selected == tabLugares) lugaresViewController.refreshTable();
    }

    // ── In-app modal system ──

    @FXML
    private void onModalClose() {
        hideModal();
    }

    private void displayModal(String icon, String title, String message,
                              String titleStyleClass, Runnable onConfirm) {
        modalIcon.setText(icon);
        modalTitle.setText(title);
        modalTitle.getStyleClass().removeAll(
                "modal-title-error", "modal-title-info", "modal-title-confirm");
        modalTitle.getStyleClass().add(titleStyleClass);
        modalMessage.setText(message);
        modalButtons.getChildren().clear();

        if (onConfirm != null) {
            Button btnNo = new Button("Cancelar");
            btnNo.getStyleClass().add("btn-outline");
            btnNo.setOnAction(e -> hideModal());

            Button btnYes = new Button("Confirmar");
            btnYes.getStyleClass().add("btn-primary");
            btnYes.setOnAction(e -> {
                hideModal();
                onConfirm.run();
            });

            modalButtons.getChildren().addAll(btnNo, btnYes);
        } else {
            Button btnOk = new Button("Aceptar");
            btnOk.getStyleClass().add("btn-primary");
            btnOk.setOnAction(e -> hideModal());
            modalButtons.getChildren().add(btnOk);
        }

        modalOverlay.setOpacity(0);
        modalOverlay.setVisible(true);
        modalOverlay.setManaged(true);
        FadeTransition ft = new FadeTransition(Duration.millis(150), modalOverlay);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    private void hideModal() {
        FadeTransition ft = new FadeTransition(Duration.millis(100), modalOverlay);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setOnFinished(e -> {
            modalOverlay.setVisible(false);
            modalOverlay.setManaged(false);
        });
        ft.play();
    }

    // ── Static accessors for sub-controllers ──

    public static void showError(String title, String message) {
        instance.displayModal("⚠", title, message, "modal-title-error", null);
    }

    public static void showInfo(String title, String message) {
        instance.displayModal("ℹ", title, message, "modal-title-info", null);
    }

    public static void showConfirm(String title, String message, Runnable onConfirm) {
        instance.displayModal("❓", title, message, "modal-title-confirm", onConfirm);
    }

    public static void saveData() {
        SaveData.save(SAVE_PATH, instance.agencia);
    }

    public static void refreshAll() {
        instance.modelosViewController.refreshTable();
        instance.fotosViewController.refreshTable();
        instance.eventosViewController.refreshTable();
        instance.lugaresViewController.refreshTable();
    }
}
