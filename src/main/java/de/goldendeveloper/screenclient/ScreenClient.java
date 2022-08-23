package de.goldendeveloper.screenclient;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ScreenClient {

    public ScreenClient() throws IOException {
        Socket socket = new Socket(Main.getConfig().getHostname(), Main.getConfig().getPort());
        System.out.println("Connected to server.");

        JFrame jframe = new JFrame("Client");
        jframe.setSize(400, 400);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon imageIcon = new ImageIcon(Main.getConfig().getImageIcon());

        JLabel jLabel = new JLabel(imageIcon);
        JButton jButton = new JButton("Send Image to Server");

        jframe.add(jLabel);
        jframe.add(jButton);

        jframe.setVisible(true);

        jButton.addActionListener(e -> {
            try {
                OutputStream outputStream = socket.getOutputStream();
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

                Image image = imageIcon.getImage();

                BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);

                Graphics graphics = bufferedImage.createGraphics();
                graphics.drawImage(image, 0,0,null);
                graphics.dispose();

                ImageIO.write(bufferedImage, "jpg", bufferedOutputStream);

                bufferedOutputStream.close();
                socket.close();

            } catch (IOException ex){
                ex.printStackTrace();
            }
        });
    }
}
