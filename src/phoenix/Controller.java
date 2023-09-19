package phoenix;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import phoenix.auxiliar.Alerter;
import phoenix.auxiliar.Directory;
import phoenix.auxiliar.Validation;
import phoenix.auxiliar.Verbose;
import phoenix.resource.Strings;

import java.io.IOException;

public class Controller {
    public static boolean gbakFlag;
    public static boolean gzipFlag;
    public static long startTime;
    public static long endTime;
    public static long taskDurationS;
    public static long taskDurationM;
    public static long taskDurationH;
    public static String totalDuration;
    private static String gbakString;
    private static String gzipString;

    static {
        gbakFlag = false;
        gzipFlag = false;
        taskDurationS = 0;
        taskDurationM = 0;
        taskDurationH = 0;
    }

    @FXML
    public TextField dbPathField1;
    @FXML
    public TextField dbPathField2;
    @FXML
    private TextField dirPathField1;
    @FXML
    private TextField dirPathField2;
    @FXML
    private TextField dirPathField3;
    @FXML
    private TextField userField;
    @FXML
    private TextField pwdField;
    @FXML
    private CheckBox bkpOpt1; // GZip
    @FXML
    private CheckBox bkpOpt2; // Convert
    @FXML
    private CheckBox clnOpt1; // Metadata
    @FXML
    private Button backupButton;
    @FXML
    private Button restoreButton;
    @FXML
    private Button cloneButton;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label statusLabel;

    public void selectDBFile() {
        String s = new String(Directory.dbFileSelector());
        if(!s.equals("")) {
            dbPathField1.setText(s);
            dbPathField2.setText(s);
        }
    }

    public void selectTargetDir() {
        String s = new String(Directory.targetDirSelector());
        if(!s.equals("")) {
            dirPathField1.setText(s);
            dirPathField2.setText(s);
            dirPathField3.setText(s);
        }
    }

