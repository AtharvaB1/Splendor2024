import java.util.*;
public class Logic {
    private ArrayList<Player> players;
    private ArrayList<Deck> decks;
    private int currPlayer;
    private boolean turnOver; //add more here
    private HashMap<String, Integer> tokens;
    
    public Logic(int count){
        tokens = new HashMap<>();
        if(count==4){
            tokens.put("White",7);
            tokens.put("Blue",7);
            tokens.put("Green",7);
            tokens.put("Red",7);
            tokens.put("Black",7);
            tokens.put("Wild",5);
        } else if(count==3){
            tokens.put("White",5);
            tokens.put("Blue",5);
            tokens.put("Green",5);
            tokens.put("Red",5);
            tokens.put("Black",5);
            tokens.put("Wild",3);
        } else{
            tokens.put("White",4);
            tokens.put("Blue",4);
            tokens.put("Green",4);
            tokens.put("Red",4);
            tokens.put("Black",4);
            tokens.put("Wild",2);
        }
        players = new ArrayList<>();
        for(int i=0; i<count;i++){
            players.add(new Player(i));
        }

    }
}
