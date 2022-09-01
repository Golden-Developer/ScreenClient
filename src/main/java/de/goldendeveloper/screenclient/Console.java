package de.goldendeveloper.screenclient;

import com.google.common.net.InetAddresses;

import javax.xml.stream.XMLStreamException;
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


    private void setLocalPort(Scanner scanner, File file) throws XMLStreamException, FileNotFoundException {
        System.out.println("Bitte einen Port für diese Anwendung eingeben:");
        String port = scanner.nextLine();
        if (!port.isBlank()) {
            if (isInteger(port)) {
                System.out.println("Der Anwendung's Port lautet: " + port);
                Config.write(file, "LocalPort", port);
            } else {
                System.out.println("Der Locale Port muss eine Zahl sein!");
                this.setLocalPort(scanner, file);
            }
        } else {
            System.out.println("Der Anwendung's Port lautet: " + Main.getConfig().getLocalPort());
            Config.write(file, "LocalPort", String.valueOf(Main.getConfig().getLocalPort()));
        }
    }

    private void setServerPort(Scanner scanner, File file) throws XMLStreamException, FileNotFoundException {
        System.out.println("Bitte einen Server Port für diese Anwendung eingeben:");
        String serverPort = scanner.nextLine();
        if (isInteger(serverPort)) {
            System.out.println("Der Anwendung's Server Port lautet: " + serverPort);
            Config.write(file, "ServerPort" ,serverPort);
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
            Config.write(file, "ServerHostname" ,serverHostname);
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
