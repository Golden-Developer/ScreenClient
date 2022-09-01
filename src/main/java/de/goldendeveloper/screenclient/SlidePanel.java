package de.goldendeveloper.screenclient;

import com.google.common.io.Files;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class SlidePanel extends JFrame implements ActionListener {

    ImageIcon[] s;
    JLabel l;
    JButton b1, b2, b3;
    int i;

    public SlidePanel() throws URISyntaxException {
        String jarFolder = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
        String path = jarFolder + "/images/";

        File f = new File(path);
        if (f.list().length > 0) {

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel p = new JPanel(new FlowLayout());
            b1 = new JButton("<<");
            b3 = new JButton("Beenden");
            b2 = new JButton(">>");
            p.add(b1);
            p.add(b3);
            p.add(b2);
            add(p, BorderLayout.SOUTH);
            b1.addActionListener(this);
            b3.addActionListener(this);
            b2.addActionListener(this);
            setVisible(false);


            String[] ls = getImages(f.list(), path);
            s = new ImageIcon[ls.length];

            for (int k = 0; k < ls.length; k++) {
                s[k] = new ImageIcon(ls[k]);
            }

            l = new JLabel("", JLabel.CENTER);
            add(l, BorderLayout.CENTER);
            l.setIcon(s[0]);


            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setUndecorated(true);
            setVisible(true);

            setLayout(new BorderLayout());


            setVisible(true);
        } else {
            System.exit(0);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == b1) {
            if (i == 0) {
                JOptionPane.showMessageDialog(null, "This is the first Image");
            } else {
                i = i - 1;
                l.setIcon(s[i]);
            }
        }
        if (e.getSource() == b2) {
            if (i == s.length - 1) {
                JOptionPane.showMessageDialog(null, "This is the last Image");
            } else {
                i = i + 1;
                l.setIcon(s[i]);
            }
        }
        if (e.getSource() == b3) {
            System.exit(1);
        }
    }

    public String[] getImages(String[] files, String path) {
        List<String> d = new ArrayList<>();
        for (String value : files) {
            File file = new File(path + "\\" + value);
            if (!file.isDirectory()) {
                String type = Files.getFileExtension(file.getPath());
                if (type.equalsIgnoreCase("png") || type.equalsIgnoreCase("jpg")) {
                    d.add(file.getAbsolutePath());
                }
            }
        }
        return d.toArray(String[]::new);
    }
}