    public void backupNow() {

        // First we validate all required fields for this action
        if (!Validation.isValidField(dbPathField1.getText())) {
            Alerter.emit(Alert.AlertType.ERROR, Strings.ACTION_BACKUP, "All fields required.");
            return;
        }
        if (!Validation.isValidField(dirPathField1.getText())) {
            Alerter.emit(Alert.AlertType.ERROR, Strings.ACTION_BACKUP, "All fields required.");
            return;
        }
        // We would disable it, but it seems Firebird really does not allow empty username and/or password
        if (!Validation.isValidField(userField.getText())) {
            Alerter.emit(Alert.AlertType.ERROR, Strings.ACTION_BACKUP, "All fields required.");
            return;
        }
        if (!Validation.isValidField(pwdField.getText())) {
            Alerter.emit(Alert.AlertType.ERROR, Strings.ACTION_BACKUP, "All fields required.");
            return;
        }

        // Second we validate the database
        if (!Validation.isValidDB(dbPathField1.getText())) {
            Alerter.emit(Alert.AlertType.ERROR, Strings.ACTION_BACKUP, "Database file not found.");
            return;
        }

        // Finally we validate Firebird services
        if (!Validation.isValidService()) {
            Alerter.emit(Alert.AlertType.ERROR, Strings.ACTION_BACKUP, "Firebird services are not running.");
            return;
        }

        // Assemble gbak command
        gbakString = Strings.GBAK_DEFAULT_COMMAND;
        if (bkpOpt2.isSelected()) gbakString += " -convert";
        gbakString += " -user " + userField.getText();
        gbakString += " -password " + pwdField.getText();
        gbakString += " " + dbPathField1.getText();
        gbakString += " " + dirPathField1.getText();
        gbakString += Strings.OUTPUT_BACKUP_FILE;

        System.out.println(gbakString);

        if (bkpOpt1.isSelected()) {
            // If GZip
            gzipString = Strings.GZIP_DEFAULT_COMMAND;
            gzipString += " " + dirPathField1.getText();
            gzipString += Strings.OUTPUT_BACKUP_FILE;

            System.out.println(gzipString);
        }

        // Disable command button right before we start
        backupButton.setDisable(true);

        // Start progress bar
        progressBar.setProgress(-1);
        statusLabel.setText("Processing Backup");

        // Delete previous log (mandatory by gbak for it to function properly!)
        Verbose.recycle();

        // Execute
        Task task = new Task() {
            Process gbak, gzip;

            @Override
            protected String call() {
                // GBAK
                gbakFlag = true;
                startTime = System.currentTimeMillis();

                try {
                    gbak = Runtime.getRuntime().exec(Controller.gbakString);
                } catch (SecurityException e) {
                    e.printStackTrace();
                    Alerter.emitSecurityException();
                } catch (IOException e) {
                    e.printStackTrace();
                    Alerter.emitException();
                }

                while (gbak.isAlive()) {
                }

                // If GBAK was not OK
                if (gbak.exitValue() != 0) return Integer.toString(gbak.exitValue());

                if (bkpOpt1.isSelected()) {
                    // GZIP
                    gzipFlag = true;
                    // TODO: [Suggestion] Be able to set proper loading label at gzip task point.
                    //statusLabel.setText("Compressing");
                    try {
                        gzip = Runtime.getRuntime().exec(Controller.gzipString);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                        Alerter.emitSecurityException();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Alerter.emitException();
                    }
                    while (gzip.isAlive()) {
                    }
                }

                endTime = System.currentTimeMillis();
                calculateTotalDuration();

                return Integer.toString(gbak.exitValue());
            }

            @Override
            protected void succeeded() {
                progressBar.setProgress(0);
                statusLabel.setText("");
                backupButton.setDisable(false);

                switch (gbak.exitValue()) {
                    case 0:
                        Verbose.log();
                        break;

                    case 1:
                        // Parse what happened
                        int r = Verbose.report();
                        if (r == 1) {
                            // Invalid username/password
                            Alerter.emit(Alert.AlertType.ERROR, Strings.ACTION_BACKUP, "Invalid username/password.");
                        } else if (r == 2) {
                            // Could not connect/find/open database
                            Alerter.emit(Alert.AlertType.ERROR, Strings.ACTION_BACKUP, "Could not open database.");
                        } else {
                            // We don't exactly know what happened
                            Alerter.emit(Alert.AlertType.ERROR, Strings.ACTION_BACKUP, "Ooops, something happened.");
                        }
                        break;
                }
            }

            @Override
            protected void failed() {
                Alerter.emit(Alert.AlertType.ERROR, Strings.ACTION_BACKUP, "Ooops, something happened.");
            }

            @Override
            protected void cancelled() {
                Alerter.emit(Alert.AlertType.ERROR, Strings.ACTION_BACKUP, "Ooops, something happened.");
            }
        };

        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();

        gbakFlag = false;
        gzipFlag = false;
    }

    public void restoreNow() {

    }

    public void cloneNow() {

    }

    public void resetDefaults() {
        userField.setText(Strings.DEFAULT_USER);
        pwdField.setText(Strings.DEFAULT_PASSWORD);
    }

    public void showAbout() {
        String content =
                Strings.PROGRAM_FULL_VERSION + " " + Strings.RELEASE_DATE
                        + System.lineSeparator()
                        + System.lineSeparator()
                        + Strings.PROGRAM_RESPONSIBLE + " " + Strings.RESPONSIBLE_EMAIL;
        Alerter.emit(Alert.AlertType.INFORMATION, Strings.ACTION_BACKUP, content);
    }

    private void calculateTotalDuration() {
        taskDurationS = (endTime - startTime) / 1000;

        if (taskDurationS >= 60) { // If 1 minute or more.
            // Divide to find how many minutes
            taskDurationM = taskDurationS / 60;
            taskDurationS %= 60;

            if (taskDurationM >= 60) { // If 1 hour or more.
                taskDurationH = taskDurationM / 60;
                taskDurationM %= 60;
            }
        }

        totalDuration = Long.toString(taskDurationH) + "h " + Long.toString(taskDurationM) + "m " + Long.toString(taskDurationS) + "s";
        System.out.println("Duration: " + totalDuration);
    }
}

/* BACKUP

    gbak
        -backup
        -verify
        -user [username]
        -password [password]
        -garbage
        -service service_mgr
        -y [log file] (the log file MUST NOT exist, or the process will fail)
        [database]
        [destination]
 */

/* COMPRESSION
    gzip
        -1 (faster)
        -f (force overwrite)
        [file]
 */

/* Detect Firebird service:
        cmd /c tasklist /svc | findstr \"fbguard.exe\""
        cmd /c tasklist /svc | findstr \"fbserver.exe\""
 */