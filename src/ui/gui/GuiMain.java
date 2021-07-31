package ui.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The entry point of the application. Run the <code>main()</code> method to start the application.
 *
 * @author Samuel A. Kosasih
 */
public class GuiMain extends Application {

    /**
     * The main entry point for this JavaFX application.
     * <br><br>
     * Uses the <code>FXMLLoader</code> class to create a <code>Scene</code> using the
     * <code>mainWindow.fxml</code> file.
     *
     * @param stage the primary stage for this application, onto which the application scene can be set.
     *
     * @see Application
     * @see Stage
     * @see Scene
     * @see Parent
     * @see FXMLLoader
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        stage.setTitle("Bank Application");
        stage.setScene(new Scene(root, 850, 600));
        stage.setResizable(false);
        stage.setOnHidden(windowEvent -> controller.shutdown());
        stage.show();
    }

    /**
     * Launches the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
