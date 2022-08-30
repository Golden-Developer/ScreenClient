package de.goldendeveloper.screenclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public class ClientServer {

    public ClientServer() throws IOException {
        SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket serverSocket = (SSLServerSocket) factory.createServerSocket(Main.getConfig().getLocalPort());
        while (true) {
            SSLSocket socket = null;
            try {
                socket = (SSLSocket) serverSocket.accept();
//                BufferedReader incoming = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
//                PrintStream outgoing = new PrintStream(socket.getOutputStream());

                DataOutputStream os=new DataOutputStream(socket.getOutputStream());
                DataInputStream is=new DataInputStream(socket.getInputStream());

                //Gui du lieu len server
                String str="helloworld";
                os.writeBytes(str);
                //Nhan du lieu da qua xu li tu server ve
                String responseStr;
                if((responseStr=is.readUTF())!=null)
                {
                    System.out.println(responseStr);
                }

                os.close();
                is.close();
//                sslsocket.close();
     /*
                Stream<String> ts = incoming.lines();
                for (Object st : ts.toArray()) {
                    ObjectMapper mapper = new ObjectMapper();
                    ObjectNode object = mapper.createObjectNode();;
                    String name = object.get("name").asText();
                    String action = object.get("action").asText();
                    String server = object.get("server").asText();

                    System.out.println("Receiving Data Name: " + name + " Action: " + action + " Server: " + server);
                    System.out.println("Get Data: " + name + action + server);

                    //RUN CODE
                }
                outgoing.close();*/
            } catch (IOException e) {
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


    public void sendFirstConnection(Socket socket) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode object = mapper.createObjectNode();;
        object.put("Name", "");
        object.put("Port", Main.getConfig().getLocalPort());
        object.put("IPAdresse", socket.getLocalAddress().toString() );
        object.put("SSHPublic", "");
    }
}
