import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.lang.model.util.ElementScanner14;
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

    private int nextButtonX;
    private int reserveButtonX;
    private int dropButtonX;
    private int endButtonY;

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
    private boolean isIllegal;
    private boolean showAllCards;
    private boolean showReservedCards;
    private ArrayList <Card> temp;
    private BufferedImage reservedPopUp;
    private BufferedImage popUp;

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
        try {
            bkg = ImageIO.read(Splendorpanel.class.getResource("/Splendor2024/images/gamebkg.jpg"));
            endBkg = ImageIO.read(Splendorpanel.class.getResource("/images/gameEndButtonScreen.jpg"));
            popUp = ImageIO.read(Splendorpanel.class.getResource("/images/shelvedCardsPopUp.png"));
            reservedPopUp = ImageIO.read(Splendorpanel.class.getResource("/images/reservedPopUp.png"));
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
        cardWidth = width / 22;
        cardHeight = height / 8;
        patronWidth = width / 21;
        patronHeight = height / 11;
        tokenWidth = width / 32;
        tokenHeight = height / 16;
        fontSize = width / 130 + height / 65;
        endButtonHeight = height / 5 - height / 10;
        endButtonWidth = width / 10;
        nextButtonX = (width / 2 - width / 20);
        reserveButtonX = (width / 2 + (width / 20) * 2);
        dropButtonX = (width / 2 - (width / 20) * 4);
        endButtonY = (height - height / 6); 

        System.out.println(width + ", " + height + " this is width and height");
        //if(!logic.canGetTokens(heldTokens)){ //if more than 10 tokens, if more than 3 tokens drawn, or if more than two of the same color
           // heldTokens.clear(); //clear temporary hand
           ///drawError(g); //draw error and make players remove or redraw tokens
      //  }

        //need to test locations
        drawSetUp(g); //background and text
        drawHands(g); //held cards, tokens, and num of tokens
        drawCards(g); //draw card matrix
        drawPatron(g);  //draw patrons
        drawDeck(g);   //draw deck backs
        drawTokens(g); //draw tokens on board

        if(showAllCards){ //drawing shelved cards //!!!!!!!!!NOT CENTERED YET!!!!!!
            int x = 384;
            int y = 177;
            g.drawImage(popUp, 341, 139, 1239, 751, null);
           for(int i = 0; i<temp.size(); i++){
                g.drawImage(temp.get(i).getCardFront(), x, y, 206, 313, null);
                x+=237;
                if(i==5){
                    x=384;
                    y=537;
                }
            }
        }

        if(showReservedCards){ //drawing shelved reserved cards//!!!!!!!!!NOT CENTERED YET!!!!!!
            int x = 619;
            g.drawImage(reservedPopUp, 579, 290, 762, 388, null);
            for(int i = 0; i<temp.size(); i++){
                g.drawImage(temp.get(i).getCardFront(), x, 326, 206, 313, null);
                x+=237;
            }
        }

        if(isHoldingCard){
            g.setColor(new Color(255, 255, 255));
            g.drawRect(reserveButtonX, endButtonY , endButtonWidth, endButtonHeight);
            g.fillRect(reserveButtonX, endButtonY , endButtonWidth, endButtonHeight);
            g.setColor(new Color(0,0,0));

            g.setColor(new Color(0, 255, 0));
            g.drawRect(dropButtonX, endButtonY, endButtonWidth, endButtonHeight);
            g.fillRect(dropButtonX, endButtonY, endButtonWidth, endButtonHeight);
            g.setColor(new Color(0,0,0));

            g.drawString("holding card!", width/ 3 + width / 8 ,  height / 5 - height / 13);
        }
        else if (heldTokens.size() > 0){
            nextButtonX = (width / 2 + (width / 20) * 2);
            g.drawRect(nextButtonX, endButtonY, endButtonWidth, endButtonHeight);
            g.fillRect(nextButtonX, endButtonY, endButtonWidth, endButtonHeight);

            g.setColor(new Color(0, 255, 0));
            g.drawRect(dropButtonX, endButtonY, endButtonWidth, endButtonHeight);
            g.fillRect(dropButtonX, endButtonY, endButtonWidth, endButtonHeight);
            g.setColor(new Color(0, 0, 0));
        }
        else{
            nextButtonX = (width / 2 - width / 20);
            reserveButtonX = (width / 2 + (width / 20) * 2);
            dropButtonX = (width / 2 - (width / 20) * 3);
            endButtonY = (height - height / 6); 
            g.drawRect(nextButtonX, endButtonY, endButtonWidth, endButtonHeight);
            g.fillRect(nextButtonX, endButtonY, endButtonWidth, endButtonHeight);
        }

        if(logic.isItLastTurn())
            g.drawString("Last Turn!", width / 2 + width / 5, height / 4);

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
        //int x = 35;
        //int y = 121;
        //int x2 = 409;
        //int y2 = 268;
        ///int tX = 35 ;
        //int tY = 423;
        int x = width / 50;
        int y = height / 10 + height / 100;
        int x2 = width / 6;  //have not tested these values yet as reserved cards are not added yet //!!!!!!!!!NOT CENTERED YET!!!!!!!!!!
        int y2 = height / 5; //have not tested these values yet as reserved cards are not added yet //!!!!!!!!!NOT CENTERED YET!!!!!!!!!!
        int tX = width / 50;
        int tY = height / 3 + height / 16;
        Player tempPlayer = null;
        ArrayList <Card> tempHand = new ArrayList <Card> (); //drawing all cards
        for (int i = 1; i<=pCount; i++){
            if(i == 2){      //depending on which player hand, set position of top left card
                x = width - width / 4 - width / 210;
                y = height / 10 + height / 100;
                x2 = width / width / 5;
                y2 = height / 5;
                tX = width - width / 4 - width / 200;
                tY = height / 3 + height / 16;

                ///x= 1520;
                //x2= 1802;
                //tX = 1520;
            } else if (i==3){
                x = width - width / 4 - width / 210;
                y = height - height / 2 + height / 8;
                x2 = width / width / 5;
                y2 = height - height / 3;
                tX = width - width / 4 - width / 200;
                tY = height - height / 2 + height / 21;

                //x= 1520;
                //y=671;
                //x2= 1802;
                //y2=667;
                //tX = 1520;
                //tY = 584;
            } else if (i==4){
                  x = width / 50 ;
                  y = height - height / 2 + height / 8;
                  x2 = width / 6;
                  y2 = height - height / 3;
                  tX = width / 50;
                  tY = height - height / 2 + height / 21;

                //x = 35;
                //y=671;
                //x2 =409;
                //y2=667;
                //tX = 35;
                //tY = 584;
            }

            tempPlayer = logic.getAllPlayers().get(i-1);
            tempHand = logic.getAllPlayers().get(i-1).getTotalCards();
            int tempX = x;

            for(int j = 0; j < tempHand.size(); j++){
                if(j == 4){  //print hand of each player
                    x=tempX;
                    y+= height / 11 + height / 25;
                }
                    g.drawImage(tempHand.get(j).getCardFront(), x, y, cardWidth, cardHeight, null);
                    x+= width / 16;
           }

           if(tempHand.size()>7){
                //draw highlight around card --> more shelved cards //!!!!!!!!!NOT CENTERED YET!!!!!!!!!!
                g.drawImage(cHigh, x+ 234 , y+146, 107, 144, null); 
           }
   
           if(tempPlayer.getTotalReservedCards().size()>1){
                //draw highlight around reserved card --> more shelved reserved cards //!!!!!!!!!NOT CENTERED YET!!!!!!!!!!
                g.drawImage(rHigh, x+234 , y+356, 107, 144, null); 
                g.drawImage(tempPlayer.getTotalReservedCards().get(0).getCardFront(), x2, y2, cardWidth, cardHeight, null); //draw first reserved card
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

        //if you Click a card in the card matrix
        for(int i = 0; i<3; i++){ 
            for(int j = 0; j<4; j++){
                
                if(mouseX>= cardX && mouseX<= cardX + w && mouseY>= cardY  && mouseY<= cardY + l && logic.getPlayer().canBuyCard(mat[i][j])&& heldTokens.size() == 0){
                    heldCard = mat[i][j];
                    isHoldingCard = true;
                    repaint();
                }
                cardX+= width / 16;
            }
            cardX = width / 4 + width / 7; 
            cardY+= height / 7 + height / 150; 
        }

        //logic.endTurn();

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
           if(heldCard != null && heldTokens.size() == 0){
               logic.buyCard(heldCard);
               logic.addTokens(logic.diffrenceOfTokens(heldCard.getCost(), logic.getPlayer().getTotalDiscount()));
               logic.endTurn();
               heldCard = null;
               isHoldingCard = false;
               repaint();
               return;
           }
           System.out.println("nothing done");
        }

        if(isHoldingCard && mouseX >= reserveButtonX && mouseX <= reserveButtonX + endButtonWidth && mouseY >= endButtonY && mouseY <= endButtonY + endButtonHeight){
            //add reserving system here
        }

        if((isHoldingCard || heldTokens.size() > 0) && mouseX >= dropButtonX && mouseX <= dropButtonX + endButtonWidth && mouseY >= endButtonY && mouseY <= endButtonY + endButtonHeight){
            isHoldingCard = false;
            heldCard = null;
            heldTokens = new HashMap<String, Integer>();
            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e){ //!!!!!!!!!NOT CENTERED YET!!!!!!!!!!
        int mouseX = e.getX();
        int mouseY = e.getY();
        int [] x = {274, 1667, 1667, 274};
        int [] y = {272, 272, 671, 671};

        for(int i = 0; i<4; i++){ //expand shelved cards
            if(mouseX>= x[i] && mouseX<= x[i]+95 && mouseY>= y[i] && mouseY <= y[i]+137){
                temp = logic.getAllPlayers().get(i).getTotalCards();
                showAllCards = true;
                repaint();
            }
        }

        //expand reserved cards -- this is broken for some reason so put in a try catch parameter -- //!!!!!!!!!NOT CENTERED YET!!!!!!!!!!
        try{
            int [] x2 = {395, 1788, 1788, 395};
            int [] y2 = {272, 272, 671, 671};

            for(int i = 0; i<4; i++){
                if(mouseX>= x2[i] && mouseX<= x2[i]+95 && mouseY>= y2[i] && mouseY <= y2[i]+137){
                    temp = logic.getAllPlayers().get(i).getTotalReservedCards();
                    showReservedCards = true;
                    repaint();
                }
            }
        }catch(Exception c){
            System.out.println("error " + c);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e){
        // nothing needed
    }

    @Override
    public void mouseExited(MouseEvent e){
        int mouseX = e.getX();
        int mouseY = e.getY();
        int [] x = {274, 1667, 1667, 274};
        int [] y = {272, 272, 671, 671};

        for(int i = 0; i<4; i++){
            if(mouseX <= x[i] && mouseX>= x[i]+95 && mouseY<= y[i] && mouseY >= y[i]+137){
                showAllCards = false;
                repaint();
            }
        }


        int [] x2 = {395, 1788, 1788, 395};
        int [] y2 = {272, 272, 671, 671};

        for(int i = 0; i<4; i++){
            if(mouseX<= x2[i] && mouseX>= x2[i]+95 && mouseY<= y2[i] && mouseY >= y2[i]+137){
                showReservedCards = false;
                repaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e){
        // nothing needed
    }

}//end of class

