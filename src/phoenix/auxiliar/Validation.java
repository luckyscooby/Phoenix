package phoenix.auxiliar;

import phoenix.resource.Strings;

import java.io.File;
import java.io.IOException;

/**
 * Project: Phoenix
 * Package: phoenix.auxiliar
 * Created by leoca_000 on 3/28/2016.
 */

public class Validation {
    public static boolean isValidField(String s) {
        return !s.equals("");
    }

    public static boolean isValidDB(String file) {
        // Now we check if DB file really exists
        File db = new File(file);
        try {
            if (db.exists()) {
                System.out.println("DB found.");
                return true;
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            Alerter.emitSecurityException();
        }

        return false;
    }

    public static boolean isValidService() {
        // Check Firebird service
        try {
            Process svcA = Runtime.getRuntime().exec(Strings.SVC_DEFAULT_COMMAND);

            while (svcA.isAlive()) {
            }

            if (svcA.exitValue() != 0) {
                return false;
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            Alerter.emitSecurityException();
        } catch (IOException e) {
            e.printStackTrace();
            Alerter.emitException();
        }
        return true;
    }

    private static void toGbak() {
        // TODO: [New] Database location converted to Gbak syntax.
        // TODO: [Fix-Critical] Phoenix should be able to handle databases through IP paths.
        // TODO: [Fix-Critical] A parser for addresses like "\\127.0.0.1\databases\MEDISYSTEM.FDB", converting to Firebird paths like "127.0.0.1:c:databases\MEDISYSTEM.FDB".

    }
}
