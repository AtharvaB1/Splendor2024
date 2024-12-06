import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PlayerChoicePanel extends JPanel implements MouseListener{
    private BufferedImage background;
    
    //constructor
    public PlayerChoicePanel(){
        try{
            background = ImageIO.read(PlayerChoicePanel.class.getResource("/images/selectBackground.png"));
        } catch(IOException e){
            System.out.println("Image error in player choice panel");
        }
        System.out.println("player choice panel opened");
        addMouseListener(this);
    }
    
    //main paint method
    public void paint(Graphics g){
        super.paint(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
    }

    @Override
    //main click detection, checks to see if a click was inside a button range and then starts up the game with the selected buttons amount of players
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        System.out.println(getWidth() +" " + getHeight());
        System.out.println("(" + e.getX() + ", " + e.getY() + ")");
        int width = getWidth(); // 1281
        int height = getHeight(); // 658

        // positions of each button's x and y coords
        double[][] p2s = {{width*.104, width*.294}, // x = 133, 377   y = 350, 430 
                            {height*.532, height*.653}};
        double[][] p3s = {{width*.411, width*.604}, // x = 527, 774   y = 350, 430
                            {height*.532, height*.653}};
        double[][] p4s = {{width*.719, width*.915}, // x = 921, 1172   y = 350, 430
                            {height*.532, height*.653}};

        if(e.getButton()==1){
            if(e.getX()>=p2s[0][0] && e.getX()<=p2s[0][1] &&
                e.getY()>=p2s[1][0] && e.getY()<=p2s[1][1]){
                    MainFrame.startGame(2);
            }
            if(e.getX()>=p3s[0][0] && e.getX()<=p3s[0][1] &&
                e.getY()>=p3s[1][0] && e.getY()<=p3s[1][1]){
                    MainFrame.startGame(3);
            }
            if(e.getX()>=p4s[0][0] && e.getX()<=p4s[0][1] &&
                e.getY()>=p4s[1][0] && e.getY()<=p4s[1][1]){
                    MainFrame.startGame(4);
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

}//end of class
