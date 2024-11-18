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
        startX = 800;
        startY = 100;
        addMouseListener(this);
    }

    //main paint method, need to add scores to be dysplayed aswell but it is mostly done
    public void paint(Graphics g){
        super.paint(g);
        Iterator<Player> iter = scores.iterator();
        Player winner = iter.next();
        g.setFont(new Font("Times New Roman", Font.PLAIN, 44));
        g.drawString(winner.getName() + " has won!", startX, startY);

        g.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        for(int i = 0; i < scores.size()-1; i++)
        {
            Player p = iter.next();
            g.drawString(p.getName() + " has lost :(", (startX-500) + i*500 , (startY +200 ));
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
