package phoenix.auxiliar;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import phoenix.Controller;
import phoenix.resource.Strings;

import java.awt.*;
import java.io.*;

/**
 * Project: Phoenix
 * Package: phoenix.auxiliar
 * Created by leoca_000 on 3/28/2016.
 */

public class Verbose {
    public static void log() {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Phoenix");
        a.setHeaderText("Backup");
        a.setContentText("Done.");

        TextArea textArea = new TextArea(Verbose.getLog() + "\nDuration: " + Controller.totalDuration);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        AnchorPane logContent = new AnchorPane(textArea);
        logContent.setTopAnchor(textArea, 0.0);
        logContent.setBottomAnchor(textArea, 0.0);
        logContent.setLeftAnchor(textArea, 0.0);
        logContent.setRightAnchor(textArea, 0.0);

        a.getDialogPane().setExpandableContent(logContent);

        Toolkit.getDefaultToolkit().beep();
        try {
            a.showAndWait();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Alerter.emitException();
        }
    }

    public static void recycle() {
        // Delete previous log file
        File f = new File(Strings.LOG_FILE);
        try {
            if (f.exists()) {
                f.delete();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            Alerter.emitSecurityException();
        }
    }

    public static int report() {
        // If GBAK execution returned 1 (not OK), it checks its log and parse what happened.
        String s = Verbose.getLog();
        if (s.contains("password")) {
            return 1;
        } else if (s.contains("ERROR:I/O")) {
            return 2;
        } //else if (s.contains("can't"))
        return 0;
    }

    public static String getLog() {
        String line;
        String log = "";
        BufferedReader inputStream;
        try {
            inputStream = new BufferedReader(new FileReader(Strings.LOG_FILE));
            while ((line = inputStream.readLine()) != null) {
                log += line;
                log += System.lineSeparator();
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Alerter.emitException();
        } catch (IOException e) {
            e.printStackTrace();
            Alerter.emitException();
        }
        return log;
    }
}
