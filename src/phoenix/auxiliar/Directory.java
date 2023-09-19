package phoenix.auxiliar;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import phoenix.resource.Strings;

import java.io.File;

/**
 * Project: Phoenix
 * Package: phoenix.auxiliar
 * Created by leoca_000 on 3/28/2016.
 */

public class Directory {
    public static String dbFileSelector() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Database");
        fileChooser.setInitialDirectory(new File(System.getProperty(Strings.INITIAL_DIRECTORY)));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All supported databases", "*.fdb;*.gdb"),
                new FileChooser.ExtensionFilter("Firebird", "*.fdb"),
                new FileChooser.ExtensionFilter("Interbase", "*.gdb")
        );

        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            return file.toString();
        } else return "";
    }

    public static String targetDirSelector() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory");
        try {
            directoryChooser.setInitialDirectory(new File(System.getProperty(Strings.INITIAL_DIRECTORY)));
        } catch (SecurityException e) {
            e.printStackTrace();
            Alerter.emitSecurityException();
        }

        File file = directoryChooser.showDialog(new Stage());
        if (file != null) {
            return (file.toString() + "\\");
        } else return "";
    }
}
