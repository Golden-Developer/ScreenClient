package de.goldendeveloper.screenclient;

import com.google.common.net.InetAddresses;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Console {

    public String run() throws URISyntaxException, XMLStreamException, FileNotFoundException {
        String jarFolder = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
        File file = new File(jarFolder + "/Config.xml");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bitte einen Name für diese Anwendung eingeben:");
        String name = scanner.nextLine();
        System.out.println("Der Anwendung's Name lautet: " + name);

        setLocalPort(scanner, file);

        System.out.println("Bitte einen Public Key für diese Anwendung eingeben:");
        String publicKey = scanner.nextLine();
        System.out.println("Der Anwendung's PublicKey lautet: " + publicKey);

        setServerHostname(scanner, file);

        setServerPort(scanner, file);

        System.out.println("Alle Änderungen können im Dashboard angepasst werden!");
        return name;
    }


    private static void write(File out, String key, String value) throws XMLStreamException, FileNotFoundException {
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
        if (ServerHostname.equalsIgnoreCase(key)) {
            writer.writeCharacters(value);
        } else {
            writer.writeCharacters(Main.getConfig().getServerHostname());
        }
        writer.writeEndElement();


        String ServerPort = "ServerPort";
        writer.writeStartElement(ServerPort);
        if (ServerPort.equalsIgnoreCase(key)) {
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


    private void setLocalPort(Scanner scanner, File file) throws XMLStreamException, FileNotFoundException {
        System.out.println("Bitte einen Port für diese Anwendung eingeben:");
        String port = scanner.nextLine();
        if (isInteger(port)) {
            System.out.println("Der Anwendung's Port lautet: " + port);
            write(file, "LocalPort", port);
        } else {
            System.out.println("Der Locale Port muss eine Zahl sein!");
            this.setLocalPort(scanner, file);
        }
    }

    private void setServerPort(Scanner scanner, File file) throws XMLStreamException, FileNotFoundException {
        System.out.println("Bitte einen Server Port für diese Anwendung eingeben:");
        String serverPort = scanner.nextLine();
        if (isInteger(serverPort)) {
            System.out.println("Der Anwendung's Server Port lautet: " + serverPort);
            write(file, "ServerPort" ,serverPort);
        } else {
            System.out.println("Der Server Port muss eine Zahl sein!");
            this.setServerPort(scanner, file);
        }
    }

    private void setServerHostname(Scanner scanner, File file) throws XMLStreamException, FileNotFoundException {
        System.out.println("Bitte eine Server Ip-Adresse für diese Anwendung eingeben:");
        String serverHostname = scanner.nextLine();

        if (InetAddresses.isInetAddress(serverHostname)) {
            System.out.println("Die Anwendung's Server Ip-Adresse lautet: " + serverHostname);
            write(file, "ServerHostname" ,serverHostname);
        } else {
            System.out.println("Die IP Addresse " + serverHostname + " konnte nicht gefunden werden");
            this.setServerHostname(scanner, file);
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }
}
