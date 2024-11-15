import java.awt.Image;
import java.awt.image.*;
import java.util.*;

@SuppressWarnings("unused")// remove later, suppress problems of img not used
public class Card{
    private BufferedImage img;
    private HashMap<String,Integer> cost;
    private String gem;
    private int victoryPoints;
    public int tier;

    //constructor
    public Card(String input, int t){
        try {
            //img = ImageIO.read(Card.class.getResource("/Image/"+input+".png")); //pls edit when actual pictures are there
        } catch (Exception e) {
            System.out.println(e+"ur imgs are screwed - Card");
            return;
        }
        String[] vars = input.split(" ");
        cost.put("White",Integer.parseInt(vars[0]));
        cost.put("Blue",Integer.parseInt(vars[1]));
        cost.put("Green",Integer.parseInt(vars[2]));
        cost.put("Red",Integer.parseInt(vars[3]));
        cost.put("Black",Integer.parseInt(vars[4]));
        victoryPoints = Integer.parseInt(vars[5]);
        gem = vars[6];
        tier = t;
    }

    //returns amount of victory points the card has
    public int getVP(){
        return victoryPoints;
    }
    
    //returns the gem type of the card
    public String getGem() {
        return gem;
    } 
    
    //returns the cost of the card in gems
    public HashMap<String,Integer> getCost(){
        return cost;
    }

    //returns the front image of the card
    public Image getCardFront(){
        return img;
    }
    
}//end of class
