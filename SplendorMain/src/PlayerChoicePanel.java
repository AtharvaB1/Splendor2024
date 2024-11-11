import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.io.IOException;
//import java.io.File;
//import java.awt.event.*;
import java.awt.Graphics;

public class PlayerChoicePanel extends JPanel{
    private BufferedImage background;
    
    public PlayerChoicePanel(){
        try{
            background = ImageIO.read(PlayerChoicePanel.class.getResource("/images/selectBackground.jpg"));
        } catch(IOException e){
            System.out.println("Image error in player choice panel");
        }
    }
    
    public void paint(Graphics g){
        super.paint(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
    }
}
