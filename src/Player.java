import java.util.*;
public class Player implements Comparable<Player>{
    private int victoryPoints;
    private ArrayList<Card> reservedCards;
    private ArrayList<Card> heldCards;
    private ArrayList<Patron> heldPatrons;
    private HashMap<String, Integer> tokens;
    private HashMap<String, Integer> discounts;
    private String name;
    private int num;
   
    //constructor
    public Player(int n){
        victoryPoints = 0;
        num = n;
        name = "Player " + n;
        reservedCards = new ArrayList<Card>();
        heldCards = new ArrayList<Card>();
        heldPatrons = new ArrayList<Patron>();
        tokens = new HashMap<String,Integer>();
        tokens.put("White", 0); tokens.put("Green", 0); tokens.put("Blue", 0); tokens.put("Red", 0); tokens.put("Black", 0); tokens.put("Wild", 0);
        discounts = new HashMap<String,Integer>();
        discounts.put("White",0); discounts.put("Green",0); discounts.put("Blue",0); discounts.put("Red",0); discounts.put("Black",0);
    }

    //returns the hashmap of the tokens the player has
    public Map<String,Integer> getTokens(){
        return tokens;
    }

    //gets the total acount of tokens the player has
    public int tokenCount(){
        int count = 0;
        Iterator<String> iter = tokens.keySet().iterator();
        for(int i = 0; i < tokens.size(); i++)
            count += tokens.get(iter.next());
        return count;
    }

    //returns an int amount of tokens that the player has for that type
    public int getTokenType(String token){
        return tokens.get(token);
    }

    // adds the tokens in the arrayList to the player, logic will run stuff to see if player needs to remove any tokens
    public void addTokens(Map<String, Integer> addTokens){
        Iterator<String> iter = addTokens.keySet().iterator();
        while(iter.hasNext())
        {
            String tokenType = iter.next();
            tokens.put(tokenType, tokens.get(tokenType)+addTokens.get(tokenType));  
        }  
    }

    //removes the tokens in the arrayList from the player, acounting for discounts, logic will check if they can remove
    public void removeTokens(Map<String, Integer> removeTokens){
        Map<String, Integer> withDiscount = new HashMap<String, Integer>();
        Iterator<String> iter = removeTokens.keySet().iterator();
        while(iter.hasNext()){
            String value = iter.next();
            if(removeTokens.get(value) - discounts.get(value) < 0){
                withDiscount.put(value, 0);
            }
            else{
                withDiscount.put(value, removeTokens.get(value) - discounts.get(value));
            }
        }

        Iterator<String> disIter = withDiscount.keySet().iterator();
        while(disIter.hasNext()){
            String gemType = (String) disIter.next();
            tokens.replace(gemType, tokens.get(gemType)-withDiscount.get(gemType));
        }
    }

    //returns a hashMap of all the gems Types and the amount of discount the player has for them (can easily remove the hashmap if needed)
    public Map<String, Integer> getTotalDiscount(){
        return discounts;
    }

    //returns the amount of discount the player has for the specified material(ruby, sapphire, etc)
    public int getDiscountType(String thisGem){
        return discounts.get(thisGem);
    }

    //returns if the player has enough discounts to buy a patreon card
    //returns true if they can, and false if they cant, will not effect any values
    public boolean canBuyCard(Card card){
        HashMap<String,Integer> rich = new HashMap<>();
        rich.putAll(tokens);
        Iterator<String> iter = discounts.keySet().iterator();
        while(iter.hasNext()){
            String x = iter.next();
            rich.put(x, rich.get(x)+discounts.get(x));
        }

        int wildTokenChecker = rich.get("Wild");
        Iterator<String> check = card.getCost().keySet().iterator();
        while(check.hasNext()){
            String x = check.next();
            int y = card.getCost().get(x);

            if(rich.get(x)-y<0){
                y-=rich.get(x);
                wildTokenChecker-=y;
            }
        }
        if(wildTokenChecker<0)
            return false;
        return true;
    }

    //returns if the player is able to buy the inputted patreon
    public boolean canBuyPatreon(Patron thisPatron)
    {
        HashMap<String,Integer> cost = thisPatron.getPatCost();
        Iterator<String> iter = cost.keySet().iterator();
        while(iter.hasNext()){
            String x = iter.next();
            if(discounts.get(x)-cost.get(x)<0){
                return false;
            }
        }
        return true;
    }

    //returns if the gem lists length is over 10 or not
    public boolean isOverTen()
    {
        if(tokenCount() > 10){
            return true;
        }
        return false;
    }

    //returns the player score
    public int getTotalVP(){
        return victoryPoints;
    }

    //returns a list of all held player cards
    public ArrayList<Card> getTotalCards(){
        return heldCards;
    }

    //returns a list of all reserved player cards
    public ArrayList<Card> getTotalReservedCards(){
        return reservedCards;
    }

    //returns a list of all held patreon cards
    public ArrayList<Patron> getTotalPatrons(){
        return heldPatrons;
    }

    //takes the selected card and adds it into the cards ArrayList
    public void takeCard(Card taken){
        heldCards.add(taken);
        discounts.put(taken.getGem(),discounts.get(taken.getGem())+1);
        victoryPoints+=taken.getVP();
    }

    //takes the selected patron and add it to the patrons ArrayList
    public void takePatron(Patron taken){
        heldPatrons.add(taken);
        victoryPoints+=3;
    }

    //checks if the player has less than 3 reserved cards.
    public boolean canReserve(){
        if(reservedCards.size() < 3)
            return true;
        return false;
    }

    //reserves card, logic checks if they can reserve it
    public void reserveCard(Card reserve){
          reservedCards.add(reserve);
    }
    
    //returns name
    public String getName(){
        return name;
    }

    //returns the number of the player (order in which they play)
    public int getNum(){
        return num;
    }

    @Override
    //comparison method, compares the scores of the players, if tied, then whichever player is earlier
    public int compareTo(Player o) {
        if(o.getTotalVP() - this.getTotalVP() != 0)
            return o.getTotalVP() - this.getTotalVP();
        else
            return this.getNum() - o.getNum();
    }
   
}//end of class
