import java.util.*;
public class Player{
    private HashMap<String,Integer> tokens;
    private HashSet<Card> cards;
    private HashSet<Card> rCards;
    private int coolPoints;

    public Player(int i) //a LOT MORE needs to be added here
    {
        coolPoints = i;
        tokens = new HashMap<>();
        cards = new HashSet<>();
        rCards = new HashSet<>();
        coolPoints=0;
    }
    public void addTokens(String tmp){
        String[] add = tmp.split(" ");
        if(add.length == 4){
            //this is 2 tokens added
        } else{
            //this is 3 tokens added
        }
    }
    public void addCards(Card tmp){
        cards.add(tmp);
        Iterator<Card> iter = cards.iterator();
        while(iter.hasNext()){
            coolPoints+=iter.next().getVP();// gets VP (JT wanted it to be called coolness in some way)
        }
    }

    public void addRCards(){

    }
    
    public boolean tokenCheck(){
        return tokens.size()>=10;
    }

    public boolean rCardsCheck(){
        return rCards.size()>=3;
    }

    public int getVP(){
        return coolPoints;
    }
}
