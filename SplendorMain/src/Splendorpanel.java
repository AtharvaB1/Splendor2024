import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.*;
//import java.util.Set;

public class Splendorpanel extends JPanel implements MouseListener{
    private int pCount; //num of players
   // private String tok1, tok2, tok3; //tokens players click on?
    private HashMap<String,Integer> heldTokens; //three tokens held by current player when interacted with
    private BufferedImage bkg; //, rule1, rule2, rule3, back1, back2, back3; // background and rules, backs of three diff card types
    Logic logic; //logic class
    JButton endTurn, rPrev, rNext, rClose; //might not be using anymore
    private Card[][] mat;
    private Patron [] pat;

    //constructor
    public Splendorpanel(int p){
        heldTokens = new HashMap<String,Integer>();
        mat = new Card[3][4];
        pCount = p;
        Logic logic = new Logic(pCount);
        pat = new Patron[pCount+1]; //one more patron than players
        try {
            //bkg = ImageIO.read(new File("bkg.png"));
            bkg = ImageIO.read(Splendorpanel.class.getResource("/images/gamebkg.jpg"));
            //rule1 =
            //rule2=
            //rule3=
            //back1 = (last row of cards)
            //back2=
            //back3= (top row of cards)
        } catch (Exception e) {
            System.out.println(e+"image issue splendor panel");
            return;
        }
        addMouseListener(this);

        for(int i = 0; i<3; i++){ //filling 2D matrix of cards on table
            for(int j = 0; j<4; j++){
                mat[i][j] = logic.getDecks().get(i).drawCard();
            }
        }

        for(int i = 0; i<pat.length; i++){
            pat[i] = logic.getPatrons().getLast();
        }
        
    }
    
    //main paint method for the entire splendorPanel
    public void paint(Graphics g){
        super.paint(g);
        //need to set actual location
        //drawCards(g); 
        //drawPatron(g);  
        //drawDeck(g);   
        //drawTokens(g);
    }

    //draws the player info, cand the current player on screen
    public void drawSetUp(Graphics g){
        g.drawImage(bkg, 0, 0, getWidth(), getHeight(), null); //background

        g.setFont(new Font("Times New Roman", Font.BOLD, 44));
        g.drawString(logic.getPlayer().getName(), getWidth()/2, getHeight()/6); //Which player's turn

        g.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        g.drawString("Player 1 - " + logic.getAllPlayers().get(0).getTotalVP() + " points", 100, 100);
        g.drawString("Player 2" + logic.getAllPlayers().get(1).getTotalVP() + " points", 100, 1000);//set actual loc later
        g.drawString("Player 3" + logic.getAllPlayers().get(2).getTotalVP() + " points", 1000, 100);
        g.drawString("Player 4" + logic.getAllPlayers().get(3).getTotalVP() + " points", 1000, 1000);


    }
    
    //draws the current cards that can be selected
    public void drawCards(Graphics g){
        int cardX = 0; //set start position of cards, increment x and y by __ for the cards after in the loop
        int cardY = 0;

        for(int i = 0; i<3; i++){ //printing cards matrix from bottom row to top
            //reset x for next row
            // y-= _; 
            for(int j = 0; j<4; j++){
                //g.drawImage(mat[i][j].getCardFront(), 0, 0, w, h, null); // need to fix correct coordinates and height and width
                // x+= _;
            }
        }

    }
    
    //draws the current patreons that can be selected
    public void drawPatron(Graphics g){
        // int patX = __; set patron position and increment x in loop

        for(int i = 0; i<pat.length; i++){
            // g.drawImage(pat[i].getPatFace(), __, __, w, h, null);
        }
    }

    //draws the backs of decks 1, 2, and 3
    public void drawDeck(Graphics g){
        for(int i = 0; i<3; i++){
            if(!logic.getDecks().get(i).deckEmpty()){
                if(i == 0){
                    //g.drawImage(back1, )
                } else if (i == 1){
                    //g.drawImage(back2)
                } else {
                    //g.drawImage(back3)
                }
            }
        }
    }

    //draws the current tokens that can be taken
    public void drawTokens(Graphics g){
        Set<String> keys = logic.getTokens().keySet();
        String[] colors = {"White", "Red", "Green", "Blue", "Brown", "Wild"};
        //int x =  starting position for top token, incremented in loop
        //int y=

        for(int i = 0; i<colors.length; i++){
            String name = colors[i];
            if(logic.getTokens().get(colors[i])>0){
                //g.drawImage(name + ".jpg", x, y, w, h, null);

            }
            //x++ _;
            //y++ _;
        }
    }

    //public boolean[] checkDeck(){

    //}

    public void getToken(String x){

    }

    public void mousePressed(MouseEvent e){
        int mouseX = e.getX();
        int mouseY = e.getY();
       /* if(x>= _ && x<= _ && y>= __ && y<= __){ area of each token, ex for white, may do loop
           if(heldTokens.contains("White")){
              heldTokens.replace("White", heldTokens.get("White"), heldTokens.get("White") +1 );
           } else {
               heldTokens.put("White", 1);
           }
          }


       */

       // }
        // TODO Auto-generated method stub
    }

    public void mouseEntered(MouseEvent e){
        // TODO Auto-generated method stub
    }

    public void mouseClicked(MouseEvent e){
        // TODO Auto-generated method stub
    }

    public void mouseExited(MouseEvent e){
        // TODO Auto-generated method stub
    }

    public void mouseReleased(MouseEvent e){
        // TODO Auto-generated method stub
    }

}//end of class

