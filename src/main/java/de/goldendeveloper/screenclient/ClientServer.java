package de.goldendeveloper.screenclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.imageio.ImageIO;
import javax.swing.*;
import org.codehaus.plexus.util.Base64;
import javax.xml.stream.XMLStreamException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class ClientServer {

    public ClientServer() throws Exception {
        System.out.println("[ClientServer] ClientServer starting...");
        ServerSocket serverSocket = new ServerSocket(Main.getConfig().getLocalPort());
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                BufferedReader incoming = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                Stream<String> ts = incoming.lines();
                for (Object st : ts.toArray()) {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode node = mapper.readTree(st.toString());
                    onDelete(node);
                    onUpdate(node);
                    onUpload(node);
                }
                incoming.close();
            } catch (IOException | XMLStreamException | URISyntaxException e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void onUpload(JsonNode node) throws IOException {
        if (node.has("upload")) {
            JsonNode upload = node.get("upload");
            if (upload.has("image")) {
                JsonNode image = upload.get("image");
                if (image.has("byteArray")) {
                    String img = image.get("byteArray").asText();
                    byte[] byteArray = img.getBytes();

                    byte[] bytesFromDecode = Base64.decodeBase64(byteArray);
                    BufferedImage newImg = toBufferedImage(bytesFromDecode);

                    JFrame frame = new JFrame();
                    frame.getContentPane().setLayout(new FlowLayout());
                    frame.getContentPane().add(new JLabel(new ImageIcon(newImg)));
                    frame.pack();
                    frame.setVisible(true);
                    //TODO: Save Image File
                }
            }

            if (upload.has("name")) {
                // TODO: Set Image name by uploaded Name
            }

            if (upload.has("duration")) {
                //TODO: Save Image display Duration
            }
        }
    }

    public void onUpdate(JsonNode node) throws XMLStreamException, IOException, URISyntaxException, InterruptedException {
        if (node.has("update")) {
            JsonNode update = node.get("update");
            String jarFolder = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
            File file = new File(jarFolder + "/Config.xml");
            if (update.has("clientport")) {
                Config.write(file, "LocalPort", update.get("clientport").asText());
            }

            if (update.has("serverport")) {
                Config.write(file, "ServerPort", update.get("serverport").asText());
            }

            if (update.has("serveripadresse")) {
                Config.write(file, "ServerHostname", update.get("serveripadresse").asText());
            }

            System.out.println("[ClientServer] New data received");
            System.out.println("[ClientServer] Restart Programm in 30 Seconds");
            TimeUnit.SECONDS.sleep(30);
            System.out.println("[ClientServer] Restart Programm now");
            restartApplication();
        }
    }

    public void onDelete(JsonNode node) {
        if (node.has("delete")) {
            JsonNode delete = node.get("delete");
            if (delete.has("name")) {
                //TODO: Find Image File by Name and delete
            }
        }
    }


    public void sendFirstConnection(Socket socket) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode object = mapper.createObjectNode();;
        object.put("Name", "");
        object.put("Port", Main.getConfig().getLocalPort());
        object.put("IPAdresse", socket.getLocalAddress().toString() );
        object.put("SSHPublic", "");
    }

    public static BufferedImage toBufferedImage(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        return ImageIO.read(is);
    }

    public void restartApplication() throws URISyntaxException, IOException {
        final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        final File currentJar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());

        /* is it a jar file? */
        if(!currentJar.getName().endsWith(".jar")) {
            return;
        }

        /* Build command: java -jar application.jar */
        final ArrayList<String> command = new ArrayList<>();
        command.add(javaBin);
        command.add("-jar");
        command.add(currentJar.getPath());

        final ProcessBuilder builder = new ProcessBuilder(command);
        builder.start();
        System.exit(0);
    }
}




/*{
  "upload": {
    "Image" :"",
    "Name":"",
    "Duration": ""
  },
  "update": {
    "ClientPort": "",
    "ServerPort" ."",
    "IPAdresse" :""
  },
  "delete" : {
    "Name": ""
  }
}*/