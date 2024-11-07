import java.awt.image.*;
import java.util.*;

@SuppressWarnings("unused")// remove later, suppress problems of img not used
public class Card{
    private BufferedImage img;
    private HashMap<String,Integer> cost;
    private String gem;
    private int victoryPoints;
    public Card(String input){
        try {
            //img = ImageIO.read(Card.class.getResource("/Image/"+input+".png")); //pls edit when actual pictures are there
        } catch (Exception e) {
            System.out.println(e+"ur imgs are screwed");
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
    }

    public int getVP(){
        return victoryPoints;
    }

    public String getGem() {
        return gem;
    }

    public HashMap<String,Integer> getCost(){
        return cost;
    }
}
