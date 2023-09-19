package phoenix.auxiliar;

import javafx.scene.control.Alert;
import phoenix.resource.Strings;

import java.awt.*;

/**
 * Project: Phoenix
 * Package: phoenix.auxiliar
 * Created by leoca_000 on 3/28/2016.
 */

public class Alerter {
    public static void emit(Alert.AlertType type, String sender, String message) {
        Alert a = new Alert(type);
        a.setTitle(Strings.PROGRAM_SHORT_TITLE);
        a.setHeaderText(sender);
        a.setContentText(message);

        Toolkit.getDefaultToolkit().beep();
        try {
            a.showAndWait();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Alerter.emitException();
        }
        return;
    }

    public static void emitSecurityException() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText("Not enough user privileges.");
        Toolkit.getDefaultToolkit().beep();
        try {
            a.showAndWait();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Alerter.emitException();
        }
    }

    public static void emitException() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(Strings.PROGRAM_SHORT_TITLE);
        a.setContentText("A program exception occurred.");
        try {
            a.showAndWait();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            //Alerter.emitException(); That would be awesome. Would.
        }
    }
}
