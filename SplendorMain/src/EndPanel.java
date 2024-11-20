import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class EndPanel extends JPanel implements MouseListener{
    private int startX;//= getWidth()/2;
    private int startY;// = getHeight()/5;
    private TreeSet<Player> scores;
    private BufferedImage bkg;
    //constructor
    public EndPanel(TreeSet<Player> s){
        scores = s;
        startX = 750;
        startY = 475;
        addMouseListener(this);
        try {
            bkg = ImageIO.read(EndPanel.class.getResource("/images/endBackground.jpg"));
        } 
        catch (IOException ex) {
            System.out.println("backround is OVER");
        }
    }

    //main paint method, need to add scores to be dysplayed aswell but it is mostly done
    public void paint(Graphics g){
        super.paint(g);
        g.drawImage(bkg, 0, 0, getWidth(), getHeight(), null); //background

        Iterator<Player> iter = scores.iterator();
        Player winner = iter.next();
        g.setFont(new Font("Times New Roman", Font.PLAIN, 50));
        g.drawString(winner.getName() + "! (score, " + winner.getTotalVP() + ")", startX, startY);

        g.setFont(new Font("Times New Roman", Font.PLAIN, 50));
        for(int i = 0; i < scores.size()-1; i++){
            Player p = iter.next();
            g.drawString(p.getName() + "(score, " + p.getTotalVP() + ")", (startX-700) + i*750 , (startY +250 ));
        }
        //repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'mouseClicked'");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'mousePressed'");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'mouseReleased'");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'mouseEntered'");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'mouseExited'");
    }
    
}//end of class
