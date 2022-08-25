package de.goldendeveloper.screenclient;

import java.io.IOException;

public class Main {

    private static Config config;

    public static void main(String[] args) throws IOException {
        config = new Config();
        new Console();
        new ScreenClient(null);
    }

    public static Config getConfig() {
        return config;
    }
}
