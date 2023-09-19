package phoenix.resource;

public class Strings {
    public static final String LOG_FILE = "stdout";
    // When we use "-service service_mgr, gbak won't output its log file.
    public static final String GBAK_DEFAULT_COMMAND = "C:\\gbak.exe -backup -verify -garbage -y " + LOG_FILE;
    public static final String GZIP_DEFAULT_COMMAND = "C:\\gzip.exe -1 -f";
    public static final String SVC_DEFAULT_COMMAND = "cmd /c tasklist /svc | findstr \"firebird.exe\"";
    public static final String INITIAL_DIRECTORY = "user.home";
    public static final String OUTPUT_BACKUP_FILE = "ASHES.FBK";
    public static final String DEFAULT_USER = "SYSDBA";
    public static final String DEFAULT_PASSWORD = "masterkey";
    public static final String PROGRAM_FULL_TITLE = "Phoenix       Database Backup On The Fly";
    public static final String PROGRAM_SHORT_TITLE = "Phoenix";
    public static final String PROGRAM_RAW_VERSION = "a0.2";
    public static final String PROGRAM_FULL_VERSION = "Version 0 Alpha 2";
    public static final String RELEASE_DATE = "[03/31/2016]";
    public static final String PROGRAM_RESPONSIBLE = "Michael Leocádio";
    public static final String RESPONSIBLE_EMAIL = "[leocadiomichael@gmail.com]";

    public static final String ACTION_BACKUP = "Backup";
    public static final String ACTION_RESTORE = "Restore";
    public static final String ACTION_CLONE = "Clone";

    public static final String FORM_FILE = "main.fxml";
}
