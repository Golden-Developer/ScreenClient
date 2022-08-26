package de.goldendeveloper.screenclient;

import java.util.Scanner;

public class Console {

    public String run() {
        System.out.println("Bitte einen Name für diese Anwendung eingeben:");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        System.out.println("Der Anwendung's Name lautet: " + name);

        System.out.println("Bitte einen Port für diese Anwendung eingeben:");
        String port = scanner.nextLine();
        System.out.println("Der Anwendung's Port lautet: " + port);

        System.out.println("Bitte einen Public Key für diese Anwendung eingeben:");
        String publicKey = scanner.nextLine();
        System.out.println("Der Anwendung's PublicKey lautet: " + publicKey);

        System.out.println("Alle Änderungen können im Dashboard angepasst werden!");
        return name;
    }
}
