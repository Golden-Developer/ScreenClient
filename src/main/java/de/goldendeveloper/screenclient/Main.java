package de.goldendeveloper.screenclient;

import java.io.*;

public class Main {

    private static Config config;

    public static void main(String[] args) throws Exception {
//        new SSH();
        config = new Config();
        if (Config.Exists()) {
            new ScreenClient(null);
        } else {
            ExportResource("ExampleConfig.xml");
            new ScreenClient(new Console().run());
        }

        new ClientServer();
        // Run LocalServer

        //TODO: Generate and Safe ssh Key
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
}
