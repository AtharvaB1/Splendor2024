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
        } catch(IOException e){
            System.out.println("Image error in menu panel");
        }
        addMouseListener(this);
    }

    public void paint(Graphics g){
        super.paint(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), null);   // background     
    }

    public void mouseClicked(MouseEvent e){ 
        int width = getWidth();
        int height = getHeight();
        System.out.println("(" + width +", "+height+")");
        double[] x = {width*.375, width*.612}; // button's x and y coords
        double[] y = {height*.46, height*.69};
        
        if(e.getButton()==e.BUTTON1){
            if(e.getX()>=x[0] && e.getY()<=x[1] && 
                e.getY()>=y[0] && e.getY()<=y[1]){
                    MainFrame.selectPlayers();
                }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
