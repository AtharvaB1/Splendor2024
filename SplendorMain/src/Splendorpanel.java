import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Splendorpanel extends JPanel implements MouseListener{
    private int pCount; //num of players
   // private String tok1, tok2, tok3; //tokens players click on?
    private HashMap<String,Integer> heldTokens; //three tokens held by current player when interacted with
    private BufferedImage bkg, rule1, rule2, rule3, back1, back2, back3;// background and rules, backs of three diff card types
    Logic logic; //logic class
    JButton endTurn, rPrev, rNext, rClose; //might not be using anymore
    private Card[][] mat;
    private Patron [] pat;
    private String[] colors;
    private ArrayList <BufferedImage> tokenImgs;

    //constructor
    public Splendorpanel(int p){
        heldTokens = new HashMap<String,Integer>();
        tokenImgs = new ArrayList <BufferedImage> ();
        colors = new String[]{"White", "Blue", "Green", "Red", "Black", "Wild"};
        mat = new Card[3][4];
        pCount = p;
        logic = new Logic(pCount);
        pat = new Patron[pCount+1]; //one more patron than players
        try {
            //bkg = ImageIO.read(new File("bkg.png"));
            bkg = ImageIO.read(Splendorpanel.class.getResource("/images/gamebkg.jpg"));
            //rule1 =
            //rule2=
            //rule3=
            back1 = ImageIO.read(Splendorpanel.class.getResource("/images/back1.png"));//(top row of cards)
            back2=ImageIO.read(Splendorpanel.class.getResource("/images/back2.png"));
            back3=ImageIO.read(Splendorpanel.class.getResource("/images/back3.png")); //(last row of cards)
        } catch (Exception e) {
            System.out.println(e+"image issue splendor panel");
            return;
        }
        addMouseListener(this);

        for(int i = 0; i<3; i++){ //filling 2D matrix of cards on table, should draw each row from separate decks
            for(int j = 0; j<4; j++){
                mat[i][j] = logic.getDecks().get(i).drawCard();
            }
        }

        for(int i = 0; i<pat.length; i++){
            pat[i] = logic.getPatrons().getLast();
        }
        for(int i = 0; i<colors.length; i++){ //fill list of token images
            try{ 
                tokenImgs.add(ImageIO.read(Splendorpanel.class.getResource("/images/Token Images/" + colors[i] +"Token.png")));
            } catch (Exception e){
                System.out.println(e+"token image issue splendorpanel");
                return;
            }
        }
        
    }
    
    //main paint method for the entire splendorPanel
    public void paint(Graphics g){
        super.paint(g);
        if(!logic.getTokens(heldTokens)){ //if more than 10 tokens, if more than 3 tokens drawn, or if more than two of the same color
            heldTokens.clear(); //clear temporary hand
           drawError(g); //draw error and make players remove or redraw tokens
        }
        //need to test locations
        drawSetUp(g);
        drawCards(g); 
        drawPatron(g);  
        drawDeck(g);   
        drawTokens(g);
    }

    //draws the player info, cand the current player on screen
    public void drawSetUp(Graphics g){
        g.drawImage(bkg, 0, 0, getWidth(), getHeight(), null); //background

        g.setFont(new Font("Times New Roman", Font.BOLD, 37));
        g.drawString(logic.getPlayer().getName(), 656, 189); //Which player's turn

        g.setFont(new Font("Times New Roman", Font.PLAIN, 34));
        
        g.drawString("Player 1 - " + logic.getAllPlayers().get(0).getTotalVP() + " points", 56, 39); //player header and points
        g.drawString("Player 2 - " + logic.getAllPlayers().get(1).getTotalVP() + " points", 1392, 39);
        if(pCount >= 3)
            g.drawString("Player 3 - " + logic.getAllPlayers().get(2).getTotalVP() + " points", 1392, 1000);
        if(pCount >= 4)
            g.drawString("Player 4 - " + logic.getAllPlayers().get(3).getTotalVP() + " points", 56, 1000);


    }
    
    //draws the current cards that can be selected
    public void drawCards(Graphics g){
        int cardX = 714; //set start position of cards, increment x and y by __ for the cards after in the loop
        int cardY = 251;

        for(int i = 0; i<3; i++){ //printing cards matrix from bottom row to top
            for(int j = 0; j<4; j++){
                if(mat[i][j] != null){
                    g.drawImage(mat[i][j].getCardFront(), cardX, cardY, 95, 137, null); // need to test correct coordinates and height and width
                }
                cardX+= 129;
            }
            cardX = 714;  //reset x position for next row
            cardY+= 161; //increment y position for next row
        }
    }
    
    //draws the current patreons that can be selected
    public void drawPatron(Graphics g){
        int patX = 587; //set patron position and increment x in loop

        for(int i = 0; i<pat.length; i++){
            g.drawImage(pat[i].getPatFace(), patX,732, 91,93, null);
            patX+=125;
        }
    }

    //draws the backs of decks 1, 2, and 3
    public void drawDeck(Graphics g){
        for(int i = 0; i<3; i++){
            if(!logic.getDecks().get(i).deckEmpty()){
                if(i == 0){
                    g.drawImage(back1,587, 251, 150, 137, null );
                } else if (i == 1){
                    g.drawImage(back1,587, 411, 150, 137, null );
                } else {
                    g.drawImage(back1,587, 567, 150, 137, null );
                }
            }
        }
    }

    //draws the current tokens that can be taken
    public void drawTokens(Graphics g){
        int y= 313; //starting position for top token, incremented in loop

        for(int i = 0; i<colors.length; i++){
            if(logic.getTokens().get(colors[i])>0){ //if specific color tokens amount >0, print image
                g.drawImage(tokenImgs.get(i), 1240, y, 61, 61, null);
            }
            y+=88;
        }
    }

    public void drawError(Graphics g){
        g.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        g.setColor(Color.RED);
        g.drawString("Check if you have more than ten tokens\nor if you drew more than three tokens\nor more than two of the same type", 1000, 1000); //set real location later
    } 

    public void drawHands(Graphics g){//draw hands of all players and update current
        int x = 35; 
        int y= 121;
        int x2 =409;
        int y2 = 268;
        ArrayList <Card> tempHand = new ArrayList <Card> ();
        for (int i = 1; i<=pCount; i++){ 
            if(i == 2){ //depending on which player hand, set position of top left card
                x= 1437;
                x2= 1802;
            } else if (i==3){
                x= 1437;
                y=671;
                x2= 1802;
                y2=667;
            } else if (i==4){
                y=671;
                y2=667;
            }
            tempHand = logic.getAllPlayers().get(i-1).getTotalCards();
            int tempX = x;

            for(int j = 0; j<7; j++){ 
                if(j ==4){  //print hand of each player
                    x=tempX;
                    y+=155;
                }
                    g.drawImage(tempHand.get(i).getCardFront(), x, y,95, 137, null);
                    x+= 121;
           }
           if(tempHand.size()>7){
            //draw highlight around card --> more shelved cards
           }

           //g.drawImage(logic.getAllPlayers().get(i-1).getTotalReservedCards().get(0).getCardFront(), )
       }




        
    }

    //public boolean[] checkDeck(){

    //}

    public void getToken(String x){

    }

    public void mousePressed(MouseEvent e){
        int tokensY = 313;//location of tokens on board, top right corner of square
        int d = 61; //diameter of token image, set actual size later
        int mouseX = e.getX();
        int mouseY = e.getY();
        int cardX = 714; //set start position of cards, increment x and y by __ for the cards after in the loop
        int cardY = 251;
        int w = 95;
        int l = 137;

        for(int i = 0; i<colors.length; i++){
            if(mouseX>= 1240 && mouseX<= 1240 + d && mouseY>= tokensY && mouseY<= tokensY+d ){ //clicking each token
                if(heldTokens.containsKey(colors[i])){
                    heldTokens.replace(colors[i], heldTokens.get(colors[i]), heldTokens.get(colors[i]) +1 );
                } else {
                    heldTokens.put(colors[i], 1);
                }
            }
                tokensY += 88;
        }

        if(!logic.getTokens(heldTokens)){ //if wrong gameplay, repaint to show error
            repaint();
        } 

        //Clicking cards in matrix
        for(int i = 0; i<3; i++){ 
            for(int j = 0; j<4; j++){
                if(mouseX>= cardX && mouseX<= cardX + w && mouseY>= cardY && mouseY<= cardY + l ){
                    logic.buyCard(mat[i][j], i);

                }
                cardX+= 129;
            }
            cardX = 714; 
            cardY+= 161; 
        }
    }

    @Override
    public void mouseEntered(MouseEvent e){
        
    }

    @Override
    public void mouseClicked(MouseEvent e){
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(MouseEvent e){
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseReleased(MouseEvent e){
        // TODO Auto-generated method stub
    }

}//end of class

