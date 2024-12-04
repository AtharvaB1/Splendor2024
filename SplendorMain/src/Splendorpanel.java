import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.*;


public class Splendorpanel extends JPanel implements MouseListener{
    private int pCount; //num of players
    private int width;
    private int height;
    //private String tok1, tok2, tok3; //tokens players click on?
    private HashMap<String,Integer> heldTokens; //three tokens held by current player when interacted with
    private Card heldCard;
    private BufferedImage bkg, endBkg, back1, back2, back3, rHigh, cHigh;// background and rules, backs of three diff card types, highlights for the shelved cards
    Logic logic; //logic class
    JButton endTurn, rPrev, rNext, rClose; //might not be using anymore
    private Card[][] mat;
    private ArrayList<Patron> pat;
    private String[] colors;
    private ArrayList <BufferedImage> tokenImgs;
    private boolean isHoldingCard;


    //constructor
    public Splendorpanel(int p){
        heldTokens = new HashMap<String,Integer>();
        heldCard = null;
        tokenImgs = new ArrayList <BufferedImage> ();
        colors = new String[]{"White", "Blue", "Green", "Red", "Black", "Wild"};
        pCount = p;
        logic = new Logic(pCount);
        pat = new ArrayList<Patron>(); //one more patron than players
        mat = logic.getMatrix();
        isHoldingCard = false;
        try {
            //bkg = ImageIO.read(new File("bkg.png"));
            bkg = ImageIO.read(Splendorpanel.class.getResource("/images/gamebkg.jpg"));
            endBkg = ImageIO.read(Splendorpanel.class.getResource("/images/gameEndButtonScreen.jpg"));
            //rule1 =
            //rule2 =
            //rule3 =
            cHigh = ImageIO.read(Splendorpanel.class.getResource("/images/cHighlight.png"));
            rHigh = ImageIO.read(Splendorpanel.class.getResource("/images/rHighlight.png"));
            back1 = ImageIO.read(Splendorpanel.class.getResource("/images/back1.png"));//(top row of cards)
            back2 = ImageIO.read(Splendorpanel.class.getResource("/images/back2.png"));
            back3 = ImageIO.read(Splendorpanel.class.getResource("/images/back3.png")); //(last row of cards)
        } catch (Exception e) {
            System.out.println(e+"image issue splendor panel");
            return;
        }
        addMouseListener(this);
        for(int i = 0; i<5; i++){
            pat.add(logic.getPatrons().get(i));
        }


        for(int i = 0; i<colors.length; i++){ //fill list of token images
            try{
                tokenImgs.add(ImageIO.read(Splendorpanel.class.getResource("/images/Token Images/" + colors[i] +"Token.png")));
            } catch (Exception e){
                System.out.println(e+"token image issue splendorpanel"+colors[i]);
                return;
            }
        }
    }
   
    //main paint method for the entire splendorPanel
    public void paint(Graphics g){
        super.paint(g);
        width = getWidth();
        height = getHeight();
        if(!logic.canGetTokens(heldTokens)){ //if more than 10 tokens, if more than 3 tokens drawn, or if more than two of the same color
            heldTokens.clear(); //clear temporary hand
            drawError(g); //draw error and make players remove or redraw tokens
        }

        //need to test locations
        drawSetUp(g); //background and text
        drawHands(g); //held cards, tokens, and num of tokens
        drawCards(g); //draw card matrix
        drawPatron(g);  //draw patrons
        drawDeck(g);   //draw deck backs
        drawTokens(g); //draw tokens on board

        g.drawString("A", width / 2 , height - height / 10 );
        if(isHoldingCard)
            g.drawString("holding card!",1200, 200);
        if(logic.isItLastTurn())
            g.drawString("Last Turn!", 1200, 900);
        if(logic.isGameOver())
            MainFrame.endGame(logic.getSortedPlayers());
    }

    //draws the player info, cand the current player on screen
    public void drawSetUp(Graphics g){
        if(logic.showEndButton()){
            g.drawImage(endBkg, 0, 0, getWidth(), getHeight(), null); //background with end button
        } else {
            g.drawImage(bkg, 0, 0, getWidth(), getHeight(), null); // normal background
        }
       
        g.setFont(new Font("Times New Roman", Font.BOLD, 45));
        g.drawString(logic.getPlayer().getName(),  width/ 3 + width / 8 , height / 5 - height / 13); //Which player's turn

        g.setFont(new Font("Times New Roman", Font.PLAIN, 34));
        g.drawString("Player 1 - " + logic.getAllPlayers().get(0).getTotalVP() + " points", width / 12, height / 10); //player header and points
        g.drawString("Player 2 - " + logic.getAllPlayers().get(1).getTotalVP() + " points", width - width / 5, height / 10);
        if(pCount > 2)
            g.drawString("Player 3 - " + logic.getAllPlayers().get(2).getTotalVP() + " points", width - width / 5, height - height / 12);
        if(pCount > 3)
            g.drawString("Player 4 - " + logic.getAllPlayers().get(3).getTotalVP() + " points", width / 12, height - height / 12);
    }
   
