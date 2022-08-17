import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 1234);
        System.out.println("Connected to server.");

        JFrame jframe = new JFrame("Client");
        jframe.setSize(400, 400);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon imageIcon = new ImageIcon("/Users/sebastianhobst/Deichlust.png");

        JLabel jLabel = new JLabel(imageIcon);
        JButton jButton = new JButton("Send Image to Server");

        jframe.add(jLabel);
        jframe.add(jButton);

        jframe.setVisible(true);

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    OutputStream outputStream = socket.getOutputStream();
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

                    Image image = imageIcon.getImage();

                    BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);

                    Graphics graphics = bufferedImage.createGraphics();
                    graphics.drawImage(image, 0,0,null);
                    graphics.dispose();

                    ImageIO.write(bufferedImage, "png", bufferedOutputStream);

                    bufferedOutputStream.close();
                    socket.close();

                } catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        });





    }
}
