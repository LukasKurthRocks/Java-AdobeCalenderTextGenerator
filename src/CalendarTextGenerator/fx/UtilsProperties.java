package CalendarTextGenerator.fx;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * http://codezentrale.bplaced.net/dcz/?p=3712
 * @author Lukas 22.11.2017
 * NOT: NOT USED!!
 * */
class UtilsProperties {
    /**
     * Speichert Konfiguration
     *
     * @param filename Dateiname der Konfigurationsdatei
     */
    static void SaveConfig(String filename) throws IOException {
        Properties prop = new Properties();
        FileOutputStream fos = null;

        try {
            // Properties setzen
            prop.setProperty("database", "localhost");
            prop.setProperty("user", "Horst");
            prop.setProperty("password", "geheim");

            // 1. Einstellungen speichern als einfache Datei
            fos = new FileOutputStream(filename);
            prop.store(fos, "Kommentarzeile");

            // 2. Einstellungen speichern als XML-Datei im Format UTF-8
            prop.storeToXML(new FileOutputStream(filename), "Kommentarzeile", "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (fos != null) fos.close();
        }
    }

    static void saveConfigAsText(String filename, String comment, Properties props) throws IOException {
        props.store(new FileOutputStream(filename), comment);
    }

    static void saveConfigAsXML(String filename, String comment, Properties props) throws IOException {
        props.storeToXML(new FileOutputStream(filename), comment, "UTF-8");
    }

    /**
     * Läd Konfiguration
     *
     * @param filename Dateiname der Konfigurationsdatei
     */
    static void LoadConfig(String filename) throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = null;

        try {
            // 1. Einstellungen aus einfacher Datei laden
            fis = new FileInputStream(filename);
            prop.load(fis);

            // 2. oder Einstellungen aus XML-Datei laden
            //prop.loadFromXML(fis);

            System.out.println(prop.getProperty("database"));
            System.out.println(prop.getProperty("user"));
            System.out.println(prop.getProperty("password"));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (fis != null) fis.close();
        }
    }
}