    //draws the current cards that can be selected
    public void drawCards(Graphics g){
        mat = logic.getMatrix();
        int cardX = width / 4 + width / 7; //set start position of cards, increment x and y by __ for the cards after in the loop
        int cardY = height / 5;


        for(int i = 0; i<3; i++){ //printing cards matrix from bottom row to top
            for(int j = 0; j<4; j++){
                if(mat[i][j] != null){
                    g.drawImage(mat[i][j].getCardFront(), cardX, cardY, 95, 137, null); // need to test correct coordinates and height and width
                }
                cardX+= 129;
            }
            cardX = width / 4 + width / 7;  //reset x position for next row
            cardY+= 161; //increment y position for next row
        }
    }
   
    //draws the current patreons that can be selected
    public void drawPatron(Graphics g){
        int patX = width / 4 + width / 12; //set patron position and increment x in loop

        for(int i = 0; i<pat.size(); i++){
            g.drawImage(pat.get(i).getPatFace(), patX, height  - height / 3, 95,93, null);
            patX+=127;
        }
    }

    //draws the backs of decks 1, 2, and 3
    public void drawDeck(Graphics g){
        for(int i = 0; i<3; i++){
            if(!logic.getDecks().get(i).deckEmpty()){
                if(i == 0){
                    g.drawImage(back1, width / 4 + width / 12, height / 5, 95, 137, null );
                } else if (i == 1){
                    g.drawImage(back2, width / 4 + width / 12, height / 5 + 161, 95, 137, null );
                } else {
                    g.drawImage(back3, width / 4 + width / 12, height / 5 + 322, 95, 137, null );
                }
            }
        }
    }

    //draws the current tokens that can be taken, ADD NUMBERS
    public void drawTokens(Graphics g){
        int y= height / 5 + height / 23; //starting position for top token, incremented in loop

        for(int i = 0; i<colors.length; i++){
            if(logic.getTokens().get(colors[i])>0){ //if specific color tokens amount >0, print image
                g.drawImage(tokenImgs.get(i), width / 2 + width / 7 + 15, y, 61, 61, null);
                g.drawString("" + logic.getTokens().get(colors[i]), width / 2 + width / 7 + 33, y); //number of tokens left
            }
            y+=88;
        }
    }


    //paints a clear error message if anything does wrong
    public void drawError(Graphics g){
        g.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        g.setColor(Color.RED);
        g.drawString("Check if you have more than ten tokens\nor if you drew more than three tokens\nor more than two of the same type", 1000, 1000); //set real location later
    }

    //draw hands of all players and updates their current card and token counts
    public void drawHands(Graphics g){
        int x = 35;
        int y = 121;
        int x2 = 409;
        int y2 = 268;
        int tX = 35 ;
        int tY = 423;
        Player tempPlayer = null;
        ArrayList <Card> tempHand = new ArrayList <Card> (); //drawing all cards
        for (int i = 1; i<=pCount; i++){
            if(i == 2){ //depending on which player hand, set position of top left card
                x= 1520;
                x2= 1802;
                tX = 1520;
            } else if (i==3){
                x= 1520;
                y=671;
                x2= 1802;
                y2=667;
                tX = 1520;
                tY = 584;
            } else if (i==4){
                x = 35;
                y=671;
                x2 =409;
                y2=667;
                tX = 35;
                tY = 584;
            }
            tempPlayer = logic.getAllPlayers().get(i-1);
            tempHand = logic.getAllPlayers().get(i-1).getTotalCards();
            int tempX = x;


            for(int j = 0; j < tempHand.size(); j++){
                if(j == 4){  //print hand of each player
                    x=tempX;
                    y+=155;
                }
                    g.drawImage(tempHand.get(j).getCardFront(), x, y,95, 137, null);
                    x+= 121;
           }


           if(tempHand.size()>7){
                //draw highlight around card --> more shelved cards
                g.drawImage(cHigh, x+ 234 , y+146, 107, 144, null);
           }
   
           if(tempPlayer.getTotalReservedCards().size()>1){
                //draw highlight around reserved card --> more shelved reserved cards
                g.drawImage(rHigh, x+234 , y+356, 107, 144, null);
                g.drawImage(tempPlayer.getTotalReservedCards().get(0).getCardFront(), x2, y2, 95, 137, null); //draw first reserved card
           }
         
           for(int j = 0; j<6; j++){ //draw tokens for each player
                if(tempPlayer.getTokens().get(colors[j])>0){
                    g.drawImage(tokenImgs.get(j), tX, tY, 61, 61, null);
                }

                if(i > 2){  //draw num of tokens held
                    g.drawString( "" + tempPlayer.getTokens().get(colors[j]), tX, tY - 0);
                }else {
                    g.drawString( "" + tempPlayer.getTokens().get(colors[j]), tX, tY + 68);
                }
                tX += 80;
           }    
       }    
    }

