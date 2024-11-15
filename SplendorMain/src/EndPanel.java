import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.JPanel;

public class EndPanel extends JPanel implements MouseListener{
    private int startX;//= getWidth()/2;
    private int startY;// = getHeight()/5;
    private TreeSet<Player> scores;

    //constructor
    public EndPanel(TreeSet<Player> s){
        scores = s;
        startX = getWidth()/2;
        startY = getHeight()/5;
        addMouseListener(this);
    }

    //main paint method, need to add scores to be dysplayed aswell but it is mostly done
    public void paint(Graphics g){
        Iterator<Player> iter = scores.iterator();
        Player winner = iter.next();
        g.setFont(new Font("Times New Roman", Font.PLAIN, 44));
        g.drawString("player " + winner.getName() + " has one!", startX, startY);
        g.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        for(int i = 1; i < scores.size(); i++)
        {
            Player p = iter.next();
            g.drawString("player " + p.getName() + " has lost :(", startX + i*50, startY);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseClicked'");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mousePressed'");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseReleased'");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseEntered'");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseExited'");
    }
    
}//end of class
