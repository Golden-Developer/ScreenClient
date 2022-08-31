package de.goldendeveloper.screenclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class ScreenClient {

    public ScreenClient(String name) {
        System.out.println("Connected to server.");
        Socket socket = null;
        try {
            socket = new Socket(Main.getConfig().getServerHostname(), Main.getConfig().getServerPort());
            OutputStream raus = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(raus, StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode json = mapper.createObjectNode();
            if (name != null && !name.isBlank()) {
                json.put("name", name);
            }
            json.put("Port", Main.getConfig().getLocalPort());
            json.put("IPAdresse", socket.getLocalAddress().toString());
        //    msg.put("SSHPublic", Main.getConfig());

            osw.write(json.toString());
            osw.flush();
            osw.close();
        } catch (UnknownHostException e) {
            System.out.println("Unknown Host...");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOProbleme...");
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                    System.out.println("Socket geschlossen...");
                } catch (IOException e) {
                    System.out.println("Socket konnte nicht geschlossen werden...");
                    e.printStackTrace();
                }
            }
        }
/*        ImageIcon imageIcon = new ImageIcon(Main.getConfig().getImageIcon());

        try {
            OutputStream outputStream = socket.getOutputStream();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            Image image = imageIcon.getImage();
            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics graphics = bufferedImage.createGraphics();
            graphics.drawImage(image, 0, 0, null);
            graphics.dispose();
            ImageIO.write(bufferedImage, "jpg", bufferedOutputStream);
            bufferedOutputStream.close();
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }*/
    }
}
