import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Patron {
    @SuppressWarnings("unused") //suppress img not being used, will be used later so please remove when implemented
    private BufferedImage img;
    private HashMap<String,Integer> cost;

    //constructor
    public Patron(String input){
        cost = new HashMap<String,Integer>();
        try {
            //img = ImageIO.read(Card.class.getResource("/Image/"+input+".png")); //pls edit when actual pictures are there
        } catch (Exception e) {
            System.out.println(e+"ur imgs are screwed");
            return;
        }

        String[] vars = input.split(" ");
        cost.put("Red",Integer.parseInt(vars[0]));
        cost.put("Green",Integer.parseInt(vars[1]));
        cost.put("Blue",Integer.parseInt(vars[2]));
        cost.put("White",Integer.parseInt(vars[3]));
        cost.put("Black",Integer.parseInt(vars[4]));
    }

    //returns the discount card requierments needed to buy this card
    public HashMap<String,Integer> getPatCost() {
        return cost;
    }

    //returns the patreon image
    public Image getPatFace(){
        return img;
    }

}//end of class
