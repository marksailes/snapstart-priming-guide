package software.snapstart.priming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

public class ClassPreLoader {

    public static final String CLASSES_FILE = "classes.txt";

    public static void preloadClass(String classname, boolean initialize) {
        try {
            Class.forName(classname, initialize, ClassPreLoader.class.getClassLoader());
        } catch (Throwable ignored) {

        }
    }

    public static void preloadClasses(boolean initialize) {
        try {
            Enumeration<URL> files = ClassPreLoader.class.getClassLoader().getResources("classes.txt");
            while (files.hasMoreElements()) {
                URL url = files.nextElement();
                URLConnection conn = url.openConnection();
                conn.setUseCaches(false);
                InputStream is = conn.getInputStream();
                preloadClassesFromStream(is, initialize);
            }
        } catch (IOException ignored) {
        }
        InputStream is = ClassPreLoader.class
                .getResourceAsStream( CLASSES_FILE);
        if (is != null)
            preloadClassesFromStream(is, initialize);
    }

    public static void preloadClassesFromStream(InputStream is, boolean initialize) {
        try (is;
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader reader = new BufferedReader(isr)) {
            String line;
            while ((line = reader.readLine()) != null) {
                int idx = line.indexOf('#');
                if (idx != -1) {
                    line = line.substring(0, idx);
                }
                final String className = line.stripTrailing();
                if (!className.isBlank()) {
                    preloadClass(className, initialize);
                }
            }
        } catch (Exception ignored) {

        }
    }

    public void invokePreloadClasses(boolean initialize) {
        preloadClasses(initialize);
    }
}
