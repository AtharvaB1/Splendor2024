import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MenuPanel extends JPanel implements MouseListener{
    private BufferedImage background;

    public MenuPanel(){
        try{
            background = ImageIO.read(MenuPanel.class.getResource("/images/menuBackground.jpg"));
//            background = ImageIO.read(new File("menuBackground.jpg"));
        } catch(IOException e){
            System.out.println("Image error in menu panel");
        }
        addMouseListener(this);
    }

    public void paint(Graphics g){
        super.paint(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), null);        
    }

    public void mouseClicked(MouseEvent e){ // need to fix thiis so that button works for all screen sizes
        if(e.getButton()==MouseEvent.BUTTON1){
            System.out.println(getWidth() + " " + getHeight());
            if(e.getX()>=MainFrame.getScreenWidth()*(96/259) && e.getX()<=MainFrame.getScreenWidth()*(162/259) && e.getY()>=MainFrame.getScreenHeight()*(64/139) && e.getY()<=MainFrame.getScreenHeight()*(96/139)){
                System.out.println("Button area clicked");
                //MainFrame.selectPlayers();
            }
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
