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
    private int cardWidth;
    private int cardHeight;
    private int patronWidth;
    private int patronHeight;
    private int tokenWidth;
    private int tokenHeight;
    private int fontSize;
    private int endButtonWidth;
    private int endButtonHeight;
    private int reserveCardWidth;
    private int reserveCardHeight;

    private int nextButtonX;
    private int reserveButtonX;
    private int dropButtonX;
    private int endButtonY;

    private HashMap<String,Integer> heldTokens; //three tokens held by current player when interacted with
    private Card heldCard;
    private BufferedImage bkg, endBkg, back1, back2, back3, rHigh, cHigh, reserveButton, endButton, dropButton;// background and rules, backs of three diff card types, highlights for the shelved cards
    Logic logic; //logic class
    private Card[][] mat;
    private ArrayList<Patron> pat;
    private String[] colors;
    private ArrayList <BufferedImage> tokenImgs;
    private boolean isHoldingCard;
    private boolean alreadyHolding;
    private boolean isIllegal;
    private boolean showAllCards;
    private boolean showReservedCards;
    private ArrayList <Card> temp;
    private BufferedImage reservedPopUp;
    private BufferedImage popUp;
    private int playerReserveShown;

    //constructor
    public Splendorpanel(int p){
        heldTokens = new HashMap<String,Integer>();
        heldCard = null;
        tokenImgs = new ArrayList <BufferedImage> ();
        temp = new ArrayList <Card> ();
        colors = new String[]{"White", "Blue", "Green", "Red", "Black", "Wild"};
        pCount = p;
        logic = new Logic(pCount);
        pat = new ArrayList<Patron>(); //one more patron than players
        mat = logic.getMatrix();
        isHoldingCard = false;
        showAllCards = false;
        showReservedCards = false;
         playerReserveShown = -1;
        try {
            bkg = ImageIO.read(Splendorpanel.class.getResource("/images/gamebkg.jpg"));
            endBkg = ImageIO.read(Splendorpanel.class.getResource("/images/gameEndButtonScreen.jpg"));
            popUp = ImageIO.read(Splendorpanel.class.getResource("/images/shelvedCardsPopUp.png"));
            reservedPopUp = ImageIO.read(Splendorpanel.class.getResource("/images/reservedPopUp.png"));
            //rule1 =
            //rule2 =
            //rule3 =
            reserveButton = ImageIO.read(Splendorpanel.class.getResource("/images/ReserveCard.png"));
            endButton = ImageIO.read(Splendorpanel.class.getResource("/images/NextButton.png"));
            dropButton = ImageIO.read(Splendorpanel.class.getResource("/images/CancelButton.png"));
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
        cardWidth = width / 22;
        cardHeight = height / 8;
        patronWidth = width / 21;
        patronHeight = height / 11;
        tokenWidth = width / 32;
        tokenHeight = height / 16;
        fontSize = width / 130 + height / 65;
        endButtonHeight = height / 5 - height / 10;
        endButtonWidth = width / 10;
        reserveCardWidth= cardWidth *3 - cardWidth + cardHeight / 4;
        reserveCardHeight = cardHeight * 3 - cardHeight + cardHeight / 5;

        nextButtonX = (width / 2 - width / 20);
        reserveButtonX = (width / 2 + (width / 20) * 2);
        dropButtonX = (width / 2 - (width / 20) * 4);
        endButtonY = (height - height / 6); 

        System.out.println(width + ", " + height + " this is width and height");

        //need to test locations
        drawSetUp(g); //background and text
        drawHands(g); //held cards, tokens, and num of tokens
        drawCards(g); //draw card matrix
        drawPatron(g);  //draw patrons
        drawDeck(g);   //draw deck backs
        drawTokens(g); //draw tokens on board

        if(showAllCards){ //drawing shelved cards //!!!!!!!!!NOT CENTERED YET!!!!!!
            int x = width / 3 - width / 26;
            int y = height / 10 + height / 8;
            g.drawImage(popUp, width / 5, height / 10 + height / 20, width / 2 + width / 10, height / 2 + height / 4, null);
           for(int i = 0; i<temp.size(); i++){
                g.drawImage(temp.get(i).getCardFront(), x, y, cardWidth + cardWidth / 2 + cardWidth / 3 - cardWidth / 4 - cardWidth / 15, cardHeight * 2 + cardHeight / 3 - cardHeight / 4 - cardHeight / 5, null);
                x +=width / 13;
                if(i == 4 || i == 9 || i == 14 || i == 19 || i == 24){
                    x = width / 3- width / 26;
                    y += height / 10 + height / 7;
                }
            }
        }

        if(showReservedCards){ 
            int bx = width / 3;
            int by = height / 2 - height /6 + height / 72;
            g.drawImage(reservedPopUp, 0, 0, width - width / 100, height, null);
            for(int i = 0; i<temp.size(); i++){
                g.drawImage(temp.get(i).getCardFront(), bx, by, reserveCardWidth, reserveCardHeight, null);
                bx+=width / 9 + width / 100;
            }
        }

        if(isHoldingCard){
            g.drawImage(reserveButton, reserveButtonX, endButtonY , endButtonWidth, endButtonHeight, null);
            g.drawImage(dropButton, dropButtonX, endButtonY, endButtonWidth, endButtonHeight, null);
            g.drawImage(endButton, nextButtonX, endButtonY, endButtonWidth, endButtonHeight, null);
            g.drawString("holding card!", width/ 3 + width / 8 ,  height / 5 - height / 50);
        }
        else if (heldTokens.size() > 0){
            nextButtonX = (width / 2 + (width / 20) * 2);
            g.drawImage(endButton, nextButtonX, endButtonY, endButtonWidth, endButtonHeight, null);
            g.drawImage(dropButton, dropButtonX, endButtonY, endButtonWidth, endButtonHeight, null);
        }
        else{
            nextButtonX = (width / 2 - width / 20);
            reserveButtonX = (width / 2 + (width / 20) * 2);
            dropButtonX = (width / 2 - (width / 20) * 3);
            endButtonY = (height - height / 6); 
        }

        if(logic.isItLastTurn())
            g.drawString("Last Turn!", width / 2 - width / 33, height - height / 50);

        if(logic.isGameOver())
            MainFrame.endGame(logic.getSortedPlayers());
    }

    //draws the player info, cand the current player on screen
    public void drawSetUp(Graphics g){
        g.drawImage(bkg, 0, 0, getWidth(), getHeight(), null); // normal background
       
        g.setFont(new Font("Times New Roman", Font.BOLD, fontSize * 3));
        g.drawString(logic.getPlayer().getName(),  width/ 3 + width / 12 , height / 5 - height / 13); //Which player's turn

        g.setFont(new Font("Times New Roman", Font.PLAIN, fontSize));
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
                    g.drawImage(mat[i][j].getCardFront(), cardX, cardY, cardWidth, cardHeight, null); // need to test correct coordinates and height and width
                }
                cardX+= width / 16;
            }
            cardX = width / 4 + width / 7;  //reset x position for next row
            cardY+= height / 7 + height / 150; //increment y position for next row
        }
    }
   
    //draws the current patreons that can be selected
    public void drawPatron(Graphics g){
        int patX = width / 4 + width / 12; //set patron position and increment x in loop

        for(int i = 0; i<pat.size(); i++){
            g.drawImage(pat.get(i).getPatFace(), patX, height  - height / 3, patronWidth, patronHeight, null);
            patX+=width / 16;
        }
    }

    //draws the backs of decks 1, 2, and 3
    public void drawDeck(Graphics g){
        for(int i = 0; i<3; i++){
            if(!logic.getDecks().get(i).deckEmpty()){
                if(i == 0){
                    g.drawImage(back1, width / 4 + width / 12, height / 5, cardWidth, cardHeight, null );
                } else if (i == 1){
                    g.drawImage(back2, width / 4 + width / 12, height / 5 + height / 7 + height / 150, cardWidth, cardHeight, null );
                } else {
                    g.drawImage(back3, width / 4 + width / 12, height / 5 + 2*(height / 7 + height / 150), cardWidth, cardHeight, null );
                }
            }
        }
    }

    //draws the current tokens that can be taken, ADD NUMBERS
    public void drawTokens(Graphics g){
        int y= height / 5 + height / 23; //starting position for top token, incremented in loop

        for(int i = 0; i<colors.length; i++){
            if(logic.getTokens().get(colors[i])>0){ //if specific color tokens amount >0, print image
                g.drawImage(tokenImgs.get(i), width / 2 + width / 7 + width / 150, y, tokenWidth, tokenHeight, null);
                g.setFont(new Font("Times New Roman", Font.PLAIN, fontSize)); 
                g.drawString("" + logic.getTokens().get(colors[i]), width / 2 + width / 6 - width / 150, y); //number of tokens left

                if(heldTokens.containsKey(colors[i])){
                    g.drawString("" + heldTokens.get(colors[i]), width / 2 + width / 5 - width / 100, y + y/ 100); //tokens selected
                }
            }
            y+=height / 12;
        }
    }

    //paints a clear error message if anything does wrong
    public void drawError(Graphics g){
        g.setFont(new Font("Times New Roman", Font.PLAIN, fontSize));
        g.setColor(Color.RED);
        g.drawString("Check if you have more than ten tokens\nor if you drew more than three tokens\nor more than two of the same type", 1000, 1000); //set real location later
    }

    //draw hands of all players and updates their current card and token counts
    public void drawHands(Graphics g){

        int handX = width / 50;
        int handY = height / 10 + height / 100;
        int tX = width / 50;
        int tY = height / 3 + height / 16;
        Player tempPlayer = null;
        ArrayList <Card> tempHand = new ArrayList <Card> (); //drawing all cards
        for (int i = 0; i<pCount; i++){

            if(i == 1){      //depending on which player hand, set position of top left card
                handX = width - width / 4 - width / 210;
                handY = height / 10 + height / 100;
                tX = width - width / 4 - width / 200;
                tY = height / 3 + height / 16;
            } else if (i==2){
                handX = width - width / 4 - width / 210;
                handY = height - height / 2 + height / 8;
                tX = width - width / 4 - width / 200;
                tY = height - height / 2 + height / 21;
            } else if (i==3){
                handX = width / 50 ;
                handY = height - height / 2 + height / 8;
                tX = width / 50;
                tY = height - height / 2 + height / 21;
            }

            tempPlayer = logic.getAllPlayers().get(i);
            tempHand = logic.getAllPlayers().get(i).getTotalCards();
            int tempX = handX; 
            int tempY = handY;

            for(int j = 0; j < 7; j++){
                if(tempHand.size() <= j)
                    break;
                
                if(j == 4){  //print hand of each player
                    tempX = handX;
                    tempY += height / 11 + height / 26 + height / 75;
                }
                g.drawImage(tempHand.get(j).getCardFront(), tempX, tempY, cardWidth, cardHeight, null);
                tempX += width / 16 + width / 2000;
           }

           if(tempHand.size()>7){
                //draw highlight around card --> more shelved cards //!!!!!!!!!NOT CENTERED YET!!!!!!!!!!
                g.drawImage(cHigh, handX + (width / 16) * 2, handY+ height / 11 + height / 26 + height / 75, cardWidth, cardHeight, null); 
           }
           
            if(logic.getAllPlayers().get(i).getTotalReservedCards().size()>1){
                //draw highlight around reserved card --> more shelved reserved cards //!!!!!!!!!NOT CENTERED YET!!!!!!!!!!
                g.drawImage(logic.getAllPlayers().get(i).getTotalReservedCards().get(0).getCardFront(), handX + (width / 16) * 3, handY + height / 11 + height / 26 + height / 69, cardWidth, cardHeight, null); //draw first reserved card
                g.drawImage(rHigh, handX + (width / 16) * 3, handY + height / 11 + height / 26 + height / 75, cardWidth, cardHeight, null); 
            }
            else if(logic.getAllPlayers().get(i).getTotalReservedCards().size()>0){
                g.drawImage(logic.getAllPlayers().get(i).getTotalReservedCards().get(0).getCardFront(), handX + (width / 16) * 3, handY + height / 11 + height / 26 + height / 69, cardWidth, cardHeight, null); //draw first reserved card
            }

            for(int j = 0; j<6; j++){ //draw tokens for each player
                if(tempPlayer.getTokens().get(colors[j])>0){
                    g.drawImage(tokenImgs.get(j), tX, tY, tokenWidth, tokenHeight, null);
                }

                if(i > 2){  //draw num of tokens held
                    g.drawString( "" + tempPlayer.getTokens().get(colors[j]), tX, tY);
                }else {
                    g.drawString( "" + tempPlayer.getTokens().get(colors[j]), tX, tY + tokenHeight);
                }
                tX += width / 24;
           }    
       }    
    }

    public void mousePressed(MouseEvent e){
        int mouseX = e.getX();
        int mouseY = e.getY();

        int tokensY = height / 5 + height / 23;//location of tokens on board, top right corner of square
        int tokensX = width / 2 + width / 7 + 15;
        int d = tokenWidth; //diameter of token image
        int cardX =  width / 4 + width / 7; //set start position of cards, increment x and y by __ for the cards after in the loop
        int cardY = height / 5;
        int w = cardWidth;
        int l = cardHeight;

        System.out.println("mouse clicked at "+ mouseX + ", " + mouseY);

        //if you click a token
        for(int i = 0; i<colors.length -1; i++){ //-1 to acount for wildtoken
            if(mouseX>= tokensX && mouseX<= tokensX + d && mouseY>= tokensY && mouseY<= tokensY+d ){ //clicking each token
                if(heldTokens.containsKey(colors[i])){
                    heldTokens.replace(colors[i], heldTokens.get(colors[i]), heldTokens.get(colors[i]) +1 );
                    System.out.println("TOKEN HIT!");
                    repaint();
                } else {
                    heldTokens.put(colors[i], 1);
                    System.out.println("TOKEN HIT!");
                    repaint();
                }
            }
            tokensY += height / 12;
        }

        if(showReservedCards && playerReserveShown == logic.getPlayer().getNum()-1){
            int rY = height / 2 - height /6 + height / 72;
            int rX = width / 3;
            for(int i = 0; i < logic.getPlayer().getTotalReservedCards().size(); i++)
            {
                System.out.println("" + rX + " " + rY);
                if(mouseX >= rX && mouseX <= rX + reserveCardWidth && mouseY >= rY && mouseY <= rY + reserveCardHeight && logic.getPlayer().canBuyCard(logic.getPlayer().getTotalReservedCards().get(i)))
                {
                    heldCard = logic.getPlayer().getTotalReservedCards().get(i);
                    isHoldingCard = true;
                    repaint();
                    return;
                }
            }
        } 
        else {}

        //if you Click a card in the card matrix
        for(int i = 0; i<3; i++){ 
            for(int j = 0; j<4; j++){
                
                if(mouseX>= cardX && mouseX<= cardX + w && mouseY>= cardY  && mouseY<= cardY + l && heldTokens.size() == 0 && heldCard == null){
                    heldCard = mat[i][j];
                    isHoldingCard = true;
                    repaint();
                }
                cardX+= width / 16;
            }
            cardX = width / 4 + width / 7; 
            cardY+= height / 7 + height / 150; 
        }

        if(mouseX >= nextButtonX && mouseX <= nextButtonX + endButtonWidth && mouseY >= endButtonY && mouseY <= endButtonY + endButtonHeight){
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
           if(heldCard != null && logic.getPlayer().canBuyCard(heldCard) && heldTokens.size() == 0){
               logic.addTokens(logic.diffrenceOfTokens(heldCard.getCost(), logic.getPlayer().getTotalDiscount()));
               logic.buyCard(heldCard);
               logic.endTurn();
               heldCard = null;
               isHoldingCard = false;
               showReservedCards = false;
               repaint();
               return;
           }
           System.out.println("nothing done");
        }

        if(isHoldingCard && logic.getPlayer().canReserve() && mouseX >= reserveButtonX && mouseX <= reserveButtonX + endButtonWidth && mouseY >= endButtonY && mouseY <= endButtonY + endButtonHeight){
           logic.reserveCard(heldCard);
            logic.removeCard(heldCard);
            isHoldingCard = false;
            heldCard = null;
            heldTokens = new HashMap<String, Integer>();
            repaint();
            logic.endTurn();
        }

        if((isHoldingCard || heldTokens.size() > 0) && mouseX >= dropButtonX && mouseX <= dropButtonX + endButtonWidth && mouseY >= endButtonY && mouseY <= endButtonY + endButtonHeight){
            isHoldingCard = false;
            heldCard = null;
            heldTokens = new HashMap<String, Integer>();
            repaint();
        }

        int handX = 0; int handY = 0;
        for(int i = 0; i < logic.getAllPlayers().size(); i++)
        {
            if(i == 0){ //yandere dev ass code
                handX = width / 50;
                handY = height / 10 + height / 100;
            }else if(i == 1){
                handX = width - width / 4 - width / 210;
                handY = height / 10 + height / 100;
            } else if (i==2){
                handX = width - width / 4 - width / 210;
                handY = height - height / 2 + height / 8;
            } else if (i==3){
                handX = width / 50;
                handY = height - height / 2 + height / 8;
            }
            System.out.println("" + (handX + (width / 16) * 3) + "  " + (handY + height / 11 + height / 26 + height / 69 )+ " waaaaa");

            if(mouseX>= handX + (width / 16) * 3 && mouseX<= handX + (width / 16) * 3 + w && mouseY >= handY + height / 11 + height / 26 + height / 69 && mouseY <= handY + height / 11 + height / 26 + height / 69 + l* 1.8){
                System.out.println("hitPopup");
                temp = logic.getAllPlayers().get(i).getTotalReservedCards();
                showReservedCards = !showReservedCards;
                playerReserveShown = i;
                repaint();
            }
        }

        for(int i = 0; i < logic.getAllPlayers().size(); i++)
        {
            if(i == 0){ //yandere dev ass codehandX + (width / 16) * 3
                handX = width / 50;
                handY = height / 10 + height / 100;
            }else if(i == 1){
                handX = width - width / 4 - width / 210;
                handY = height / 10 + height / 100;
            } else if (i==2){
                handX = width - width / 4 - width / 210;
                handY = height - height / 2 + height / 8;
            } else if (i==3){
                handX = width / 50;
                handY = height - height / 2 + height / 8;
            }
            System.out.println("" + (handX + (width / 16) * 3) + "  " + (handY + height / 11 + height / 26 + height / 69 )+ " waaaaa");

            if(mouseX>= handX + (width / 16) * 2 && mouseX<= handX + (width / 16) * 2 + w && mouseY >= handY + height / 11 + height / 26 + height / 69 && mouseY <= handY + height / 11 + height / 26 + height / 69 + l* 1.8){
                System.out.println("hitPopup");
                temp = logic.getAllPlayers().get(i).getTotalCards();
                showAllCards = !showAllCards;
                repaint();
            }
        }

    }

    @Override
    public void mouseEntered(MouseEvent e){ 
        // nothing needed
    }

    @Override
    public void mouseClicked(MouseEvent e){
        // nothing needed
    }

    @Override
    public void mouseExited(MouseEvent e){
        // nothing needed
    }

    @Override
    public void mouseReleased(MouseEvent e){
        // nothing needed
    }

}//end of class

