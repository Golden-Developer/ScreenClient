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
import java.io.IOException;
import java.io.InputStream;

public class Config {

    private String ImageIcon;
    private String Hostname;
    private int Port;

    public Config() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream local = classloader.getResourceAsStream("config.xml");
        if (local == null) {
            local = classloader.getResourceAsStream("Config.xml");
        }
        readXML(local);
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
                    String Hostname = element.getElementsByTagName("Hostname").item(0).getTextContent();
                    String Port = element.getElementsByTagName("Port").item(0).getTextContent();

                    if (!ImageIcon.isEmpty() || !ImageIcon.isBlank()) {
                        this.ImageIcon = ImageIcon;
                    }
                    if (!Hostname.isEmpty() || !Hostname.isBlank()) {
                        this.Hostname = Hostname;
                    }
                    if (!Port.isEmpty() || !Port.isBlank()) {
                        this.Port = Integer.parseInt(Port);
                    }

                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public String getHostname() {
        return Hostname;
    }

    public String getImageIcon() {
        return ImageIcon;
    }

    public Integer getPort() {
        return Port;
    }
}

