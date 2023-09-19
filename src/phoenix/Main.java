/*
    THIS PROJECT HAS BEEN ABANDONED SINCE FIREBIRD VERSIONS HAVE TOO MANY INCONSISTENCIES BETWEEN EACH OTHER.
    THE INTENT OF THIS PROJECT WAS TO SUPPORT FIREBIRD 2.5 AND 3, PRIMARILY FOR MEDILAB SISTEMAS COMPANY (WHERE I WORKED).
    SINCE THERE ARE OTHER TOOLS AVAILABLE, THIS SOFTWARE WILL NO LONGER BE DEVELOPED.
    MICHAEL LEOCÁDIO
*/
package phoenix;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import phoenix.resource.Strings;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(Strings.FORM_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle(Strings.PROGRAM_FULL_TITLE + " " + Strings.PROGRAM_RAW_VERSION);
        primaryStage.setScene(new Scene(root));

        // Disable window resizing
        primaryStage.setResizable(false);

        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            System.out.println("Has security manager.");
        } else {
            System.out.println("No security manager.");
        }

        primaryStage.show();
    }
}