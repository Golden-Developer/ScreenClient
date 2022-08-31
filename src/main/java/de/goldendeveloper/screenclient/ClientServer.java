package de.goldendeveloper.screenclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.imageio.ImageIO;
import javax.xml.stream.XMLStreamException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public class ClientServer {

    public ClientServer() throws IOException {
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
          /*      InputStream inputStream = socket.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);
                File outputfile = new File(Main.getConfig().getImageOutputPath() + generatedString + ".jpg");
                ImageIO.write(bufferedImage, "jpg", outputfile);
                System.out.println(socket.getPort());*/

                if (image.has("byteArray") && image.has("size")) {

                    byte[] img = upload.get("size").binaryValue();
                    int size = ByteBuffer.wrap(img).asIntBuffer().get();

                    byte[] imageAr = upload.get("byteArray").binaryValue();

                    BufferedImage imageBuff = ImageIO.read(new ByteArrayInputStream(imageAr));

                    System.out.println("Received " + imageBuff.getHeight() + "x" + imageBuff.getWidth() + ": " + System.currentTimeMillis());
//                    ImageIO.write(imageBuff, "jpg", new File("C:\\Users\\Jakub\\Pictures\\test2.jpg"));
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

    public void onUpdate(JsonNode node) throws XMLStreamException, FileNotFoundException, URISyntaxException {
        if (node.has("update")) {
            JsonNode update = node.get("update");
            String jarFolder = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
            File file = new File(jarFolder + "/Config.xml");
            if (update.has("clientPort")) {
                Config.write(file, "LocalPort", update.get("clientPort").asText());
            }

            if (update.has("serverPort")) {
                Config.write(file, "ServerPort", update.get("serverPort").asText());
            }

            if (update.has("ipAdresse")) {
                Config.write(file, "ServerHostname", update.get("ipAdresse").asText());
            }
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