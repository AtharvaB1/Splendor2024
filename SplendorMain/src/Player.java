import java.util.*;
public class Player
{
    private int victoryPoints;
    private ArrayList<Card> reservedCards;
    private ArrayList<Card> heldCards;
    private ArrayList<Patron> heldPatrons;
    private HashMap<String, Integer> tokens;
    private HashMap<String, Integer> discounts;
   
    //constructor
    public Player(){
        victoryPoints = 0;
        reservedCards = new ArrayList<Card>();
        heldCards = new ArrayList<Card>();
        heldPatrons = new ArrayList<Patron>();
        tokens = new HashMap<String,Integer>();
        discounts = new HashMap<>();
    }


    //returns the hashmap of the tokens the player has
    public Map<String,Integer> getTokens(){
        return tokens;
    }
   
    //gets the total acount of tokens the player has
    public int tokenCount(){
        int ret = 0;
        Iterator<String> iter = tokens.keySet().iterator();
        for(int i = 0; i < tokens.size(); i++)
            ret += tokens.get(iter.next());
        return ret;
    }


    //returns an int amount of tokens that the player has for that type
    public int getTokenType(String gem){
        return tokens.get(gem);
    }


    // adds the tokens in the arrayList to the player, logic will run stuff to see if player needs to remove any tokens
    public void addTokens(Map<String, Integer> addGems){
        Iterator<String> iter = addGems.keySet().iterator();
        while(iter.hasNext())
        {
            String gemType = iter.next();
            if(tokens.containsKey(gemType))
                tokens.replace(gemType, tokens.get(gemType)+addGems.get(gemType));
            else
                tokens.put(gemType, 1);  
        }  
    }


    //removes the tokens in the arrayList from the player
    // returns true if it is legal to add, does nothing and returns false if it is not legal
    public boolean removeTokens(Map<String, Integer> removeGems){
        Iterator<String> iter = removeGems.keySet().iterator();
        while(iter.hasNext())
        {
            String gemType = iter.next();
            if(tokens.get(gemType)-removeGems.get(gemType) < 0)
                return false;
        }

        iter = removeGems.keySet().iterator();
        while(iter.hasNext())
        {
            String gemType = (String) iter.next();
            if(tokens.containsKey(gemType))
                tokens.replace(gemType, tokens.get(gemType)-removeGems.get(gemType));
        }
        return true;
    }


    //returns a hashMap of all the gems Types and the amount of discount the player has for them (can easily remove the hashmap if needed)
    public Map<String, Integer> getTotalDiscount(){
        return discounts;
    }


    //returns the amount of discount the player has for the specified material(ruby, sapphire, etc)
    public int getDiscountType(String thisGem){
        if(getTotalDiscount().containsKey(thisGem))
            return getTotalDiscount().get(thisGem);
        else
            return 0;
    }


    //returns if the player has enough discounts to buy a patreon card
    //returns true if they can, and false if they cant, will not effect any values
    public boolean canBuyPatreon(Patron thisPatron)
    {
        Map<String, Integer> comp = getTotalDiscount();
        Map<String, Integer> reqDiscount = thisPatron.getPatCost();
        Iterator<String> iter = reqDiscount.keySet().iterator();
        while(iter.hasNext())
        {
            String discount = iter.next();
            if(comp.get(discount) < reqDiscount.get(discount))
                return false;
        }
        return true;
    }


    //returns if the gem lists length is over 10 or not
    public boolean isOverTen()
    {
        if(tokenCount() > 10)
        {
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
        if(discounts.get(taken.getGem())!=null){
            discounts.put(taken.getGem(), discounts.get(taken.getGem())+1);
        } else{
            discounts.put(taken.getGem(), 1);
        }
    }


    //takes the selected patron and add it to the patrons ArrayList
    public void takePatron(Patron taken){
        heldPatrons.add(taken);
    }


    //checks if the player has less than 3 reserved cards.
    public boolean canReserve(){
        if(heldCards.size() < 3)
            return true;
        return false;
    }


    //takes the selected card and adds it to the reservedCards arraylist, also adds a wild gem to the gems arrayList (if one is available)
    //returns true if the player actually can reserve a card, does nothing and returns false if not (it will still return true if it can reserve a card but cant add a wild token)
    public boolean reserveCard(Card reserve){
        if(canReserve())
        {
            reservedCards.add(reserve);
            Map<String, Integer> wild = new HashMap<String,Integer>();
            wild.put("Wild", 1);
            addTokens(wild);
            return true;
        }
        return false;  
    }


    //removes the gems that are unaccounted by discounts and adds the card into the cards ArrayList
    //(returns true if the player has enough tokens and discounts, returns false and does not do anything if player does not have enough)
    public boolean buyCard(Card card){
        Map<String,Integer> cardCost = card.getCost();
        if(removeTokens(cardCost))
        {
            heldCards.add(card);
            return true;
        }
        return false;
    }


    //runs after every turn, checks if player has cards required to achieve Patron, automatically gives player the patron and doesn’t require their input.
    //(true if player has enough discounts, returns false and does not do anything if player does not have engough discounts)
    //only covers a sigle patreon, so we are gonna have to call this as many times as there are patreons on the board
    public boolean buyPatron(Patron patron){
        if(canBuyPatreon(patron))
        {
            heldPatrons.add(patron);
            return true;
        }
        return false;
    }


}//end of class

