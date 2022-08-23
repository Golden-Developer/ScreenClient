package de.goldendeveloper.screenclient;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Main {

    private static Config config;

    public static void main(String[] args) throws IOException {
        config = new Config();
        new ScreenClient();
    }

    public static Config getConfig() {
        return config;
    }
}
