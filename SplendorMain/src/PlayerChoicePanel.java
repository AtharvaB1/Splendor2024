import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PlayerChoicePanel extends JPanel implements MouseListener{
    private BufferedImage background;
    
    public PlayerChoicePanel(){
        try{
            background = ImageIO.read(PlayerChoicePanel.class.getResource("/images/selectBackground.jpg"));
        } catch(IOException e){
            System.out.println("Image error in player choice panel");
        }
        addMouseListener(this);
    }
    
    public void paint(Graphics g){
        super.paint(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        if(e.getButton()==e.BUTTON1){
            if(e.getX()>=134)
            System.out.println("(" + e.getX() + ", " + e.getY() + ")");
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }
}
