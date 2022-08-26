package de.goldendeveloper.screenclient;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Main {

    private static Config config;

    public static void main(String[] args) throws Exception {
        config = new Config();
        if (ConfigExists()) {
            new ScreenClient(null);
        } else {
            new ScreenClient(new Console().run());
        }
    }

    public static Config getConfig() {
        return config;
    }

    public static Boolean ConfigExists() throws Exception {
        ExportResource("ExampleConfig.xml");
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        Path dir = Paths.get("config/Config.xml");
        Path src = Path.of(classloader.getResource("config.xml").getPath());
        boolean exists = Files.exists(dir);

        if (!exists) {
            File file = new File("config/Config.xml");
            file.createNewFile();
            Files.copy(src, dir, StandardCopyOption.COPY_ATTRIBUTES);
        }
        return exists;
    }


    public static String ExportResource(String resourceName) throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        String jarFolder;
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            stream = classloader.getResourceAsStream(resourceName);
            if (stream == null) {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
//            jarFolder = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
            jarFolder = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
            System.out.println(jarFolder);
            resStreamOut = new FileOutputStream(jarFolder + "/Config.xml");
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            stream.close();
            resStreamOut.close();
        }

        return jarFolder + "/" + resourceName;
    }
}
