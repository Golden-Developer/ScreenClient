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
        System.out.println("[ScreenClient] Connect to server...");
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
            json.put("IPAdresse", socket.getLocalAddress().toString().replace("/", ""));
            json.put("SSHPublic", "SSHPublicKey");

            System.out.println("[ScreenClient] Sending data...");
            osw.write(json.toString());
            osw.flush();
            osw.close();
            System.out.println("[ScreenClient] Sending finished");
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
                    System.out.println("[ScreenClient] Socket geschlossen...");
                } catch (IOException e) {
                    System.out.println("[ScreenClient] Socket konnte nicht geschlossen werden...");
                    e.printStackTrace();
                }
            }
        }
    }
}
