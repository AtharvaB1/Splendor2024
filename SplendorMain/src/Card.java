import java.util.*;
import javax.imageio.ImageIO;
import java.awt.image.*;

@SuppressWarnings("unused")// remove later, suppress problems of img not used
public class Card{
    private BufferedImage img;
    private int WhiteCost, BlueCost, GreenCost, RedCost, BlackCost;
    private String gem;
    private int VP;
    public Card(String input){
        try {
            //img = ImageIO.read(Card.class.getResource("/Image/"+input+".png")); //pls edit when actual pictures are there
        } catch (Exception e) {
            System.out.println(e+"ur imgs are screwed");
            return;
        }
        String[] vars = input.split(" ");
        WhiteCost = Integer.parseInt(vars[0]);
        BlueCost = Integer.parseInt(vars[1]);
        GreenCost = Integer.parseInt(vars[2]);
        RedCost = Integer.parseInt(vars[3]);
        BlackCost = Integer.parseInt(vars[4]);
        VP = Integer.parseInt(vars[5]);
        gem = vars[6];

        System.out.println(WhiteCost+BlueCost+GreenCost+RedCost+BlackCost+VP+""+gem);

    }

    public int getVP(){
        return VP;
    }
}
