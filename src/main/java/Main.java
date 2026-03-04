import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        // Create UI elements (Nodes)
        Label label = new Label("Hello, JavaFX!");
        Button button = new Button("Click Me");

        // Add an event handler
        button.setOnAction(e -> label.setText("Button clicked!"));

        // Layout: VBox stacks things vertically
        VBox root = new VBox(10, label, button); // 10px spacing
        root.setPadding(new javafx.geometry.Insets(20));

        // Scene holds the layout, set size
        Scene scene = new Scene(root, 400, 300);

        // Stage is the window
        stage.setTitle("My First JavaFX App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args); // boots the JavaFX runtime
    }
}