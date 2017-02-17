/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hurtowniedanych;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author daniel
 */
public class Utils {
          static void alert(String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, content, ButtonType.OK);
        switch (alertType) {
            case ERROR:
                alert.setTitle("Błąd");
                break;
            case INFORMATION:
                alert.setTitle("Informacja");
                break;
            case WARNING:
                alert.setTitle("Ostrzeżenie!");
                break;
        }
        alert.setHeaderText(header);
        alert.showAndWait();
    }
    
}
