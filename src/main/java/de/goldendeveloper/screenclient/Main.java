package de.goldendeveloper.screenclient;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static Config config;

    public static void main(String[] args) throws Exception {
        config = new Config();
        startClient();

        Thread slider = new Thread(() -> {
            try {
                new SlidePanel();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });


        Thread thread = new Thread(() -> {
            try {
                if (Config.Exists()) {
                    new ScreenClient(null);
                    slider.start();
                } else {
                    ExportResource("ExampleConfig.xml");
                    new ScreenClient(new Console().run());
                    slider.start();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();
        if (thread.isAlive()) {
            new Thread(() -> {
                try {
                    new ClientServer();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }

        //TODO: Generate and Safe ssh Key
        //        new SSH();
    }

    public static Config getConfig() {
        return config;
    }

    public static void setConfig(Config config) {
        Main.config = config;
    }

    public static void ExportResource(String resourceName) throws Exception {
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
            jarFolder = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
            resStreamOut = new FileOutputStream(jarFolder + "/Config.xml");
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } finally {
            assert stream != null;
            stream.close();
            assert resStreamOut != null;
            resStreamOut.close();
        }
    }

    public static void startClient() throws URISyntaxException, IOException {
        String jarFolder = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
        Path dir = Paths.get(jarFolder + "/images/");
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
    }
}