    public void mousePressed(MouseEvent e){
        int mouseX = e.getX();
        int mouseY = e.getY();

        int tokensY = height / 5 + height / 23;//location of tokens on board, top right corner of square
        int tokensX = width / 2 + width / 7 + 15;
        int d = 61; //diameter of token image
        int cardX =  width / 4 + width / 7; //set start position of cards, increment x and y by __ for the cards after in the loop
        int cardY = height / 5;
        int w = 95;
        int l = 137;

        System.out.println("mouse clicked at "+ mouseX + ", " + mouseY);

        //if you click a token
        for(int i = 0; i<colors.length; i++){
            if(mouseX>= tokensX && mouseX<= tokensX + d && mouseY>= tokensY && mouseY<= tokensY+d ){ //clicking each token
                if(heldTokens.containsKey(colors[i])){
                    heldTokens.replace(colors[i], heldTokens.get(colors[i]), heldTokens.get(colors[i]) +1 );
                } else {
                    heldTokens.put(colors[i], 1);
                }
            }
            tokensY += 88;
        }

        //if you Click a card in the token matrix
        for(int i = 0; i<3; i++){ 
            for(int j = 0; j<4; j++){
                
                if(mouseX>= cardX && mouseX<= cardX + w && mouseY>= cardY  && mouseY<= cardY + l && logic.getPlayer().canBuyCard(mat[i][j])&& heldTokens.size() == 0){
                    heldCard = mat[i][j];
                    isHoldingCard = true;
                    repaint();
                }
                cardX+= 129;
            }
            cardX = 714; 
            cardY+= 161; 
        }

        if(logic.showEndButton() && mouseX >= 587 && mouseX <= 914 && mouseY >= 867 && mouseY <= 993){ //switch player button on end button screen
            System.out.println("hit switch player button");
            logic.switchPlayer();
        }

        if(logic.showEndButton() && mouseX >= 808 && mouseX <= 1134 && mouseY >= 859 && mouseY <= 985){ //switch player button on normal screen
            System.out.println("hit switch player button");
            logic.switchPlayer();
        }

        //logic.endTurn();

        //dont know where the end buttone cords are for now so just assume that this is the end button, this handels everyhting that will happen when the endButton is pressed
        if( /*logic.showEndButton() && */mouseX >= 1007 && mouseX <= 1334 && mouseY >= 867 && mouseY <= 993){ //only allows to click endbutton if it is displayed
           System.out.println("hit endbutton");
           
           //if they have tokens
           if(logic.canGetTokens(heldTokens) && heldTokens.size() > 0){
                logic.getPlayer().addTokens(heldTokens);
                logic.removeTokens(heldTokens);
                logic.endTurn();
                heldTokens = new HashMap<String, Integer>();
                repaint();
                return;
           }

           //if they are holding a card
           if(heldCard != null && heldTokens.size() == 0){
               logic.buyCard(heldCard);
               logic.addTokens(logic.diffrenceOfTokens(heldCard.getCost(), logic.getPlayer().getTotalDiscount()));
               logic.endTurn();
               heldCard = null;
               isHoldingCard = false;
               repaint();
               return;
           }

           isHoldingCard = false;
           heldCard = null;
           heldTokens = new HashMap<String, Integer>();
           logic.endTurn();
           repaint();

           System.out.println("nothing done, skipping turn");
        }
    }

    @Override
    public void mouseEntered(MouseEvent e){
        int mouseX = e.getX();
        int mouseY = e.getY();
     //expand shelved cards

     //expand card
     //expand reserved cards

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

