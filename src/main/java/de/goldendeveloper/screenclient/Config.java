package de.goldendeveloper.screenclient;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {

    private String ImageIcon;
    private String ServerHostname;
    private int ServerPort;
    private int LocalPort;

    public Config() {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream local = classloader.getResourceAsStream("config.xml");

            String jarFolder = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
            File file = new File(jarFolder + "/Config.xml");

            Path path = Files.createTempFile("Config", ".xml");

            if (file.exists()) {
                InputStream targetStream = new FileInputStream(file);
                if (targetStream != null && targetStream.available() >= 0) {
                    readXML(targetStream);
                }
            } else {
                if (local != null && Files.exists(path)) {
                    readXML(local);
                } else {
                    local = classloader.getResourceAsStream("Config.xml");
                    readXML(local);
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    public static Boolean Exists() throws Exception {
        String jarFolder = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
        return Files.exists(Paths.get(jarFolder + "/Config.xml"));
    }

    public static void write(File out, String key, String value) throws XMLStreamException, FileNotFoundException {
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = output.createXMLStreamWriter(new FileOutputStream(out));

        writer.writeStartElement("login");

        String ImageIcon = "ImageIcon";
        writer.writeStartElement("ImageIcon");
        if (ImageIcon.equalsIgnoreCase(key)) {
            writer.writeCharacters(value);
        } else {
            writer.writeCharacters(Main.getConfig().getImageIcon());
        }
        writer.writeEndElement();

        String ServerHostname = "ServerHostname";
        writer.writeStartElement(ServerHostname);
        if (key.equalsIgnoreCase(ServerHostname)) {
            writer.writeCharacters(value);
        } else {
            writer.writeCharacters(Main.getConfig().getServerHostname());
        }
        writer.writeEndElement();


        String ServerPort = "ServerPort";
        writer.writeStartElement(ServerPort);
        if (key.equalsIgnoreCase(ServerPort)) {
            writer.writeCharacters(value);
        } else {
            writer.writeCharacters(String.valueOf(Main.getConfig().getServerPort()));
        }
        writer.writeEndElement();


        String LocalPort = "LocalPort";
        writer.writeStartElement(LocalPort);
        if (LocalPort.equalsIgnoreCase(key)) {
            writer.writeCharacters(value);
        } else {
            writer.writeCharacters(String.valueOf(Main.getConfig().getLocalPort()));
        }
        writer.writeEndElement();

        writer.writeEndElement();

        writer.flush();
        writer.close();

        Main.setConfig(new Config());
    }

    private void readXML(InputStream inputStream) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inputStream);
            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("login");
            for (int i = 0; i < list.getLength(); i++) {
                if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) list.item(i);
                    String ImageIcon = element.getElementsByTagName("ImageIcon").item(0).getTextContent();
                    String ServerHostname = element.getElementsByTagName("ServerHostname").item(0).getTextContent();
                    String ServerPort = element.getElementsByTagName("ServerPort").item(0).getTextContent();
                    String LocalPort = element.getElementsByTagName("LocalPort").item(0).getTextContent();

                    if (!ImageIcon.isEmpty() || !ImageIcon.isBlank()) {
                        this.ImageIcon = ImageIcon;
                    }
                    if (!ServerHostname.isEmpty() || !ServerHostname.isBlank()) {
                        this.ServerHostname = ServerHostname;
                    }
                    if (!ServerPort.isEmpty() || !ServerPort.isBlank()) {
                        this.ServerPort = Integer.parseInt(ServerPort);
                    }

                    if (!LocalPort.isEmpty() || !LocalPort.isBlank()) {
                        this.LocalPort = Integer.parseInt(LocalPort);
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public String getServerHostname() {
        return ServerHostname;
    }

    public String getImageIcon() {
        return ImageIcon;
    }

    public Integer getServerPort() {
        return ServerPort;
    }

    public int getLocalPort() {
        return LocalPort;
    }

    public static void Export(String resourceName) throws Exception {
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

