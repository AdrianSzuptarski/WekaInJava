package hurtowniedanych;

import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 *
 * @author szupek
 */
public class HurtownieDanychKsiegarnie extends Application {
    
    public static Stage current;
    public static DB db;
    
    @Override
    public void start(Stage primaryStage) throws SQLException {      
        db = new DB();
        db.connect();      
        open_window("/hurtowniedanych/FXML.fxml", "Hurtownie Danych: Projekt Ksiegarniii");
  
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public static void open_window(String name, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HurtownieDanychKsiegarnie.class.getClass().getResource(name));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            stage.show();
            stage.setResizable(false);
            stage.setTitle(title);
            if(current != null) {
                close_window();
            }
            current = stage;
         } catch(Exception e) {
             alert("Błąd", "Nie udało się otworzyć nowego okna! "+e.getLocalizedMessage().toString());
         }
    }
    
    public static void alert(String title, String text) {
        try {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(text);
            alert.showAndWait();
        } catch(Exception e) {
            System.exit(0);
        }
    }
    
    public static void close_window() {
        try {
            Stage stage = (Stage) current.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            alert("Błąd", "Nie można zamknąć okna!");
        }
    }
    
}
