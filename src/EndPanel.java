import java.awt.Color;
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
    public int width;
    public int height;
    private int startX;//= getWidth()/2;
    private int startY;// = getHeight()/5;
    private TreeSet<Player> scores;
    private BufferedImage bkg;
    //constructor
    public EndPanel(TreeSet<Player> s){
        scores = s;
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
        width = getWidth();
        height = getHeight();
        startX = width / 3 + width / 19;
        startY = height / 3 + height / 8;
        g.drawImage(bkg, 0, 0, getWidth(), getHeight(), null); //background

        Iterator<Player> iter = scores.iterator();
        Player winner = iter.next();
        g.setFont(new Font("Times New Roman", Font.PLAIN, width / 60 + height / 54));
        g.setColor(new Color(0,0,0));
        g.drawString(winner.getName() + "! (score, " + winner.getTotalVP() + ")", startX, startY);

        g.setFont(new Font("Times New Roman", Font.PLAIN,  width / 54 + height / 54));
        startX = width / 25;
        startY = height - height / 15;
        for(int i = 0; i < scores.size()-1; i++){
            Player p = iter.next();
            g.drawString(p.getName() + "(score, " + p.getTotalVP() + ")", startX, startY);
            startX += width / 3 + width / 35;
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
