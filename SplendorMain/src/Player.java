import java.util.*;
public class Player{
    private HashMap<String,Integer> tokens;
    private HashSet<Card> cards;
    private HashSet<Card> reservedCards;
    private int VP;
    private int coolness;

    public Player(int i)
    {
        coolness = i;
        tokens = new HashMap<>();
    }
}
